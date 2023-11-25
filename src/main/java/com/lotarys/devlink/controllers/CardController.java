package com.lotarys.devlink.controllers;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.Link;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    //TODO Решить как будет формироваться ссылка, решить что возвращать при создании карты.
    private final CardService cardService;

  @PostMapping()
  public ResponseEntity<?> createCard(@RequestBody CardDTO card, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(HttpStatus.CREATED);
  }

  @GetMapping("/{url}")
  public ResponseEntity<Card> getCard(@PathVariable String url) {
      return ResponseEntity
              .ok(cardService.getCardByUrl(url));
  }

  @GetMapping()
  public ResponseEntity<List<Card>> getAllCardsOfUser(@AuthenticationPrincipal User user) {
      return ResponseEntity
              .ok(cardService.getCardsOfUser(user));
  }

  @PutMapping("/{url}")
  public ResponseEntity<Card> putLinksToCard(@PathVariable String url, List<Link> links, @AuthenticationPrincipal User user) {
      return ResponseEntity.ok(cardService.addLinkToCard(user, cardService.getCardByUrl(url), links));
  }
}
