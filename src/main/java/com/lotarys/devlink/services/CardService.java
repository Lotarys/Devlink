package com.lotarys.devlink.services;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.dtos.ResponseCardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.Link;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.repositories.CardRepository;
import com.lotarys.devlink.exceptions.NotFoundCardException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final LinkService linkService;
    private final ImageService imageService;

    private String generateRandomString() {
            String charLower = "abcdefghijklmnopqrstuvwxyz";
            String charUpper = charLower.toUpperCase();
            String number = "0123456789";
            String dataForRandomString = charLower + charUpper + number;
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                int rndCharAt = (int) (Math.random() * dataForRandomString.length());
                char rndChar = dataForRandomString.charAt(rndCharAt);
                sb.append(rndChar);
            }
            return sb.toString();
    }

    private ResponseCardDTO mapCardToCardDTO(Card card) {
        return new ResponseCardDTO(
                        card.getId(),
                        card.getUrl(),
                        imageService.getCardImage(card),
                        card.getFirstName(),
                        card.getLastName(),
                        card.getEmail(),
                        card.getTitle(),
                        card.getViews(),
                        card.getLinks()
        );
    }

    @Transactional
    public void createCard(CardDTO cardDTO, User user) {
            Card card = new Card();
            List<Link> links = cardDTO.getLinks();
            String randomString = generateRandomString();
            while (cardRepository.findByUrl(randomString) == null) {
                randomString = generateRandomString();
            }
            card.setFirstName(cardDTO.getFirstName());
            card.setLastName(cardDTO.getLastName());
            card.setEmail(cardDTO.getEmail());
            card.setUrl(randomString);
            card.setUser(user);
            card.setViews(0L);
            card.setTitle(cardDTO.getTitle());
            card.setLinks(links);
            cardRepository.save(card);
            linkService.addLinks(links, card);
    }

    public List<Card> getCardsOfUser(User user) {
        return user.getCards();
    }

    public ResponseCardDTO getResponseCardByUrl(String url) {
        Card card = cardRepository.findByUrl(url).orElseThrow(() ->
                new NotFoundCardException("Card with url " + url + " does not exist"));
        return mapCardToCardDTO(card);
    }

    public Card getCardByUrl(String url) {
        return cardRepository.findByUrl(url).orElseThrow(() ->
                new NotFoundCardException("Card with url " + url + " does not exist"));
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
