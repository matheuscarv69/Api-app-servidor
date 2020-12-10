package io.github.matheuscarv69.domain.entity.othersEntity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.matheuscarv69.domain.entity.FormSocial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "form")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 40)
    String beneficio;

    @JsonIgnore
    @ManyToMany(mappedBy = "beneficios")
    private List<FormSocial> forms = new ArrayList<>();

}
