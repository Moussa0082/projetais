package projet.ais.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.services.ProcedureService;

@RestController
@RequestMapping("api-koumi/procedure")
public class ProcedureController {
    
    @Autowired
    private ProcedureService procedureService;


    @GetMapping("/nbActeurParMois")
    public List<Map<String, Object>> getNbActeur() {
        return procedureService.getNbActeurParMois();
    }


    @GetMapping("/getCommande")
    public List<Map<String, Object>> getCommanded() {
        return procedureService.getCommandeParMois();
    }
}
