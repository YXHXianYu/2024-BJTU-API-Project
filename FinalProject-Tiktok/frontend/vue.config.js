module.exports = {
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                pathRewrite: { '^/api': '' },
            },
        },
        host: '0.0.0.0',
        port: 5000,
        disableHostCheck: true,
    },
    chainWebpack: config => {
        config.module
            .rule('raw')
            .test(/\.proto$/)
            .use('raw-loader')
            .loader('raw-loader')
            .end();
    },
};