let pages = require('@lion/lion-front-core/src/webpack/vue.config');
let webpack=require('webpack');
let CompressionPlugin = require("compression-webpack-plugin");
module.exports = {
    filenameHashing: true,
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery"
            })
        ],
        output: {
            filename: `js/[name].[hash].js`,
            chunkFilename: `js/[name].[hash].js`
        },
    },
    "pages":pages.pages(),
    "css": {
        loaderOptions: {
            less: {
                lessOptions: {
                    javascriptEnabled: true
                }
            }
        }
    },
    chainWebpack: (config) => {
        config.plugin('compressionPlugin').use(new CompressionPlugin({
            test: /\.(js|css|less)$/, // 匹配文件名
            threshold: 10240, // 对超过10k的数据压缩
            minRatio: 0.8,
            deleteOriginalAssets: true // 删除源文件
        }))
    }

}

