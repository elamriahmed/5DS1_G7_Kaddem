package com.spring.kaddem.repositories;


import com.spring.kaddem.entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

    @Query("SELECT count(c) FROM Contrat c where ((c.archived=true) and  ((c.dateDebutContrat BETWEEN :startDate AND :endDate)) or(c.dateFinContrat BETWEEN :startDate AND :endDate))")
    public Integer getnbContratsValides(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

   // List<Contrat> findByEtudiantIdEtudiant(Integer idEtudiant);



}
