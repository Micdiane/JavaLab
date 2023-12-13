package MsgGUI;

import Sender.miniemail.config.MailConfig;
import Sender.miniemail.constant.EmailContentTypeEnum;
import Sender.miniemail.constant.SmtpHostEnum;
import Sender.miniemail.core.DefaultMiniEmailFactory;
import Sender.miniemail.core.MiniEmail;
import Sender.miniemail.core.MiniEmailFactoryBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DraftPanel extends BorderPane {
    private final DefaultMiniEmailFactory miniEmailFactory;

    public interface OnEmailSentListener {
        void onEmailSent(EmailEntity draft);
    }
    private OnEmailSentListener emailSentListener;

    public void setEmailSentListener(OnEmailSentListener listener) {
        this.emailSentListener = listener;
    }
    private TextArea draftTextArea;
    private ListView<EmailEntity> draftListView;
    private TextField recipientTextField;
    private TextField subjectTextField;
    private EmailEntity curEmailEntity;



    public DraftPanel(String UserEmail,String UserPassword,SmtpHostEnum Type) {
        UserEmail = "1141504337@qq.com";
        UserPassword="qjejagycjsdbhjfj"; // TODO 记得删掉。
        this.miniEmailFactory = (DefaultMiniEmailFactory) (new MiniEmailFactoryBuilder()).build(MailConfig.config(UserEmail, UserPassword).setMailDebug(Boolean.FALSE).setMailSmtpHost(Type));



        setPadding(new Insets(10));

        draftTextArea = new TextArea();
        draftListView = new ListView<>();
        draftListView.setPrefWidth(200); // Set the preferred width of the draft list view

        VBox leftVBox = new VBox(10);
        leftVBox.getChildren().addAll(draftListView);

        // Create recipient and subject input fields on separate lines
        recipientTextField = new TextField();
        subjectTextField = new TextField();
        VBox inputFields = new VBox(10);
        inputFields.getChildren().addAll(
                new HBox(10, new Label("收件人:"), recipientTextField),
                new HBox(10, new Label("主题:"), subjectTextField)
        );

        leftVBox.getChildren().addAll(inputFields);
        setLeft(leftVBox);

        Button saveButton = new Button("保存草稿");
        saveButton.setOnAction(e -> saveDraft());

        Button newDraftButton = new Button("新建草稿");
        newDraftButton.setOnAction(e -> createNewDraft());

        Button deleteDraftButton = new Button("删除草稿文件");
        deleteDraftButton.setOnAction(e -> deleteDraft());

        Button sendButton = new Button("快速发送");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        sendButton.setOnAction(e ->{
            try {
                MiniEmail miniEmail = miniEmailFactory.init();
                miniEmail.send(new String[]{recipientTextField.getText()},subjectTextField.getText(),
                                EmailContentTypeEnum.TEXT, draftTextArea.getText());
            }catch (Exception exception){
                alert.setHeaderText("Error");
                alert.setContentText("邮件发送失败");
            }
            alert.setHeaderText(null);
            alert.setContentText("邮件成功发送给: " + String.join(", ", recipientTextField.getText()));
        } );

        HBox buttonBar = new HBox(10);
        buttonBar.getChildren().addAll(saveButton, newDraftButton, deleteDraftButton,sendButton);

        setCenter(draftTextArea);
        setBottom(buttonBar);

        // Load drafts when the application starts
        loadDrafts();

        // Add listener to load selected draft when a draft is selected in the list view
        draftListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDraftContent(newValue);
                curEmailEntity = newValue;
            }
        });
    }

    private void saveDraft() {
        curEmailEntity.setContent(draftTextArea.getText());
        curEmailEntity.setRecipient(recipientTextField.getText());
        curEmailEntity.setSubject(subjectTextField.getText());
        try {
            Path draftFile = Paths.get("EmailAppBuffer", "drafts", curEmailEntity.getId() + ".dat");
            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(draftFile))) {
                outputStream.writeObject(curEmailEntity);
            }

            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("草稿保存成功");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String generateUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    private void loadDrafts() {
        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            // Load all draft files
            List<EmailEntity> draftEntities = new ArrayList<>();
            Files.walkFileTree(draftsDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(file))) {
                        EmailEntity draftEntity = (EmailEntity) inputStream.readObject();
                        draftEntities.add(draftEntity);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            // Display draft entities in the list view
            ObservableList<EmailEntity> items = FXCollections.observableArrayList(draftEntities);
            draftListView.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadDraftContent(EmailEntity draftEntity) {
        draftTextArea.setText(draftEntity.getContent());
        recipientTextField.setText(draftEntity.getRecipient());
        subjectTextField.setText(draftEntity.getSubject());
    }

    private void createNewDraft() {
        recipientTextField.clear();
        subjectTextField.clear();
        draftTextArea.clear();
        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            // Get the file name using time stamp
            String fileName = System.currentTimeMillis() + ".dat";

            // Save the draft entity
            String uniqueId = generateUniqueId(); // 实现一个生成唯一标识符的方法
            EmailEntity draftEntity = new EmailEntity(uniqueId, recipientTextField.getText(), subjectTextField.getText(), draftTextArea.getText());

            Path draftFile = Paths.get("EmailAppBuffer", "drafts", fileName);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(draftFile))) {
                outputStream.writeObject(draftEntity);
            }

            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("草稿新建成功");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // After saving, reload drafts
        loadDrafts();
        // Clear the selection in the draft list view (if any)
        draftListView.getSelectionModel().clearSelection();
    }


    private void deleteDraft() {
        // Delete the draft file
        try {
            Path draftFile = Paths.get("EmailAppBuffer", "drafts", curEmailEntity.getId() + ".dat");
            Files.deleteIfExists(draftFile);

            // Display a confirmation dialog
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("草稿删除成功");
            alert2.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // After deleting, reload drafts
        loadDrafts();
        // Clear the selection in the draft list view (if any)
        draftListView.getSelectionModel().clearSelection();
    }

}
