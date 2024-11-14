package com.chat.services;

import com.chat.elasticsearch.repositories.MessageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SearchService {

    @Autowired
    MessageSearchRepository messageSearchRepository;

}
