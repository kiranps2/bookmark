package com.example.bookmark.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
public class Bookmark {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void onCreate() {
        addedAt = LocalDateTime.now();
    }
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id=id;
    }
    public String getTitle() {
    	return title;
    }
    public void setTitle(String title) {
    	this.title=title;
    }
    public String getUrl() {
    	return url;
    }
    public void setUrl(String url) {
    	this.url=url;
    }
    public LocalDateTime getAddedAt() {
    	return addedAt;
    }
    public User getUser() { 
    	return user; 
    	} 
    public void setUser(User user) { 
    	this.user = user;
    	}
}
