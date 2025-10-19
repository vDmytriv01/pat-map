package com.vdmytriv.patmap.controller;

import com.vdmytriv.patmap.dto.place.CreatePlaceRequestDto;
import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.place.UpdatePlaceRequestDto;
import com.vdmytriv.patmap.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceDto> create(@Valid @RequestBody CreatePlaceRequestDto requestDto) {
        PlaceDto created = placeService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/places/" + created.getId())).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PlaceDto>> getAll(@RequestParam(required = false) Long categoryId,
                                                 @RequestParam(required = false) String city) {
        List<PlaceDto> places = placeService.search(categoryId, city);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PlaceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceDto> update(@PathVariable Long id,
                                           @Valid @RequestBody UpdatePlaceRequestDto requestDto) {
        PlaceDto updated = placeService.update(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceDto> partialUpdate(@PathVariable Long id,
                                                  @RequestBody UpdatePlaceRequestDto requestDto) {
        PlaceDto updated = placeService.update(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        placeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
