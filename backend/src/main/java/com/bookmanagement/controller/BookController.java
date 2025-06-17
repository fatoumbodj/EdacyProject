
package com.bookmanagement.controller;

import com.bookmanagement.model.Book;
import com.bookmanagement.model.User;
import com.bookmanagement.repository.BookRepository;
import com.bookmanagement.repository.UserRepository;
import com.bookmanagement.security.services.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/books")
@Tag(name = "Gestion des livres", description = "Endpoints pour gérer les livres")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // Méthode utilitaire pour obtenir un utilisateur par défaut en mode démo
    private User getDefaultUser() {
        // Créer ou récupérer un utilisateur par défaut pour le mode démo
        Optional<User> defaultUser = userRepository.findByEmail("demo@example.com");
        if (defaultUser.isPresent()) {
            return defaultUser.get();
        } else {
            // Créer un utilisateur par défaut si il n'existe pas
            User user = new User("demo@example.com", "Demo", "User", "password");
            return userRepository.save(user);
        }
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les livres", description = "Récupère la liste des livres avec filtres optionnels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des livres récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<List<Book>> getAllBooks(@Parameter(description = "Terme de recherche dans le titre ou l'auteur")
                                                  @RequestParam(required = false) String search,
                                                  @Parameter(description = "Filtrer par catégorie")
                                                  @RequestParam(required = false) String category) {
        // Utiliser l'utilisateur par défaut en mode démo
        User user = getDefaultUser();

        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookRepository.findByUserAndTitleOrAuthorContaining(user, search);
        } else if (category != null && !category.isEmpty()) {
            books = bookRepository.findByUserAndCategory(user, category);
        } else {
            books = bookRepository.findByUser(user);
        }

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livre par ID", description = "Récupère les détails d'un livre spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<Book> getBookById(@Parameter(description = "ID du livre") @PathVariable Long id) {
        User user = getDefaultUser();

        Optional<Book> book = bookRepository.findByIdAndUser(id, user);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau livre", description = "Ajoute un nouveau livre à la collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        User user = getDefaultUser();

        book.setUser(user);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un livre", description = "Met à jour les informations d'un livre existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<Book> updateBook(@Parameter(description = "ID du livre") @PathVariable Long id,
                                           @Valid @RequestBody Book bookDetails) {
        User user = getDefaultUser();

        Optional<Book> optionalBook = bookRepository.findByIdAndUser(id, user);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setCategory(bookDetails.getCategory());
            book.setStatus(bookDetails.getStatus());
            book.setDescription(bookDetails.getDescription());
            book.setPublishYear(bookDetails.getPublishYear());
            book.setRating(bookDetails.getRating());
            book.setCoverUrl(bookDetails.getCoverUrl());

            Book updatedBook = bookRepository.save(book);
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre", description = "Supprime un livre de la collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<?> deleteBook(@Parameter(description = "ID du livre") @PathVariable Long id) {
        User user = getDefaultUser();

        Optional<Book> book = bookRepository.findByIdAndUser(id, user);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Statistiques des livres", description = "Récupère les statistiques des livres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<Map<String, Object>> getBookStats() {
        User user = getDefaultUser();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookRepository.countByUser(user));
        stats.put("availableBooks", bookRepository.countByUserAndStatus(user, Book.BookStatus.AVAILABLE));
        stats.put("borrowedBooks", bookRepository.countByUserAndStatus(user, Book.BookStatus.BORROWED));
        stats.put("reservedBooks", bookRepository.countByUserAndStatus(user, Book.BookStatus.RESERVED));

        Double avgRating = bookRepository.getAverageRatingByUser(user);
        stats.put("averageRating", avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);

        return ResponseEntity.ok(stats);
    }
}
