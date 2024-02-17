package ru.apzakharov.controllers;

import ru.apzakharov.healing.PsychologicalHealing;
import ru.apzakharov.service.PsychologicalService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/psychological")
public class PsychologicalHeathController extends AbstractHealthController<PsychologicalHealing>{

       public PsychologicalHeathController(PsychologicalService healthService) {
              super(healthService);
       }

       @Override
       protected PsychologicalHealing buildHealingEntity(HttpServletRequest req) {
              String phrase = req.getParameter("phrase");
              return new PsychologicalHealing("phrase");
       }
}
