(function (activeProfiles,
           systemEnvironment,
           systemProperties,
           plugins) {
    return {
        server: {
            port: 6969
        },
        A: 12,
        B: {
            C: 'D12231',
            E: '[F, G]',
            H: {
                I: {
                    J: plugins.getData1.apply('Hello from application.js')
                }
            }
        }
    };
})