package deploy.controller;

import com.tonggong.mvc.AbstractController;
import deploy.repository.LoginRepository;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LoginController extends AbstractController<LoginRepository> {

    @FXML
    private Button btn_login;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;

    @Override
    public void init() {
        btn_login.setOnMouseClicked(event -> {
            String userName = user.getText();
            if (userName == null || userName.trim().equals("")){

            }
            String pass = password.getText();
            if (pass == null || pass.trim().equals("")){

            }
            try {
                if (repository.login(userName,pass)){

                }
                goMainPane();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void goMainPane() {
        MainController mainController = new MainController();
        final Stage newStage = new Stage();
        mainController.setObj(newStage);
        final StackPane root = new StackPane();
        root.setPrefSize(800,600);
        final ProgressIndicator mainProgress = new ProgressIndicator();
        mainProgress.setMaxSize(200,200);
        root.getChildren().add(mainProgress);
        Task<BorderPane> task = new Task<BorderPane>() {
            @Override
            protected BorderPane call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                loader.setController(mainController);
                return loader.load();
            }
        };
        task.setOnSucceeded(event -> {
            Platform.runLater(()->{
                try {
                    //load successfully and remove loading view
                    root.getChildren().remove(mainProgress);
                    root.getChildren().add(task.get());
                    ((Stage) getObj()).close();
                    newStage.show();
                } catch (InterruptedException | ExecutionException e ) {
                    e.printStackTrace();
                }
            });
        });
        new Thread(task).start();
        newStage.setScene(new Scene(root));
    }

    @Override
    public LoginRepository initRepository() {
        return new LoginRepository(this);
    }

    @Override
    public boolean initEventBus() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void initInBackground() {

    }
}
