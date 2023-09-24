package com.example.productservice.category;

import com.example.productservice.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Table(name = "category")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "category_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String categoryName;

    @JsonIgnore
    @OneToMany(
            mappedBy = "category",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Product> products = new ArrayList<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
