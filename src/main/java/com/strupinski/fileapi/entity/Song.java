package com.strupinski.fileapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "song")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "storage_type")
    private String storageType;

    @Column(name = "file_path")
    private String filePath;

    public Song(String name, String storageType, String filePath) {
        this.name = name;
        this.storageType = storageType;
        this.filePath = filePath;
    }
}
