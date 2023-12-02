package MsgGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DraftPanel extends JPanel {
    private JTextArea draftTextArea;

    public DraftPanel() {
        setLayout(new BorderLayout());

        draftTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(draftTextArea);

        JButton saveButton = new JButton("Save Draft");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDraft();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveDraft() {
        String content = draftTextArea.getText();
        String fileName = generateFileName();

        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            File draftFile = new File(draftsDirectory.toString(), fileName);
            FileWriter writer = new FileWriter(draftFile);
            writer.write(content);
            writer.close();

            JOptionPane.showMessageDialog(this, "Draft saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save draft");
        }
    }

    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return "draft_" + timestamp + ".txt";
    }
}