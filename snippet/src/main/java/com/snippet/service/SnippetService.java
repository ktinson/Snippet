package com.snippet.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snippet.model.Snippet;
import com.snippet.model.User;
import com.snippet.repository.SnippetRepository;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {
      @Autowired
    private SnippetRepository snippetRepository;
    private static final Logger logger = LoggerFactory.getLogger(SnippetService.class);

    // Create a new snippet
    public Snippet createSnippet(Snippet snippet) {
        logger.info("creating snippet: ", snippet.getLanguage());
        return snippetRepository.save(snippet);
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
}
