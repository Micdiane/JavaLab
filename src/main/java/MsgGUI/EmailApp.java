package MsgGUI;


import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailApp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public EmailApp() {
        // 设置窗口标题
        setTitle("Email App");

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();

        // 创建登录选项卡
        JPanel loginPanel = new LoginPanel();
        tabbedPane.addTab("登录", loginPanel);

        // 创建注册选项卡
        JPanel registerPanel = new RegistrationPanel();
        tabbedPane.addTab("注册", registerPanel);

        // 创建注册选项卡
        JPanel accountCancellationPanel = new AccountCancellationPanel();
        tabbedPane.addTab("注销", accountCancellationPanel);

        // 创建草稿选项卡
        JPanel draftPanel = new DraftPanel();
        tabbedPane.addTab("写草稿", draftPanel);

        // 创建Send Panel
        JPanel sendPanel = new EmailPanel();
        tabbedPane.addTab("发送", sendPanel);



        // 将选项卡面板添加到窗口
        add(tabbedPane);

        // 设置窗口大小、位置和关闭操作
        setSize(1080, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

        public static void main(String[] args) {
            FlatLightLaf.setup();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    EmailApp emailApp = new EmailApp();
                    emailApp.setVisible(true);
                }
            });
        }
    }