package com.spring.kaddem.repositories;


import com.spring.kaddem.entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EquipeRepository  extends JpaRepository<Equipe, Integer> {
    

}
