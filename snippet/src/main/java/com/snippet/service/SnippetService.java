package com.snippet.service;

import com.snippet.model.Snippet;
import com.snippet.model.User;
import com.snippet.repository.SnippetRepository;
import com.snippet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnippetEncryptionService encryptionService;

    public Snippet createSnippet(Snippet snippet, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        snippet.setUser(user);

        return snippetRepository.save(snippet);
    }

    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    public Optional<Snippet> getSnippetById(Long id) {
        return snippetRepository.findById(id);
    }

    public List<Snippet> getSnippetByLanguage(String language) {
        return snippetRepository.findByLanguage(language);
    }
}
