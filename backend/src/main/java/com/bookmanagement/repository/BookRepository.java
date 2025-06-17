
package com.bookmanagement.repository;

import com.bookmanagement.model.Book;
import com.bookmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
    List<Book> findByUserAndCategory(User user, String category);
    
    @Query("SELECT b FROM Book b WHERE b.user = :user AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Book> findByUserAndTitleOrAuthorContaining(@Param("user") User user, @Param("search") String search);
    
    Optional<Book> findByIdAndUser(Long id, User user);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.user = :user")
    Long countByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.user = :user AND b.status = :status")
    Long countByUserAndStatus(@Param("user") User user, @Param("status") Book.BookStatus status);
    
    @Query("SELECT AVG(b.rating) FROM Book b WHERE b.user = :user")
    Double getAverageRatingByUser(@Param("user") User user);
}
