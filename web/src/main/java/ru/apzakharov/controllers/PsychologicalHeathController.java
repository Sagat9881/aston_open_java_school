package ru.apzakharov.controllers;


import ru.apzakharov.ServiceLocator;
import ru.apzakharov.ServiceLocatorException;
import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.service.PsychologicalService;

import javax.servlet.http.HttpServletRequest;

//@WebServlet(urlPatterns = "/psychological/*", name = "psychological",displayName = "psychological")
public class PsychologicalHeathController extends AbstractHealthController<PsychologicalHealing> {
    public PsychologicalHeathController() {
        super(findPsychologicalService());
    }

    private static PsychologicalService findPsychologicalService() {
        PsychologicalService service = ServiceLocator.getForClass(PsychologicalService.class);
        if (service == null) {
            throw ServiceLocatorException.serviceByClassNotFound(PsychologicalService.class);
        }
        return service;
    }

    @Override
    protected PsychologicalHealing buildHealingEntity(HttpServletRequest req) {
        String phrase = req.getParameter("phrase");
        return new PsychologicalHealing(phrase);
    }
}
