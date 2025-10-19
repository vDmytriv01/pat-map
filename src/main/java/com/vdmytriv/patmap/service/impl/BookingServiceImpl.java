package com.vdmytriv.patmap.service.impl;

import com.vdmytriv.patmap.dto.booking.BookingDto;
import com.vdmytriv.patmap.mapper.BookingMapper;
import com.vdmytriv.patmap.model.Booking;
import com.vdmytriv.patmap.model.BookingStatus;
import com.vdmytriv.patmap.model.Place;
import com.vdmytriv.patmap.model.User;
import com.vdmytriv.patmap.repository.BookingRepository;
import com.vdmytriv.patmap.repository.PlaceRepository;
import com.vdmytriv.patmap.repository.UserRepository;
import com.vdmytriv.patmap.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto create(BookingDto dto) {
        Booking booking = bookingMapper.toEntity(dto);
        booking.setUser(getUser(dto.getUserId()));
        booking.setPlace(getPlace(dto.getPlaceId()));
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getByUserId(Long userId) {
        return bookingRepository.findAllByUserId(userId).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long id) {
        Booking booking = getBooking(id);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(Long id, BookingDto dto) {
        Booking booking = getBooking(id);
        bookingMapper.updateBookingFromDto(dto, booking);
        if (dto.getUserId() != null
                && (booking.getUser() == null || !dto.getUserId().equals(booking.getUser().getId()))) {
            booking.setUser(getUser(dto.getUserId()));
        }
        if (dto.getPlaceId() != null
                && (booking.getPlace() == null || !dto.getPlaceId().equals(booking.getPlace().getId()))) {
            booking.setPlace(getPlace(dto.getPlaceId()));
        }
        Booking updated = bookingRepository.save(booking);
        return bookingMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Booking booking = getBooking(id);
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional
    public BookingDto cancel(Long id) {
        Booking booking = getBooking(id);
        booking.setStatus(BookingStatus.CANCELED);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private Place getPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
    }
}
