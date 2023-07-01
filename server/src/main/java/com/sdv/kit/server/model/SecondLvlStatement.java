package com.sdv.kit.server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "second_lvl_statements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SecondLvlStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private FirstLvlStatement firstLvlStatement;

    @Builder.Default
    @OneToMany(mappedBy = "secondLvlStatement", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Mark> marks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecondLvlStatement that = (SecondLvlStatement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
