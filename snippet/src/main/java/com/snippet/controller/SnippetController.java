package com.snippet.controller;

import com.snippet.model.Snippet;
import com.snippet.model.User;
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
@RequestMapping("/snippets")
public class SnippetController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Snippet> createSnippet(@RequestBody Snippet snippet) {
        try {
            if (snippet.getUser() == null || snippet.getUser().getId() == null) {
                return ResponseEntity.badRequest().body(null); // User ID must be provided
            }

            // Fetch the User object using the user ID
            Optional<User> user = userRepository.findById(snippet.getUser().getId());
            if (!user.isPresent()) {
                return ResponseEntity.badRequest().body(null); // User not found
            }

            snippet.setUser(user.get());

            Snippet createdSnippet = snippetService.createSnippet(snippet, snippet.getUser().getId());

            return new ResponseEntity<>(createdSnippet, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public List<Snippet> getAllSnippets(@RequestParam(value = "lang", required = false) String language) {
        if (language != null) {
            return snippetService.getSnippetByLanguage(language);
        } else {
            return snippetService.getAllSnippets();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Snippet> getSnippetById(@PathVariable Long id) {
        Optional<Snippet> snippet = snippetService.getSnippetById(id);
        return snippet.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
