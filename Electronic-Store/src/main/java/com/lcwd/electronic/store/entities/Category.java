package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "category_title",length = 50,nullable = false)
    private String title;
    @Column(name = "category_description",length = 50)
    private String description;
    private String coverImage;

    @OneToMany( cascade = CascadeType.ALL,fetch = FetchType.LAZY ,mappedBy = "category")
    List<Product> products=new ArrayList<>();
}
