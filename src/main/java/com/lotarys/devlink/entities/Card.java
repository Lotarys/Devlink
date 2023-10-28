package com.lotarys.devlink.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {
    @Id
    private String id;

    private String title;

    private String photo;

    private Long views;


    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Link> links;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
