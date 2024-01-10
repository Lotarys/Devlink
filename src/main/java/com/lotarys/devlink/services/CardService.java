package com.lotarys.devlink.services;

import com.lotarys.devlink.dtos.CardDTO;
import com.lotarys.devlink.dtos.ResponseCardDTO;
import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.Link;
import com.lotarys.devlink.entities.User;
import com.lotarys.devlink.repositories.CardRepository;
import com.lotarys.devlink.exceptions.NotFoundCardException;
import com.lotarys.devlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final LinkService linkService;
    private final ImageService imageService;
    private final UserRepository userRepository;

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
    public void createCard(CardDTO cardDTO, MultipartFile img, User user) {
        Card card = new Card();
        List<Link> links = cardDTO.getLinks();
        String randomString = generateRandomString();
        while (cardRepository.findByUrl(randomString) == null) {
            randomString = generateRandomString();
        }
        card.setFirstName(cardDTO.getFirstName());
        card.setLastName(cardDTO.getLastName());
        card.setEmail(cardDTO.getEmail());
        imageService.postCardImage(img, randomString);
        card.setUrl(randomString);
        card.setUser(user);
        card.setViews(0L);
        card.setTitle(cardDTO.getTitle());
        cardRepository.save(card);
        linkService.addLinks(links, card);
    }

    public List<ResponseCardDTO> getCardsOfUser(User user) {
        User newUser = userRepository.findUserWithCards(user.getId());
        return newUser.getCards().stream().map(this::mapCardToCardDTO).toList();
    }

    public ResponseCardDTO getResponseCardByUrl(String url) {
        Card card = cardRepository.findByUrl(url).orElseThrow(() ->
                new NotFoundCardException("Card with url " + url + " does not exist"));
        return mapCardToCardDTO(card);
    }

    @Transactional
    public void deleteCard(String url) {
        imageService.deleteCardImage(url);
        cardRepository.deleteByUrl(url);
    }

    @Transactional
    public void updateCard(String url, CardDTO cardDTO, MultipartFile img) {
        imageService.postCardImage(img, url);
        Card card = cardRepository.findByUrl(url).orElseThrow(() -> new NotFoundCardException("Card with url " + url + " does not exist"));
        List<Link> links = cardDTO.getLinks();
        card.setFirstName(cardDTO.getFirstName());
        card.setLastName(cardDTO.getLastName());
        card.setEmail(cardDTO.getEmail());
        card.setTitle(cardDTO.getTitle());
        cardRepository.save(card);
        linkService.addLinks(links, card);
    }
}
