package com.vdmytriv.patmap.repository;

import com.vdmytriv.patmap.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
