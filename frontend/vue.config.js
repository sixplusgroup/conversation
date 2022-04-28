module.exports = {
    publicPath:'./',
    configureWebpack: {
        devtool: 'source-map',
        devServer: {
            // proxy: {
            //     '/lala':{
            //         target:'http://localhost:8001/api',
            //         // ws:true,
            //         // changeOrigin:true,//允许跨域
            //         pathRewrite:{
            //             '/lala':'client1:123456@'   //请求的时候使用这个api就可以
            //         }
            //     },
            // },
            port: 8000
        }
    },
}