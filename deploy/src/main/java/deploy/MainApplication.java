package deploy;

import com.tonggong.BaseApplicaton;
import com.tonggong.conponments.dialogs.util.SimpleAlertUtil;
import com.tonggong.other.WindowCloseEvent;
import deploy.controller.LoginController;
import deploy.util.DBUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static deploy.App.THRME_STYLE;

public class MainApplication extends BaseApplicaton {

    @Override
    public void start() {
        //初始化
        new Thread(() -> {
            try {
                DBUtil.init();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        SimpleAlertUtil.initStage(stage);
        //JFXSpinner jfxSpinner = new JFXSpinner();
        //jfxSpinner.setMaxSize(400,300);
        //getRoot().getChildren().add(jfxSpinner);
        Task<Pane> task = new Task<Pane>() {
            @Override
            protected Pane call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                LoginController mainController = new LoginController();
                mainController.setObj(stage);
                loader.setController(mainController);
                return loader.load();
            }
        };
        task.setOnSucceeded(event -> {
            Platform.runLater(()->{
                //initDB();
                //getRoot().getChildren().remove(jfxSpinner);
                try {
                    getRoot().getChildren().add(task.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        });
        new Thread(task).start();
        stage.setOnCloseRequest(new WindowCloseEvent(stage));
        scene.getStylesheets().add(THRME_STYLE);
        //stage.getIcons().add(App.getIcon());
    }

    @Override
    public String stageName() {
        return "登录";
    }

    @Override
    public double[] initStageSize() {
        return new double[]{800,600};
    }

    @Override
    public boolean isInDebug() {
        return false;
    }
}
