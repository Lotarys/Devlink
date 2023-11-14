package com.lotarys.devlink.controllers;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

  @PostMapping()
    public ResponseEntity<?> createCard(@RequestBody CardDTO card, @AuthenticationPrincipal User user) {
        cardService.createCard(card,user);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }
}
