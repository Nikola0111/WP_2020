module.exports = {
    // proxy all webpack dev-server requests starting with /api
    // to our Spring Boot backend (localhost:8098) using http-proxy-middleware
    // see https://cli.vuejs.org/config/#devserver-proxy
    devServer: {
        port:4200,
        proxy: {
            '^/': {
                target: 'http://localhost:8080/PocetniREST/rest',
                changeOrigin: true,
                secure:false,
                logLevel: 'debug'
            },
        }
    }
};