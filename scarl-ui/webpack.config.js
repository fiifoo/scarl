var webpack = require('webpack')
var ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
    entry: './src/index.jsx',
    output: {
        filename: 'index.js',
        path: './dist'
    },
    module: {
        loaders: [
            { test: /\.js|jsx$/, exclude: /node_modules/, loader: 'babel-loader' },
            { test: /\.css$/, loader: ExtractTextPlugin.extract('style', 'css-loader') },
            { test: /\.png$/, loader: 'url-loader?limit=100000' },
            { test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=application/font-woff' },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=application/octet-stream'},
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file'},
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=image/svg+xml' }
        ]
    },
    plugins: [
        new ExtractTextPlugin('index.css'),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('production')
            }
        }),
    ],
    devServer: {
        port: 80
    }
}
