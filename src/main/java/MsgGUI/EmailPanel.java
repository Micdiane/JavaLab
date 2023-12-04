package MsgGUI;

import Sender.miniemail.config.MailConfig;
import Sender.miniemail.constant.EmailContentTypeEnum;
import Sender.miniemail.constant.SmtpHostEnum;
import Sender.miniemail.core.MiniEmail;
import Sender.miniemail.core.MiniEmailFactory;
import Sender.miniemail.core.MiniEmailFactoryBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import Sender.miniemail.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class EmailPanel extends BorderPane {
    private TextField recipientTextField;
    private CheckBox ccCheckBox;
    private CheckBox bccCheckBox;
    private TextField subjectTextField;
    private TextArea messageTextArea;

    private static final String TO_EMAIL = "1141504337@qq.com";
    private static final String[] TO_EMAILS = new String[]{"1141504337@qq.com"};
    MiniEmailFactory miniEmailFactory;

    public EmailPanel() {
        this.miniEmailFactory = (new MiniEmailFactoryBuilder()).build(MailConfig.config("1141504337@qq.com", "qjejagycjsdbhjfj").setMailDebug(Boolean.TRUE).setSenderNickname("Micdiane").setMailSmtpHost(SmtpHostEnum.SMTP_QQ));
        MiniEmail miniEmail = this.miniEmailFactory.init();

        setPadding(new Insets(10));

        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(0, 0, 10, 0));

        Label sendLabel = new Label("新邮件");
        Image envelopeImage = new Image("file:D:\\lessons\\JAVA\\lab\\Final\\mini-email-main\\src\\main\\java\\MsgGUI\\icons\\Envelope.png");
        ImageView envelopeImageView = new ImageView(envelopeImage);
        sendLabel.setGraphic(envelopeImageView);

        Button sendButton = new Button("发送");
        Button saveDraftButton = new Button("存草稿");
        FlowPane buttonPanel = new FlowPane();
        buttonPanel.getChildren().addAll(sendButton, saveDraftButton);
        topPanel.getChildren().addAll(sendLabel, buttonPanel);

        setTop(topPanel);

        VBox recipientPanel = new VBox(10);
        recipientPanel.setPadding(new Insets(0, 0, 10, 0));

        recipientTextField = new TextField();
        ccCheckBox = new CheckBox("抄送");
        bccCheckBox = new CheckBox("密送");
        subjectTextField = new TextField();
        recipientPanel.getChildren().addAll(
                new Label("收件人:"), recipientTextField,
                new Label("主题:"), subjectTextField,
                ccCheckBox, bccCheckBox
        );

        VBox attachmentPanel = new VBox(10);
        Button attachButton = new Button("添加附件");
        Button urlButton = new Button("添加URL");
        attachmentPanel.getChildren().addAll(attachButton, urlButton);

        recipientPanel.getChildren().add(attachmentPanel);

        setLeft(recipientPanel);

        messageTextArea = new TextArea();
        setCenter(messageTextArea);

        FlowPane searchPanel = new FlowPane();
        Button searchButton = new Button("查找联系人");
        Button addressBookButton = new Button("通讯录");
        searchPanel.getChildren().addAll(searchButton, addressBookButton);

        setBottom(searchPanel);

        sendButton.setOnAction(e -> {
            // 处理发送按钮点击事件
            String content = messageTextArea.getText();
            List<String> sendSuccessToList = miniEmail.send(TO_EMAILS, content);

            // Display an alert based on the send result
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("邮件发送状态");

            if (sendSuccessToList != null && !sendSuccessToList.isEmpty()) {
                alert.setHeaderText(null);
                alert.setContentText("邮件成功发送给: " + String.join(", ", sendSuccessToList));
            } else {
                alert.setHeaderText("Error");
                alert.setContentText("邮件发送失败");
            }

            alert.showAndWait();
        });

        saveDraftButton.setOnAction(e -> {
            // 处理存草稿按钮点击事件

        });

        attachButton.setOnAction(e -> {
            // Show a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            Window primaryStage = null;
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Check if a file is selected
            if (selectedFile != null) {
                // Add the attachment to MiniEmail
                String filename = selectedFile.getName();
                miniEmail.addAttachment(selectedFile, filename);

                // Perform further actions if needed
                System.out.println("Attachment added: " + filename);
            }
        });

        urlButton.setOnAction(e -> {
            // 处理添加URL

            // 创建一个弹窗，输入为URL和文件名
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add URL Attachment");
            dialog.setHeaderText("Enter the URL and Filename:");
            dialog.setContentText("URL:");

            // Create a text field for the filename
            TextField filenameField = new TextField();
            filenameField.setPromptText("Filename");

            // Set the dialog content to be a VBox containing the URL input and filename input
            VBox vbox = new VBox();
            vbox.getChildren().addAll(dialog.getEditor(), filenameField);
            dialog.getDialogPane().setContent(vbox);

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(urlString -> {
                try {
                    // Convert the entered URL string to a URL object
                    URL url = new URL(urlString);

                    // Get the filename from the filenameField
                    String filename = filenameField.getText().trim();

                    // 添加到MiniEmail
                    miniEmail.addAttachment(url, filename);

                    // Perform further actions if needed
                    System.out.println("Attachment added from URL: " + urlString + " with filename: " + filename);
                } catch (MalformedURLException malformedURLException) {
                    // Handle the case where the entered URL is not valid
                    System.err.println("Invalid URL entered");
                }
            });
        });


        searchButton.setOnAction(e -> {
            // 处理查找联系人按钮点击事件

        });

        addressBookButton.setOnAction(e -> {
            // 处理通讯录按钮点击事件

        });
    }

}