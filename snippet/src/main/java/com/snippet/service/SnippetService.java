package com.snippet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SnippetEncryptionService encryptionService;

    private static final Logger logger = LoggerFactory.getLogger(SnippetService.class);

    // Create a new snippet with encrypted code
    public Snippet createSnippet(Snippet snippet, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            try {
                logger.info("Creating snippet: " + snippet.getLanguage());
                // Encrypt the snippet code before saving
                String encryptedCode = encryptionService.encrypt(snippet.getCode());
                snippet.setCode(encryptedCode);
                snippet.setUser(user.get());
                return snippetRepository.save(snippet);
            } catch (Exception e) {
                throw new RuntimeException("Error encrypting snippet code", e);
            }
        } else {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    // Get a snippet by ID and decrypt the code if it's encrypted
    public Optional<Snippet> getSnippetById(Long id) {
        Optional<Snippet> snippet = snippetRepository.findById(id);
        if (snippet.isPresent()) {
            String code = snippet.get().getCode();
            // Check if the code is bcrypt hashed
            if (!isBcryptHash(code)) {
                try {
                    // Decrypt the snippet code before returning
                    String decryptedCode = encryptionService.decrypt(code);
                    snippet.get().setCode(decryptedCode);
                } catch (Exception e) {
                    throw new RuntimeException("Error decrypting snippet code", e);
                }
            }
        }
        return snippet;
    }

    // Check if the code is bcrypt hash
    public boolean isBcryptHash(String code) {
        return code != null && code.startsWith("$2a$");
    }

    // Get all snippets
    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    // Get snippets by language
    public List<Snippet> getSnippetByLanguage(String language) {
        return snippetRepository.findByLanguage(language);
    }

    // Get snippets by user ID
    public List<Snippet> getSnippetByUserId(Long userId) {
        return snippetRepository.findByUserId(userId);
    }
}
