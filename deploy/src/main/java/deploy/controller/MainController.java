package deploy.controller;

import com.tonggong.examples.controlfx.items.textflow.CustomCodePane;
import com.tonggong.mvc.AbstractController;
import deploy.repository.MainRepository;
import deploy.util.ConfigUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainController extends AbstractController<MainRepository> {

    @FXML
    private CustomCodePane code_pane;
    @FXML
    private StackPane main_menu;
    @FXML
    private TabPane tp_menu;
    @FXML
    private ListView lv_all_file;
    @FXML
    private StatusBar status_bar;
    @FXML
    private Button btn_choose_main_dir;

    @Override
    public void init() {
        tp_menu.getTabs().addAll(getNewTab("本地部署"),getNewTab("远程部署"));
        try {
            Map map = ConfigUtil.getData();
            Object path = map.get("local_all_file");
            if (path == null){
                btn_choose_main_dir.setVisible(true);
                lv_all_file.setVisible(false);
                btn_choose_main_dir.setOnMouseClicked(event -> {
                    try {
                        setPath(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }else{
                btn_choose_main_dir.setVisible(false);
                lv_all_file.setVisible(true);
                initAllFileList((String) path,0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPath(int type) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(((Stage) getObj()));
        if (file!=null) {
            initAllFileList(file.getAbsolutePath(),type);
        }
    }

    @SuppressWarnings("unchecked")
    private void initAllFileList(String path,int type) throws IOException {
        if (type != 0){
            lv_all_file.getItems().clear();
        }
        File[] files = new File(path).listFiles();
        assert files!=null;
        for (File file : files) {
            Label label = new Label(file.getName());
            lv_all_file.getItems().add(label);
        }
        lv_all_file.setVisible(true);
        btn_choose_main_dir.setVisible(false);
        ConfigUtil.setData("local_all_file",path);
        Button btnReChoose = new Button("重新选择");
        btnReChoose.setOnMouseClicked(event -> {
            try {
                setPath(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        lv_all_file.getItems().add(0,btnReChoose);
    }

    private Tab getNewTab(String text){
        Tab tab = new Tab(text);
        tab.setClosable(false);
        return tab;
    }

    @Override
    public MainRepository initRepository() {
        return new MainRepository(this);
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
