package MsgGUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class EmailPanel extends BorderPane {
    private TextField recipientTextField;
    private CheckBox ccCheckBox;
    private CheckBox bccCheckBox;
    private TextField subjectTextField;
    private TextArea messageTextArea;

    public EmailPanel() {
        setPadding(new Insets(10));

        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(0, 0, 10, 0));

        Label sendLabel = new Label("新邮件");
        sendLabel.setStyle("-fx-graphic: url('./icons/Envelope.png');");
        Button sendButton = new Button("发送");
        Button saveDraftButton = new Button("存草稿");
        Button closeButton = new Button("关闭");
        FlowPane buttonPanel = new FlowPane();
        buttonPanel.getChildren().addAll(sendButton, saveDraftButton, closeButton);
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
        Button photoButton = new Button("添加照片");
        Button htmlButton = new Button("添加HTML文档");
        attachmentPanel.getChildren().addAll(attachButton, photoButton, htmlButton);

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
        });

        saveDraftButton.setOnAction(e -> {
            // 处理存草稿按钮点击事件
        });

        closeButton.setOnAction(e -> {
            // 处理关闭按钮点击事件
        });

        attachButton.setOnAction(e -> {
            // 处理添加附件按钮点击事件
        });

        photoButton.setOnAction(e -> {
            // 处理添加照片按钮点击事件
        });

        htmlButton.setOnAction(e -> {
            // 处理添加HTML文档按钮点击事件
        });

        searchButton.setOnAction(e -> {
            // 处理查找联系人按钮点击事件
        });

        addressBookButton.setOnAction(e -> {
            // 处理通讯录按钮点击事件
        });
    }

}