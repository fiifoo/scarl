const MiniCssExtractPlugin = require('mini-css-extract-plugin')

module.exports = {
    entry: './src/index.jsx',
    output: {
        filename: 'admin.js',
        path: __dirname + '/dist',
    },
    module: {
        rules: [
            { test: /\.js|jsx$/, exclude: /node_modules/, loader: 'babel-loader' },
            { test: /\.css$/, use: [MiniCssExtractPlugin.loader, 'css-loader']},
            { test: /\.(png|jpg|gif)$/, use: [{loader: 'url-loader', options: {limit: 8192}}]},
            { test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/, use: [{loader: 'url-loader', options: {limit: 8192,mimetype: 'application/font-woff'}}]},
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, use: [{loader: 'url-loader', options: {limit: 8192, mimetype: 'application/octet-stream'}}]},
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file-loader'},
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, use: [{loader: 'url-loader', options: {limit: 8192, mimetype: 'image/svg+xml'}}]},
        ]
    },
    plugins: [
        new MiniCssExtractPlugin({filename: 'admin.css'})
    ],
    devServer: {
        inline: false,
        port: 8081,
    }
}
