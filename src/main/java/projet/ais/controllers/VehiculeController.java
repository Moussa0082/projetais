package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.services.VehiculeService;

@RestController
@RequestMapping("/vehicule")
public class VehiculeController {


    @Autowired
    private VehiculeService vehiculeService;

    
    
}
