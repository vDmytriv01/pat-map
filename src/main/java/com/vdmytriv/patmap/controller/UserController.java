package com.vdmytriv.patmap.controller;

import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.review.ReviewDto;
import com.vdmytriv.patmap.dto.user.UserDto;
import com.vdmytriv.patmap.service.ReviewService;
import com.vdmytriv.patmap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        UserDto user = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me/reviews")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ReviewDto>> getMyReviews(Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        List<ReviewDto> reviews = reviewService.getByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/me/favorites")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PlaceDto>> getFavorites(Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        List<PlaceDto> favorites = userService.getFavoritePlaces(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/me/favorites/{placeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addFavorite(@PathVariable Long placeId, Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        userService.addFavoritePlace(userId, placeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/favorites/{placeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long placeId, Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        userService.removeFavoritePlace(userId, placeId);
        return ResponseEntity.noContent().build();
    }
}
