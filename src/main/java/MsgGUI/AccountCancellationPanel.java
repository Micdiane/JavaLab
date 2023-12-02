package MsgGUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountCancellationPanel extends GridPane {
    private TextField emailTextField;
    private PasswordField passwordField;

    public AccountCancellationPanel() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label emailLabel = new Label("Email:");
        emailTextField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Button cancelAccountButton = new Button("Cancel Account");
        cancelAccountButton.setOnAction(e -> {
            String email = emailTextField.getText();
            String password = passwordField.getText();

            if (cancelAccount(email, password)) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Account Cancellation");
                alert.setHeaderText(null);
                alert.setContentText("Account canceled successfully");
                alert.showAndWait();
                clearFields();
            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Account Cancellation");
                alert.setHeaderText(null);
                alert.setContentText("Failed to cancel account");
                alert.showAndWait();
            }
        });

        add(emailLabel, 0, 0);
        add(emailTextField, 1, 0);
        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);
        add(cancelAccountButton, 0, 2, 2, 1);
    }

    private boolean cancelAccount(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 验证账户凭据
        if (!validateCredentials(email, password)) {
            return false;
        }

        // 删除账户的SQL语句
        String deleteQuery = "DELETE FROM users WHERE email=?";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, email);

            // 执行删除操作
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean validateCredentials(String email, String password) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/javaFinal";
        String username = "root";
        String dbPassword = "root";

        // 查询准备语句
        String query = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection connection = DriverManager.getConnection(url, username, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 检查结果集是否有行
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void clearFields() {
        emailTextField.setText("");
        passwordField.setText("");
    }
}