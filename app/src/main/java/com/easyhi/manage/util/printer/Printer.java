package com.easyhi.manage.util.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class Printer {


    public static void main(String[] args) throws IOException, InterruptedException {
        Printer printer = new Printer();
        String content = "<c>测试打印机标题</c><br/><cb>测试打印机标题</cb><br/>" +
                "<bold>测试加粗</bold><br/>" +
                "<bold>加粗<b>放大</b></bold><br/>" +
                "<bold>加粗<l>价高</l><w>加宽</w></bold><br/>" +
                "--------------------------------------<br/>" +
                "测试打印内容       9190<br/>" +
                "<qr>http://example.com</qr><br/>" +
                "<bold>二维码内容为 http://example.com</bold><br/>" +
                "<c>锐意提供技术支持</c><br/><br/><br/><br/><br/><br/>";
        String composition = content + "<cut/>";
//        List<List<PrintContent>> contentList = PrinterLayout.parseContent(composition);
//        printer.printImage(ImageUtil.generateExampleQr("ajjjjjssssssssssssssssssssssssssssssssssssssssssssssssssssdssddas", 400), 90);
//        printer.print(contentList);
    }

    public void print(List<List<PrintContent>> contentList) throws IOException, InterruptedException {
        for (List<PrintContent> printContents : contentList) {
            printContent(printContents);
//            println(PrinterLayout.getBytesForXMLTemplate("<br/><br/>"));
            cut();
        }
    }

    public void printContent(List<PrintContent> printContents) throws IOException, InterruptedException {
        for (PrintContent content : printContents) {
//            if (content.getType().equals("text")) {
//                println(PrinterLayout.getBytesForXMLTemplate(content.getContent()));
//            }
//            if (content.getType().equals("qr")) {
//                printImage(ImageUtil.generateExampleQr(content.getContent(), 400));
//            }
        }
    }


    private static void printArray(String[] contents) {
        for (String c : contents) {
            System.out.println(c);
        }
    }

    private void cut() {
        SunmiPrintHelper.getInstance().cutpaper();
    }

    public void feedPaper() {
        SunmiPrintHelper.getInstance().feedPaper();
    }

    /**
     * 打印文本
     *
     * @param text
     * @throws IOException
     */
    public void println(String text) {
        SunmiPrintHelper.getInstance().printText(text, 24f, false, false, null);
    }

    /**
     * 打印
     *
     * @throws IOException
     */
    public void println(byte[] printBytes) {
        ByteBuffer bf = ByteBuffer.allocate(printBytes.length + 5);
        bf.put(new byte[]{0x1c, 0x26});
        bf.put(printBytes);
        bf.put(new byte[]{0x1c, 0x2E});
        bf.put((byte) (0x0a & 0xff));
        bf.flip();
        SunmiPrintHelper.getInstance().sendRawData(bf.array());
    }

    public void printImage(byte[] printBytes) {
        ByteBuffer bf = ByteBuffer.allocate(printBytes.length + 1);
        bf.put(printBytes);
        bf.put((byte) (0x0a & 0xff));
        SunmiPrintHelper.getInstance().sendRawData(bf.array());
    }

}
