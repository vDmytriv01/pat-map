package com.vdmytriv.patmap.repository;

import com.vdmytriv.patmap.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
