package com.example.productservice.product;

import com.example.productservice.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Product")
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "product_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "sku",
            nullable = false
    )
    private String sku;

    @Column(
            name = "name",
            nullable = false,
            length = 40
    )
    private String name;

    @Column(
            name = "price",
            nullable = false
    )
    private BigDecimal price;

    @Column(
            name = "image_url",
            nullable = true
    )
    private String imageUrl;

    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(
            name = "units_in_stock",
            nullable = false
    )
    private int unitsInStock;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime cratedAt;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "category_product_fk"
            )
    )
    private Category category;


}