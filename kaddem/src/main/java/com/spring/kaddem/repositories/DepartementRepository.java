package com.spring.kaddem.repositories;


import com.spring.kaddem.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {
}
