package com.snippet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.snippet.model.Snippet;
import com.snippet.model.User;
import com.snippet.repository.SnippetRepository;
import com.snippet.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SnippetService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // Password encoder for code encryption

    // Create a new snippet
    public Snippet createSnippet(Snippet snippet, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            logger.info("Creating snippet: ", snippet.getLanguage());
            // Encrypt the snippet code before saving
            String encryptedCode = encoder.encode(snippet.getCode());
            snippet.setCode(encryptedCode);
            return snippetRepository.save(snippet);
        } else {
            throw new IllegalArgumentException("User not found: " + userId);
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
    public List<Snippet> getSnippetByLanguage(String language) {
        return snippetRepository.findByLanguage(language);
    }

    // Find snippet by user ID
    public List<Snippet> getSnippetByUserId(Long userId) {
        return snippetRepository.findByUserId(userId);
    }
}
