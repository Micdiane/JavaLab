package MsgGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmailPanel extends JPanel {
    private JLabel recipientLabel;
    private JTextField recipientTextField;
    private JCheckBox ccCheckBox;
    private JCheckBox bccCheckBox;
    private JLabel subjectLabel;
    private JTextField subjectTextField;
    private JButton attachButton;
    private JButton photoButton;
    private JButton htmlButton;
    private JTextArea messageTextArea;
    private JButton searchButton;
    private JButton addressBookButton;

    public EmailPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        ImageIcon sendIcon = new ImageIcon("D:\\lessons\\JAVA\\lab\\Final\\mini-email-main\\src\\main\\java\\MsgGUI\\icons\\Envelope.png");
        Image image = sendIcon.getImage();
        Image newImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel sendLabel = new JLabel("新邮件", sendIcon, JLabel.LEFT);
        topPanel.add(sendLabel, BorderLayout.WEST);
        JButton sendButton = new JButton("发送");
        JButton saveDraftButton = new JButton("存草稿");
        JButton closeButton = new JButton("关闭");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(saveDraftButton);
        buttonPanel.add(closeButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);



        add(topPanel, BorderLayout.NORTH);








        JPanel messagePanel = new JPanel(new BorderLayout());

        JPanel recipientPanel = new JPanel(new FlowLayout());
        recipientTextField = new JTextField(20);
        ccCheckBox = new JCheckBox("抄送");
        bccCheckBox = new JCheckBox("密送");
        recipientLabel = new JLabel("收件人:");
        recipientPanel.add(recipientLabel);
        recipientPanel.add(recipientTextField);
        recipientPanel.add(ccCheckBox);
        recipientPanel.add(bccCheckBox);
        subjectLabel = new JLabel("主题:");
        subjectTextField = new JTextField(20);
        recipientPanel.add(subjectLabel, BorderLayout.SOUTH);
        recipientPanel.add(subjectTextField, BorderLayout.SOUTH);
        JPanel attachmentPanel = new JPanel(new FlowLayout());
        attachButton = new JButton("添加附件");
        photoButton = new JButton("添加照片");
        htmlButton = new JButton("添加HTML文档");
        attachmentPanel.add(attachButton);
        attachmentPanel.add(photoButton);
        attachmentPanel.add(htmlButton);
        recipientPanel.add(attachmentPanel, BorderLayout.WEST);

        messagePanel.add(recipientPanel, BorderLayout.NORTH);
        messageTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.CENTER);


        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchButton = new JButton("查找联系人");
        addressBookButton = new JButton("通讯录");
        searchPanel.add(searchButton);
        searchPanel.add(addressBookButton);
        add(searchPanel, BorderLayout.SOUTH);



        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理发送按钮点击事件
            }
        });

        saveDraftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理存草稿按钮点击事件
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理关闭按钮点击事件
            }
        });

        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理添加附件按钮点击事件
            }
        });

        photoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理添加照片按钮点击事件
            }
        });

        htmlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理添加HTML文档按钮点击事件
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理查找联系人按钮点击事件
            }
        });

        addressBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理通讯录按钮点击事件
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("邮件面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new EmailPanel());
        frame.pack();
        frame.setVisible(true);
    }
}