package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer>{
    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :anio AND (a.deathYear IS NULL OR a.deathYear >= :anio)")
    List<Autor> findByVivosEnAnio(@Param("anio") Integer anio);

}
