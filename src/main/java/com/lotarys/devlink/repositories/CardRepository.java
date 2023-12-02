package com.lotarys.devlink.repositories;

import com.lotarys.devlink.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    void deleteByUrl(String url);
    Optional<Card> findByUrl(String url);
}
