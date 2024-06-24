package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@Table(name="autor")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private Integer id; // Campo ID para la entidad Autor

    @JsonAlias("name")
    @Column(name = "nombre")
    private String name;

    @JsonAlias("birth_year")
    @Column(name = "fecha_nacimiento")
    private Integer birthYear;

    @JsonAlias("death_year")
    @Column(name = "fecha_muerte")
    private Integer deathYear;
}
