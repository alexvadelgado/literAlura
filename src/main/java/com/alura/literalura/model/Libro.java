package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name="libro")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="libro_id")
    private Integer id;

    @Column(name = "titulo")
    @JsonAlias("title")
    private String title;
    @JsonAlias("download_count")
    private Integer downloadCount;

    @JsonAlias("languages")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "languages", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "lenguaje")
    private List<String> languages;

    @JsonAlias("authors")
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Autor.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "autores_libro", joinColumns = @JoinColumn(name = "libro_id"), inverseJoinColumns = @JoinColumn(name="autor_id"))

    private List<Autor> authors;

}
