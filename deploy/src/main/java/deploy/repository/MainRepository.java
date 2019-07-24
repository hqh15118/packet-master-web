package deploy.repository;

import com.tonggong.mvc.AbstractRepository;
import deploy.controller.MainController;

public class MainRepository extends AbstractRepository<MainController> {

    public MainRepository(MainController controller) {
        super(controller);
    }
}
