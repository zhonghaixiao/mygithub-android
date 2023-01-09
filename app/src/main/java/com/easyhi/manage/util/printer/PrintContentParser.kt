package com.easyhi.manage.util.printer

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


object PrintContentParser {

    fun getBytesForXMLTemplate(template: String): List<PrintContent> {
        var template = template
        template = "<body>$template</body>"
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document =
                builder.parse(ByteArrayInputStream(template.toByteArray(StandardCharsets.UTF_8)))
            val root = document.documentElement
            return parseNode(root.childNodes)
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    private fun parseNode(nodeList: NodeList): List<PrintContent> {
        val nodes = mutableListOf<PrintContent>()
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            nodes.add(if(node.nodeType == Node.ELEMENT_NODE) {
                val nodeName = node.nodeName
                PrintContent(
                    nodeName.lowercase(Locale.getDefault()),
                    node.textContent
                )
            }else {
                PrintContent(
                    "empty",
                    node.textContent
                )
            })
        }
        return nodes
    }

}