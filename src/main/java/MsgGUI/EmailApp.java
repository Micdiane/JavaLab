package MsgGUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmailApp extends Application {

    private TabPane tabPane;

    private LoginPanel loginpage = new LoginPanel();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Email App");

        // Create a VBox for the navigation buttons
        VBox navigationPanel = new VBox(10);
        navigationPanel.setPadding(new Insets(10));

        // Create buttons for each feature
        Button mainButton = new Button("主界面");
        Button loginButton = new Button("登录");
        Button registerButton = new Button("注册");
        Button accountCancellationButton = new Button("注销");
        Button draftButton = new Button("写草稿");
        Button sendButton = new Button("发送");

        // Add event handlers to buttons
        mainButton.setOnAction(e -> navigateTo("主界面"));
        loginButton.setOnAction(e -> navigateTo("登录"));
        registerButton.setOnAction(e -> navigateTo("注册"));
        accountCancellationButton.setOnAction(e -> navigateTo("注销"));
        draftButton.setOnAction(e -> navigateTo("写草稿"));
        sendButton.setOnAction(e -> navigateTo("发送"));

        // Add buttons to the VBox
        navigationPanel.getChildren().addAll(mainButton, loginButton, registerButton, accountCancellationButton, draftButton, sendButton);

        // Create a TabPane for the main interface and other features
        tabPane = new TabPane();

        // Create a GridPane for layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

        // Add navigation pane and tab pane to the GridPane
        grid.add(navigationPanel, 0, 0);
        grid.add(tabPane, 1, 0);

        // Set the initial scene with the welcome page
        showWelcomePage();

        // Create scene and show the window
        Scene scene = new Scene(grid, 1080, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void navigateTo(String feature) {
        // Implement your navigation logic here
        System.out.println("Navigating to: " + feature);

        // Check the selected feature and generate the corresponding tab
        Tab newTab = null;

        switch (feature) {
            case "主界面":
                showWelcomePage();
                break;

            case "登录":
                // Show the login panel if not logged in
                if (loginpage.getCurrentUser() == null) {
                    newTab = new Tab("登录");
                    newTab.setContent(loginpage); // Use the existing instance of LoginPanel
                }
                break;

            case "注册":
                newTab = new Tab("注册");
                newTab.setContent(new RegistrationPanel()); // Replace with the content for the registration feature
                break;

            case "注销":
                showWelcomePage();
                break;

            case "切换账号":
//                showWelcomePage();
                break;

            case "写草稿":
                // Show the draft panel if logged in
                if (loginpage.getCurrentUser() != null) {
                    newTab = new Tab("写草稿");
                    newTab.setContent(new DraftPanel()); // Replace with the content for the draft feature
                }else{ //弹窗 显示未登录
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请先登录");
                    alert.showAndWait();

                }
                break;

            case "发送":
                // Show the email panel if logged in
                if (loginpage.getCurrentUser() != null) {
                    newTab = new Tab("发送");
                    newTab.setContent(new EmailPanel()); // Replace with the content for the send feature
                }else{ //弹窗 显示未登录
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请先登录");
                    alert.showAndWait();

                }
                break;
        }

        // Add the new tab to the TabPane
        if (newTab != null) {
            tabPane.getTabs().add(newTab);
        }
    }

    private void showWelcomePage() {
        // Display a welcome message or any other content for non-logged-in users
        tabPane.getTabs().clear(); // Clear existing tabs
        Tab welcomeTab = new Tab("欢迎页");
        StringBuffer WecomeString = new StringBuffer();


        // 根据当前用户信息 打印欢迎信息
        if(loginpage.currentUser == null)
        {
            WecomeString.append("欢迎使用邮件系统，请先登录");
        }
        else {
            WecomeString.append("尊敬的：");
            WecomeString.append(loginpage.currentUser.getEmail());
            WecomeString.append("用户，欢迎使用邮件系统");
        }
        Label welcomeLabel = new Label(WecomeString.toString());
        welcomeTab.setContent(welcomeLabel);
        tabPane.getTabs().add(welcomeTab);
    }

    // Define a simple User class to represent user information

}
