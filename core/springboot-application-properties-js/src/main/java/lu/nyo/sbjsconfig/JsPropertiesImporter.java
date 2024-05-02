package lu.nyo.sbjsconfig;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.TypeLiteral;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader.Provider;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toMap;
import static lu.nyo.sbjsconfig.JsPropertiesImporter.JsPropertiesImporterException.ERROR_CODE.FILE_NOT_FOUND;
import static lu.nyo.sbjsconfig.JsPropertiesImporter.JsPropertiesImporterException.ERROR_CODE.SCRIPT_EXECUTION_ERROR;
import static lu.nyo.utils.ExtraMapUtils.flatten;
import static org.graalvm.polyglot.Source.newBuilder;

public final class JsPropertiesImporter implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    static class JsPropertiesImporterException extends RuntimeException {

        enum ERROR_CODE {
            FILE_NOT_FOUND("application.js Not found in the resources"),
            SCRIPT_EXECUTION_ERROR("An error encountered while running the application.js");

            private final String message;

            ERROR_CODE(String message) {
                this.message = message;
            }

        }

        public JsPropertiesImporterException(ERROR_CODE errorCode, Throwable cause) {
            super(errorCode.message, cause);
        }
    }

    private static final String LANG_JS = "js";

    private static final String APPLICATION_PROPERTIES_FILE_NAME = "application.js";

    private static final String PROPERTY_SOURCE_NAME = "application";

    private static final TypeLiteral<Map<String, Object>> TYPE_LITERAL = new TypeLiteral<>() {
    };

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {
        final URL jsConfigPath = getApplicationJsFileUrl(environmentPreparedEvent);
        final Context context = createContext();

        final Source source;
        try {
            assert jsConfigPath != null;
            source = createSource(jsConfigPath);
        } catch (IOException | AssertionError e) {
            throw new JsPropertiesImporterException(FILE_NOT_FOUND, e.getCause());
        }

        try {
            final Map<String, Object> userDefinedConfiguration = runScript(context, source, environmentPreparedEvent);

            final Map<String, String> flattenedMap = flatten(userDefinedConfiguration, Object::toString);

            final Properties properties = new Properties();
            flattenedMap.forEach(properties::setProperty);

            final PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource(PROPERTY_SOURCE_NAME, properties);

            environmentPreparedEvent.getEnvironment().getPropertySources().addFirst(propertiesPropertySource);
        } catch (Exception e) {
            throw new JsPropertiesImporterException(SCRIPT_EXECUTION_ERROR, e.getCause());
        } finally {
            context.close(true);
        }
    }

    private static Map<String, Plugin<Object, Object>> getPlugins() {
        return load(Plugin.class)
                .stream()
                .map(Provider::get)
                .collect(toMap(Plugin::getName, p -> ((Plugin<Object, Object>) p)));
    }

    private static Context createContext() {
        return Context.newBuilder(LANG_JS)
                .allowAllAccess(true)
                .build();
    }

    private static URL getApplicationJsFileUrl(ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {
        return environmentPreparedEvent
                .getSpringApplication()
                .getMainApplicationClass()
                .getClassLoader()
                .getResource(APPLICATION_PROPERTIES_FILE_NAME);
    }

    private static Source createSource(URL jsConfigPath) throws IOException {
        return newBuilder(LANG_JS, jsConfigPath).build();
    }

    private static Map<String, Object> runScript(Context context,
                                                 Source source,
                                                 ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {

        final ConfigurableEnvironment configurableEnvironment = environmentPreparedEvent.getEnvironment();
        final String[] activeProfiles = configurableEnvironment.getActiveProfiles();
        final Map<String, Object> systemEnvironment = configurableEnvironment.getSystemEnvironment();
        final Map<String, Object> systemProperties = configurableEnvironment.getSystemProperties();
        final Map<String, Plugin<Object, Object>> plugins = getPlugins();

        return context.eval(source)
                .execute(activeProfiles, systemEnvironment, systemProperties, plugins)
                .as(TYPE_LITERAL);
    }
}
