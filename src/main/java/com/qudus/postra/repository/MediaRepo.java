package com.qudus.postra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.postra.model.Media;

public interface MediaRepo extends JpaRepository<Media, UUID> {
    
}
