package projet.ais.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProcedureService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> getNbActeurParMois(){
        String query = "CALL NbActeurParMois()";
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getCommandeParMois(){
        String query = "CALL nombreCommandesParMois()";
        return jdbcTemplate.queryForList(query);
    }


}
