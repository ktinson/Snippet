package com.snippet.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snippet.model.Snippet;
import com.snippet.model.User;
import com.snippet.repository.SnippetRepository;
import com.snippet.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
      @Autowired
    private SnippetRepository snippetRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SnippetService.class);

    // Create a new snippet
    public Snippet createSnippet(Snippet snippet, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
        logger.info("creating snippet: ", snippet.getLanguage());
        return snippetRepository.save(snippet);
        }else{
            throw new IllegalArgumentException("User not found: "+ userId);
        }
    }

    // Get all snippets
    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    // Find snippet by ID
    public Optional<Snippet> getSnippetById(Long id) {
        return snippetRepository.findById(id);
    }
    // Find snippet by language
    public List<Snippet> getSnippetBylanguage(String language) {
        return snippetRepository.findByLanguage(language);
    }
    // Find snippet by language
    public List<Snippet> getSnippetByUserId(Long userId) {
        return snippetRepository.findByUserId(userId);
    }
}
