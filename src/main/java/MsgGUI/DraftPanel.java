package MsgGUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DraftPanel extends BorderPane {
    private TextArea draftTextArea;

    public DraftPanel() {
        setPadding(new Insets(10));

        draftTextArea = new TextArea();

        Button saveButton = new Button("Save Draft");
        saveButton.setOnAction(e -> saveDraft());

        setCenter(draftTextArea);
        setBottom(saveButton);
    }

    private void saveDraft() {
        String content = draftTextArea.getText();
        String fileName = generateFileName();

        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            Path draftFile = draftsDirectory.resolve(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(draftFile.toFile()));
            writer.write(content);
            writer.close();

            // 使用JavaFX的对话框显示保存成功消息
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Save Draft");
            alert.setHeaderText(null);
            alert.setContentText("Draft saved successfully");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // 使用JavaFX的对话框显示保存失败消息
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Save Draft");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save draft");
            alert.showAndWait();
        }
    }

    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return "draft_" + timestamp + ".txt";
    }
}