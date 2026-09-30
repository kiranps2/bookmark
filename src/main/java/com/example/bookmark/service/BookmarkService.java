package com.example.bookmark.service;

import com.example.bookmark.model.Bookmark;
import com.example.bookmark.model.User;
import com.example.bookmark.repository.BookmarkRepository;
import com.example.bookmark.repository.UserRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(
            BookmarkRepository bookmarkRepository,
            UserRepository userRepository) {

        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Transactional
    public void addBookmark(
            String username,
            String title,
            String url) {

        User user = getUser(username);

        long count = bookmarkRepository
                .countByUserId(user.getId());

        if (count >= 5) {
            throw new IllegalStateException(
                    "You can add only 5 bookmarks");
        }

        Bookmark bookmark = new Bookmark();

        bookmark.setTitle(title);
        bookmark.setUrl(url);
        bookmark.setUser(user);

        bookmarkRepository.save(bookmark);
    }

    public Page<Bookmark> getBookmarks(
            String username,
            String keyword,
            int page) {

        User user = getUser(username);

        Pageable pageable = PageRequest.of(
                page,
                3,
                Sort.by("addedAt").ascending()
        );

        if (keyword == null || keyword.isBlank()) {

            return bookmarkRepository.findByUserId(
                    user.getId(),
                    pageable
            );
        }

        return bookmarkRepository
            .findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndUrlContainingIgnoreCase(
                    user.getId(),
                    keyword,
                    user.getId(),
                    keyword,
                    pageable
            );
    }

    public Bookmark getBookmark(
            Long id,
            String username) {

        User user = getUser(username);

        return bookmarkRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bookmark not found"));
    }

    @Transactional
    public void updateBookmark(
            Long id,
            String username,
            String title,
            String url) {

        Bookmark bookmark =
                getBookmark(id, username);

        bookmark.setTitle(title);
        bookmark.setUrl(url);

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(
            Long id,
            String username) {

        Bookmark bookmark =
                getBookmark(id, username);

        bookmarkRepository.delete(bookmark);
    }
}