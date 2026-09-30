package com.example.bookmark.repository;

import com.example.bookmark.model.Bookmark;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository
        extends JpaRepository<Bookmark, Long> {

    long countByUserId(Long userId);

    Page<Bookmark> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<Bookmark>
    findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndUrlContainingIgnoreCase(
            Long userId1,
            String title,
            Long userId2,
            String url,
            Pageable pageable
    );

    Optional<Bookmark> findByIdAndUserId(
            Long id,
            Long userId
    );
}