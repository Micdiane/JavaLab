package Sender.miniemail;

import Sender.miniemail.config.MailConfig;
import Sender.miniemail.constant.EmailContentTypeEnum;
import Sender.miniemail.constant.SmtpHostEnum;
import Sender.miniemail.core.MiniEmail;
import Sender.miniemail.core.MiniEmailFactory;
import Sender.miniemail.core.MiniEmailFactoryBuilder;
import Receiver.Myreceiver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MiniEmailTests {
    // 该邮箱修改为你需要测试的收件邮箱地址
    private static final String TO_EMAIL = "1141504337@qq.com";
    // 发送邮件给多个收件人
    private static final String[] TO_EMAILS = new String[]{"1141504337@qq.com"};

    MiniEmailFactory miniEmailFactory;

    @Before
    public void before() {
        // 创建工厂类
        miniEmailFactory = new MiniEmailFactoryBuilder().build(MailConfig.config("1141504337@qq.com", "qjejagycjsdbhjfj")
                .setMailDebug(Boolean.TRUE)
                .setSenderNickname("Micdiane")
                .setMailSmtpHost(SmtpHostEnum.SMTP_QQ)
        );
    }

    /**
     * 快速发送文本邮件，2行代码搞定
     */
    @Test
    public void testSendText() {
        MiniEmail miniEmail = miniEmailFactory.init();
        List<String> sendSuccessToList = miniEmail
//                .addCarbonCopy(new String[]{TO_EMAIL})
                .send(TO_EMAILS, "这是来自Micdiane Java Final Lab 的 mini-email 的测试信文本内容");
        System.out.println("send success to = " + sendSuccessToList);
    }

    @Test
    public void receiverText() throws Exception {
        Myreceiver.receive(30);

    }


    /**
     * 快速发送Html邮件，2行代码搞定
     */
    @Test
    public void testSendHtml() {
        MiniEmail miniEmail = miniEmailFactory.init();
        List<String> sendSuccessToList = miniEmail
                .addBlindCarbonCopy(new String[]{TO_EMAIL})
                .send(TO_EMAILS, "HTML邮件主题", EmailContentTypeEnum.HTML, "<h1 style='color:red;'>这是来自 mini-email 的测试 HTML 内容</h1>");
        System.out.println("send success to = " + sendSuccessToList);
    }

    /**
     * 复杂组合发送（附件+抄送+密送）
     */
    @Test
    public void testSendAttachFileAndURL() throws MalformedURLException {
        File file = new File("src/test/Testsrc/lswl.png");

        Assert.assertTrue("图片不存在", file.exists());

        URL url = new URL("https://avatars.githubusercontent.com/u/26403930?s=460&u=1a90eb155a8dbb56385be72a90fdd2911a068409&v=4");
        long start = System.currentTimeMillis();

        MiniEmail miniEmail = miniEmailFactory.init();
        List<String> sendSuccessToList = miniEmail
//                .addCarbonCopy(new String[]{"1846316024@qq.com"})
                .addBlindCarbonCopy(new String[]{TO_EMAIL})
                .addAttachment(file, "666.jpg")
                .addAttachment(url, "THUNDZENG的头像.jpg")
                .send(TO_EMAILS, "致我们失去的青春", EmailContentTypeEnum.HTML, "<h2 style='color:blue;'>这是来自 mini-email 的测试 HTML 内容</h2><br /><h2 style='color:red;'>请查看附件内容</h2>");
        System.out.println("send finish,use time = " + (System.currentTimeMillis() - start) / 1000);

        System.out.println("send success to = " + sendSuccessToList);
    }
}
