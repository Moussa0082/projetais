package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Conseil;

public interface ConseilRepository  extends JpaRepository<Conseil , String>{
    
    Conseil findByIdConseil(String idConseil);

    List<Conseil> findAllByActeurIdActeur(String idConseil);

}
