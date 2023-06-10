package com.innowise.fileapi.repository;

import com.innowise.fileapi.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByFilePath(String key);
}
