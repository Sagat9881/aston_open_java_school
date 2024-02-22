package servlet;

import ru.apzakharov.ServiceLocator;
import ru.apzakharov.controllers.RestController;

import javax.servlet.http.HttpServlet;
import java.util.List;

public class DispatcherServlet  extends HttpServlet {

    private final List<RestController> controllers;

    public DispatcherServlet(List<RestController> controllers) {
        this.controllers =  controllers;
    }
}
