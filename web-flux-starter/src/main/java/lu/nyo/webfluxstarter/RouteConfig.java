package lu.nyo.webfluxstarter;

public class RouteConfig {
    private String path;
    private String method;
    private String handlerClass;

    private String handlerImpl;

    public String getHandlerImpl() {
        return handlerImpl;
    }

    public void setHandlerImpl(String handlerImpl) {
        this.handlerImpl = handlerImpl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(String handlerClass) {
        this.handlerClass = handlerClass;
    }

    @Override
    public String toString() {
        String format = """
                  {
                    path: %s,
                    method: %s,
                    handler: %s,
                    handlerImpl: %s
                  }
                """;
        return String.format(format, path, method, handlerClass, handlerImpl);
    }
}
