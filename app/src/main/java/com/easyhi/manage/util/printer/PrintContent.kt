package com.easyhi.manage.util.printer

data class PrintContent(
    val tag: String,
    val content: String,
)


var content = "<c>测试居中</c><br></br>" +
        "<bold>加粗</bold><br></br>" +
        "jsasadjcnjsnjs<br></br>" +
        "<cb>居中放大</cb><br></br>" +
        "<b>放大</b><br></br>" +
        "<line/>" +
        "<qr>https://www.baidu.com</qr><br></br>" +
        "<bold>加粗</bold><br></br>" +
        "<bar>123456</bar><br></br>" +
        "<th>商品#价格</th><br></br>" +
        "<tr>包子#￥23</tr><br></br>"
