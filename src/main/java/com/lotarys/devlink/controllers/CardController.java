package com.lotarys.devlink.controllers;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.dtos.ResponseCardDTO;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

  @PostMapping("/create")
  public ResponseEntity<?> createCard(@RequestPart CardDTO card,
                                      @RequestPart MultipartFile img,
                                      @AuthenticationPrincipal User user) {
      cardService.createCard(card, img, user);
      return ResponseEntity.ok().body(HttpStatus.CREATED);
  }

  @GetMapping("/get/{url}")
  public ResponseEntity<ResponseCardDTO> getCard(@PathVariable String url) {
      return ResponseEntity
              .ok(cardService.getResponseCardByUrl(url));
  }

  @PutMapping("/update/{url}")
  public ResponseEntity<HttpStatus> updateCard(@PathVariable String url,
                                      @RequestPart CardDTO card,
                                      @RequestPart MultipartFile img) {
        cardService.updateCard(url, card, img);
        return ResponseEntity.ok(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{url}")
  public ResponseEntity<HttpStatus> deleteCard(@PathVariable String url) {
      cardService.deleteCard(url);
      return ResponseEntity.ok(HttpStatus.OK);
  }

  @GetMapping()
  public ResponseEntity<List<ResponseCardDTO>> getAllCardsOfUser(@AuthenticationPrincipal User user) {
      return ResponseEntity
              .ok(cardService.getCardsOfUser(user));
  }

}
