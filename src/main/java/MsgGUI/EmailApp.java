package MsgGUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class EmailApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Email App");

        // 创建选项卡面板
        TabPane tabPane = new TabPane();

        // 创建登录选项卡
        Tab loginTab = new Tab("登录");
        loginTab.setContent(new LoginPanel());
        tabPane.getTabs().add(loginTab);

        // 创建注册选项卡
        Tab registerTab = new Tab("注册");
        registerTab.setContent(new RegistrationPanel());
        tabPane.getTabs().add(registerTab);

        // 创建注销选项卡
        Tab accountCancellationTab = new Tab("注销");
        accountCancellationTab.setContent(new AccountCancellationPanel());
        tabPane.getTabs().add(accountCancellationTab);

        // 创建草稿选项卡
        Tab draftTab = new Tab("写草稿");
        draftTab.setContent(new DraftPanel());
        tabPane.getTabs().add(draftTab);

        // 创建发送选项卡
        Tab sendTab = new Tab("发送");
        sendTab.setContent(new EmailPanel());
        tabPane.getTabs().add(sendTab);

        // 创建场景并显示窗口
        Scene scene = new Scene(tabPane, 1080, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}