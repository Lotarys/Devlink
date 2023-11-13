package com.lotarys.devlink.services;


import com.lotarys.devlink.entities.Card;
import com.lotarys.devlink.entities.Link;
import com.lotarys.devlink.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public void addLinks(List<Link> links, Card card) {
        for (Link link : links) {
            link.setCard(card);
            linkRepository.save(link);
        }
    }
}
