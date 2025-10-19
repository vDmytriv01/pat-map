package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.booking.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingDto dto);

    List<BookingDto> getAll();

    BookingDto getById(Long id);

    BookingDto update(Long id, BookingDto dto);

    void delete(Long id);
}
