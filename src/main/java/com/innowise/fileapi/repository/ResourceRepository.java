package com.innowise.fileapi.repository;

import com.innowise.fileapi.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByName(String name);
}
