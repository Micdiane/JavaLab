package MsgGUI;

import Sender.miniemail.config.MailConfig;
import Sender.miniemail.constant.EmailContentTypeEnum;
import Sender.miniemail.constant.SmtpHostEnum;
import Sender.miniemail.core.DefaultMiniEmailFactory;
import Sender.miniemail.core.MiniEmail;
import Sender.miniemail.core.MiniEmailFactory;
import Sender.miniemail.core.MiniEmailFactoryBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class SendPanel extends BorderPane {
    private TextField recipientTextField;
    private CheckBox ccCheckBox;
    private CheckBox bccCheckBox;
    private CheckBox HtmlOptionBox;
    private TextField subjectTextField;
    private TextArea messageTextArea;
    private TextField ccTextField;
    private TextField bccTextField;
    private List<Contact> contactsList = new ArrayList<>();

    private ContactManager contactManager;


    private static final String TO_EMAIL = "1141504337@qq.com";
    private static final String[] TO_EMAILS = new String[]{"1141504337@qq.com"};
    MiniEmailFactory miniEmailFactory;

    public SendPanel(String UserEmail,String UserPassword,SmtpHostEnum MailType) {
        contactManager = new ContactManager();
        UserEmail = "1141504337@qq.com";
        UserPassword="qjejagycjsdbhjfj"; // TODO 记得删掉。
        this.miniEmailFactory = (DefaultMiniEmailFactory) (new MiniEmailFactoryBuilder()).build(MailConfig.config(UserEmail, UserPassword).setMailDebug(Boolean.FALSE).setMailSmtpHost(MailType));
        MiniEmail miniEmail = miniEmailFactory.init();
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

        ccTextField = new TextField();
        ccTextField.setPromptText("输入抄送邮箱地址");
        ccTextField.setVisible(false); // 初始时不可见

        bccTextField = new TextField();
        bccTextField.setPromptText("输入密送邮箱地址");
        bccTextField.setVisible(false); // 初始时不可见

        setTop(topPanel);

        VBox recipientPanel = new VBox(10);
        recipientPanel.setPadding(new Insets(0, 0, 10, 0));

        recipientTextField = new TextField();
        ccCheckBox = new CheckBox("抄送");
        bccCheckBox = new CheckBox("密送");
        HtmlOptionBox = new CheckBox("HTML格式");
        subjectTextField = new TextField();
        recipientPanel.getChildren().addAll(
                new Label("收件人:"), recipientTextField,
                new Label("主题:"), subjectTextField,
                ccCheckBox, bccCheckBox,HtmlOptionBox,
                ccTextField, bccTextField
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

            // 获取基本信息
            String content = messageTextArea.getText();
            String subjectText = subjectTextField.getText();
            String[] toEmails = {recipientTextField.getText()};
            String[] ccEmails = ccCheckBox.isSelected() ? ccTextField.getText().split(" "): new String[]{};
            String[] bccEmails = bccCheckBox.isSelected() ? bccTextField.getText().split(" ") : new String[]{};
            EmailContentTypeEnum Type = HtmlOptionBox.isSelected() ? EmailContentTypeEnum.HTML : EmailContentTypeEnum.TEXT;
            // 处理发送按钮点击事件
            miniEmail.addCarbonCopy(ccEmails);
            miniEmail.addBlindCarbonCopy(bccEmails);
            List<String> sendSuccessToList = miniEmail.send(toEmails, subjectText , Type ,content);

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
            List<Contact> contacts = new ArrayList<>();
            try {
                Path contactsDirectory = Paths.get("EmailAppBuffer", "contacts");
                Files.createDirectories(contactsDirectory);

                // 遍历通讯录文件夹中的所有联系人文件
                Files.walkFileTree(contactsDirectory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(file))) {
                            Contact contact = (Contact) inputStream.readObject();
                            contacts.add(contact);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        searchButton.setOnAction(e -> {
            showAddressBook();
        });
        ccCheckBox.setOnAction(e -> ccTextField.setVisible(ccCheckBox.isSelected()));
        bccCheckBox.setOnAction(e -> bccTextField.setVisible(bccCheckBox.isSelected()));

    }

    private void showAddressBook() {
        // 创建一个对话框来显示通讯录
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("通讯录");
        dialog.setHeaderText("联系人列表");

        // 创建一个列表视图来显示联系人
        ListView<Pair<String, String>> listView = new ListView<>();

        for (Map.Entry<String, String> entry : contactManager.contactsMap.entrySet()) {
            String name = entry.getKey();
            String email = entry.getValue();
            listView.getItems().add(new Pair<>(name, email));
        }

        // 将列表视图添加到对话框中
        dialog.getDialogPane().setContent(listView);

        // 添加添加按钮
        ButtonType addButton = new ButtonType("添加联系人", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(addButton);

        // 添加关闭按钮
        ButtonType closeButton = new ButtonType("确定", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);


        // 设置按钮事件
        dialog.setResultConverter(buttonType -> {
            // 当点击添加按钮时，直接返回新的联系人对象
            if (buttonType == addButton) {
                addContact();
            }
            // 当点击关闭按钮时，保存联系人信息
            if (buttonType == closeButton) {
                Pair<String, String> selectedContact = listView.getSelectionModel().getSelectedItem();
                if (selectedContact != null) {
                    String selectedEmail = selectedContact.getValue();
                    recipientTextField.setText(selectedEmail);
                }
                contactManager.saveContactsToFile();
            }
            return null;
        });

        // 显示对话框
        dialog.showAndWait();
    }

    private void addContact() {
        // 创建一个对话框来输入新的联系人信息
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("添加联系人");
        dialog.setHeaderText("输入联系人信息");

        // 创建文本框来输入联系人的姓名和电子邮件地址
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("姓名");
        TextField emailTextField = new TextField();
        emailTextField.setPromptText("电子邮件地址");

        // 将文本框添加到对话框中
        dialog.getDialogPane().setContent(new VBox(10, nameTextField, emailTextField));

        // 添加确定和取消按钮
        ButtonType addButton = new ButtonType("添加", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // 设置按钮事件
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                String name = nameTextField.getText();
                String email = emailTextField.getText();
                if (!name.isEmpty() && !email.isEmpty()) {
                    // 将新联系人添加到 contactManager 中
                    contactManager.put(name, email);

                    // 更新通讯录界面，重新显示联系人列表
                    showAddressBook();
                }
            }
            return null;
        });

        // 显示对话框并等待用户输入
        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            Pair<String, String> contact = result.get();
            // 在这里可以使用 contact，例如将其添加到联系人列表中
        }
    }



}