package com.vdmytriv.patmap.repository;

import com.vdmytriv.patmap.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findAllByCategoryId(Long categoryId);

    List<Place> findAllByAddressContainingIgnoreCase(String city);

    List<Place> findAllByCategoryIdAndAddressContainingIgnoreCase(Long categoryId, String city);
}
