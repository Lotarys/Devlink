package com.lotarys.devlink.services;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.Link;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.repositories.CardRepository;
import com.lotarys.devlink.exceptions.CardAlreadyExistException;
import com.lotarys.devlink.exceptions.NotFoundCardException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final LinkService linkService;

    @Transactional
    public Card createCard(CardDTO cardDTO, User user) {
        if(cardRepository.findByUrl(cardDTO.getUrl()).isEmpty()) {
            Card card = new Card();
            card.setUrl(cardDTO.getUrl());
            card.setUser(user);
            card.setViews(0L);
            card.setTitle(cardDTO.getTitle());
            card.setLinks(cardDTO.getLinks());
            card.setPhoto(user.getPhoto());
            cardRepository.save(card);
            linkService.addLinks(cardDTO.getLinks(), card);
            return card;
        } else {
            throw new CardAlreadyExistException("Card with url " + cardDTO.getUrl() + " already exist");
        }
    }

    public List<Card> getCardsOfUser(User user) {
        return user.getCards();
    }

    public Card getCardByUrl(String url) {
        return cardRepository.findByUrl(url).orElseThrow(() -> new NotFoundCardException("Card with url " + url + "does not exist"));
    }

    @Transactional
    public Card addLinkToCard(User user, Card card, List<Link> links) {
        if(card.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You don't have permissions to do this");
        }
        if (links == null) {
            throw new IllegalArgumentException("the links can't be empty");
        }
        List<Link> existingLinks = card.getLinks();
        if (existingLinks != null) {
            links.addAll(existingLinks);
        }
        card.setLinks(links);
        cardRepository.save(card);
        return card;
    }
}
