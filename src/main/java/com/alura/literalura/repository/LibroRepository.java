package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    List<Libro> findByAuthorsContaining(Autor autor);

    @Query("SELECT DISTINCT l FROM Libro l JOIN FETCH l.languages WHERE :idioma MEMBER OF l.languages")
    List<Libro> findByLanguagesContains(@Param("idioma") String idioma);



}
