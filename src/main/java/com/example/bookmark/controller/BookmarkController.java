package com.example.bookmark.controller;

import com.example.bookmark.model.Bookmark;

import com.example.bookmark.service.BookmarkService;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(
            BookmarkService bookmarkService) {

        this.bookmarkService = bookmarkService;
    }


    @GetMapping
    public String listBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            Authentication authentication,
            Model model) {

        Page<Bookmark> bookmarks =
                bookmarkService.getBookmarks(
                        authentication.getName(),
                        keyword,
                        page
                );

        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("keyword", keyword);

        return "bookmarks";
    }


    @GetMapping("/add")
    public String addPage(Model model) {

        model.addAttribute("bookmark", new Bookmark());

        return "bookmark-form";
    }


    @PostMapping("/add")
    public String addBookmark(
            @RequestParam String title,
            @RequestParam String url,
            Authentication authentication,
            Model model) {

        try {

            bookmarkService.addBookmark(
                    authentication.getName(),
                    title,
                    url
            );

            return "redirect:/bookmarks";

        } catch (IllegalStateException e) {

            model.addAttribute("error", e.getMessage());

            Bookmark bookmark = new Bookmark();
            bookmark.setTitle(title);
            bookmark.setUrl(url);

            model.addAttribute("bookmark", bookmark);

            return "bookmark-form";
        }
    }


    @GetMapping("/edit/{id}")
    public String editPage(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Bookmark bookmark =
                bookmarkService.getBookmark(
                        id,
                        authentication.getName()
                );

        model.addAttribute("bookmark", bookmark);

        return "bookmark-form";
    }


    @PostMapping("/edit/{id}")
    public String updateBookmark(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String url,
            Authentication authentication) {

        bookmarkService.updateBookmark(
                id,
                authentication.getName(),
                title,
                url
        );

        return "redirect:/bookmarks";
    }


    @PostMapping("/delete/{id}")
    public String deleteBookmark(
            @PathVariable Long id,
            Authentication authentication) {

        bookmarkService.deleteBookmark(
                id,
                authentication.getName()
        );

        return "redirect:/bookmarks";
    }
}