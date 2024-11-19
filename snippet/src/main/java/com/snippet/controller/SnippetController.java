package com.snippet.controller;

import com.snippet.model.Snippet;
import com.snippet.service.SnippetService;
import com.snippet.repository.SnippetRepository;
import com.snippet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/snippet")
public class SnippetController {

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    private SnippetService snippetService;

    @Autowired
    private UserRepository userRepository;

    // POST /snippet - Create a new snippet
    @PostMapping
    public ResponseEntity<Snippet> createSnippet(@RequestBody Snippet snippet, @RequestParam Long userId) {
        Snippet createdSnippet = snippetService.createSnippet(snippet, userId);
        return new ResponseEntity<>(createdSnippet, HttpStatus.CREATED);
    }

    // GET /snippet - Get all snippets
    @GetMapping
    public List<Snippet> getAllSnippets(@RequestParam(value = "lang", required = false) String language) {
        List<Snippet> snippets;
        if (language != null) {
            snippets = snippetService.getSnippetByLanguage(language);
        } else {
            snippets = snippetService.getAllSnippets();
        }

        // Decrypt any snippet that is not a bcrypt hash
        for (Snippet snippet : snippets) {
            String code = snippet.getCode();
            // Check if the code is bcrypt-hashed
            if (!snippetService.isBcryptHash(code)) {
                try {
                    // Decrypt the code if it is encrypted
                    String decryptedCode = snippetService.getSnippetById(snippet.getId()).get().getCode();
                    snippet.setCode(decryptedCode);
                } catch (Exception e) {
                    snippet.setCode("Decryption failed");
                }
            }
        }

        return snippets;
    }

    // GET /snippet/{id} - Get a snippet by ID
    @GetMapping("/{id}")
    public ResponseEntity<Snippet> getSnippetById(@PathVariable Long id) {
        Optional<Snippet> snippet = snippetService.getSnippetById(id);
        return snippet.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
