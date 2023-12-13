package MsgGUI;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactManager {
    public Map<String, String> contactsMap = new HashMap<>();
    private final String filePath = "D:/lessons/JAVA/lab/Final/mini-email-main/EmailAppBuffer/contacts/contacts.txt";
    public ContactManager() {
        try{
            loadContactsFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 保存联系人Map到文件
    public void saveContactsToFile() {
        try {
            Path contactsFilePath = Paths.get(filePath);

            // 创建文件目录
            Files.createDirectories(contactsFilePath.getParent());

            // 将联系人信息写入文件
            try (BufferedWriter writer = Files.newBufferedWriter(contactsFilePath)) {
                for (Map.Entry<String, String> entry : contactsMap.entrySet()) {
                    String name = entry.getKey();
                    String email = entry.getValue();
                    writer.write(name + ":" + email);
                    writer.newLine();
                }
            }

            System.out.println("联系人信息已保存到文件: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 从文件加载联系人Map
    public void loadContactsFromFile() {
        try {
            Path contactsFilePath = Paths.get(filePath);

            // 检查文件是否存在
            if (Files.exists(contactsFilePath)) {
                // 读取联系人信息文件
                List<String> lines = Files.readAllLines(contactsFilePath);

                // 清空现有的联系人信息
                contactsMap.clear();

                // 解析文件中的每一行，并添加到联系人信息中
                for (String line : lines) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String name = parts[0];
                        String email = parts[1];
                        contactsMap.put(name, email);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void put(String name, String email) {
        contactsMap.put(name,email);
    }
}
