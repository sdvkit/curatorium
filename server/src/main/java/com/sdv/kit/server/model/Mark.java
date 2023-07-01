package com.sdv.kit.server.model;

import com.sdv.kit.server.model.enumerated.MarkType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "marks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SecondLvlStatement secondLvlStatement;

    @ManyToOne
    private Student student;

    private Integer value;

    @Enumerated(EnumType.ORDINAL)
    private MarkType markType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(id, mark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
