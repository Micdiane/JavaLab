package Receiver;

import Sender.miniemail.util.StringUtils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class Myreceiver {
    private int msgcnt;

    public static void receive(int cnt) throws Exception {
        // 服务器地址
        String servicePath = "pop.qq.com";
        // 端口号
        String duankou = "110";
        // 邮箱名
        String emailName = "1141504337@qq.com";
        // 授权码
        String password = "qjejagycjsdbhjfj";

        // 准备连接服务器的会话信息
        Properties props = new Properties();
        // 连接超时报错配置
        props.setProperty("mail.pop3.timeout", "2000");
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.port", duankou);
        props.setProperty("mail.pop3.host", servicePath);

        // 创建Session实例对象
        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        store.connect(emailName, password);

        // 获得收件箱 只能获取一个月内的邮件
        Folder folder = store.getFolder("INBOX");
        // 如果要读写的话可以选择READ_WRITE
        folder.open(Folder.READ_ONLY);

        // 获得收件箱中的邮件总数
        int messageCount = folder.getMessageCount();
        System.out.println("邮件总数: " + messageCount);

        // 获取最近30封邮件
        Message[] messages = folder.getMessages();
        parseFileMessage(messages,cnt);

        // 释放资源
        folder.close(true);
        store.close();
    }

    private static void parseFileMessage(Message[] messages,int msgcnt) throws Exception {
        if (messages == null || messages.length < 1)
            System.out.println("没有需要处理的邮件");

        // 解析所有邮件
        for (int i = messages.length - 1; i >= 0 && msgcnt >=0 ; i--)
//        for (int i = 0; i <= messages.length && msgcnt >=0 ; i++)
        {
            Message message = messages[i];
            if (message != null) {
                msgcnt--;
                MimeMessage msg = (MimeMessage) message;
                System.out.println("主题: " + getSubject(msg));
                System.out.println("发件人: " + getFrom(msg));
                System.out.println("当前为第" + msg.getMessageNumber() + "封邮件");
                boolean isContainerAttachment = isContainAttachment(msg);
                System.out.println("是否包含附件：" + isContainerAttachment);
                if (isContainerAttachment) {
                    saveAttachment(msg, "D:\\data\\mailFile\\", getSubject(msg)); //保存附件
                }
            }
        }
    }

    /**
     * 判断邮件中是否包含附件
     * @return 存在附件返回true，不存在返回false
     */
    public static boolean isContainAttachment(Part part) throws Exception {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application")) {
                        flag = true;
                    }

                    if (contentType.contains("name")) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }
        return flag;
    }

    private static String getSubject(MimeMessage msg) throws Exception {
        return MimeUtility.decodeText(msg.getSubject());
    }

    private static String getFrom(MimeMessage msg) throws Exception {
        InternetAddress[] from = (InternetAddress[]) msg.getFrom();

        if (from.length == 0) {
            return "";
        }

        return Arrays.stream(from)
                .filter(Objects::nonNull)
                .map(address -> {
                    try {
                        String personal = address.getPersonal();
                        String decodedPersonal = personal != null ? MimeUtility.decodeText(personal) : "";
                        return decodedPersonal + ":" + MimeUtility.decodeText(address.getAddress());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return "";
                    }
                })
                .collect(Collectors.joining(","));
    }

    /**
     * 保存文件
     * @param destDir   文件目录
     * @param fileName  文件名
     */
    public static void saveAttachment(Part part, String destDir,String fileName) throws Exception {
        if (part.isMimeType("multipart/*")) {
            //复杂体邮件
            Multipart multipart = (Multipart) part.getContent();
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //迭代处理邮件体，直到附件为止
                String disp = bodyPart.getDisposition();
                String decodeName = decodeText(bodyPart.getFileName());
                decodeName = StringUtils.isEmpty(decodeName)?fileName:decodeName;
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    saveFile(bodyPart.getInputStream(), destDir, decodeName);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart,destDir,fileName);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("name") || contentType.contains("application")) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeName);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(),destDir,fileName);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     * @param is 输入流
     * @param fileName 文件名
     * @param destDir 文件存储目录
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws Exception {
        createEmptyDirectory(destDir);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(new File(destDir + fileName).toPath()));
        int len;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 创建一个空目录
     */
    public static void createEmptyDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 文本解码
     */
    public static String decodeText(String encodeText) throws Exception {
        if (encodeText == null || encodeText.isEmpty()) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
}