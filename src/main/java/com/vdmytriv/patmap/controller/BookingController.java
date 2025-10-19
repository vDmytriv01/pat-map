package com.vdmytriv.patmap.controller;

import com.vdmytriv.patmap.dto.booking.BookingDto;
import com.vdmytriv.patmap.dto.booking.CreateBookingRequestDto;
import com.vdmytriv.patmap.model.BookingStatus;
import com.vdmytriv.patmap.service.BookingService;
import com.vdmytriv.patmap.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingDto> create(@Valid @RequestBody CreateBookingRequestDto requestDto,
                                             Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setUserId(userId);
        bookingDto.setPlaceId(requestDto.getPlaceId());
        bookingDto.setDateTime(requestDto.getDateTime());
        bookingDto.setStatus(BookingStatus.PENDING);
        BookingDto created = bookingService.create(bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        List<BookingDto> bookings = bookingService.getByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BookingDto> cancel(@PathVariable Long id, Authentication authentication) {
        BookingDto booking = bookingService.getById(id);
        Long currentUserId = userService.getByEmail(authentication.getName()).getId();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
        if (!isAdmin && !booking.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to cancel this booking");
        }
        BookingDto canceled = bookingService.cancel(id);
        return ResponseEntity.ok(canceled);
    }
}
