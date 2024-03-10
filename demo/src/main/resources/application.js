(function (activeProfiles,
           systemEnvironment,
           systemProperties,
           plugins) {
    return {
        server: {
            port: 8987
        },
        A: 12,
        B: {
            C: 'D12231',
            E: ['F', 'G'],
            H: {
                I: {
                    J: plugins.getData.apply('Hello from application.js')
                }
            }
        }
    };
})