package com.lotarys.devlink.repositories;

import com.lotarys.devlink.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
}
