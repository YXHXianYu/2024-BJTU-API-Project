module.exports = {
    devServer: {
        port: 5000
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