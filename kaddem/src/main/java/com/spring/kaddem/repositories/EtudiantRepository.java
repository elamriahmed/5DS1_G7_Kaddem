package com.spring.kaddem.repositories;


import com.spring.kaddem.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {


    Etudiant findByNomEAndPrenomE(String nomE, String prenomE);
}
