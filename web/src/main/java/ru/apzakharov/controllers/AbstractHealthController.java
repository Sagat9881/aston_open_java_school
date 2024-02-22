package ru.apzakharov.controllers;

import ru.apzakharov.healing.Healing;
import ru.apzakharov.service.HealthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractHealthController<T extends Healing> implements RestController {
    private final HealthService<T> healthService;

    public AbstractHealthController(HealthService<T> healthService) {
        this.healthService = healthService;
    }

    protected abstract T buildHealingEntity(HttpServletRequest req);


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            String heal = healthService.getSomeHeal().getHeal();
            writer.write(heal);
        }
    }


    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            final T t = buildHealingEntity(req);
            healthService.putHeal(t);
            writer.write("Success");
        }
    }
}
