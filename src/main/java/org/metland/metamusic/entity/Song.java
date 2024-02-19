package org.metland.metamusic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    private Integer id;
    private Integer trackNumber;
    private String title;
    private Album album;
    private Integer cdNumber;
    private Artist artist;
    private String genre;
    private Integer year;
    private String bpm;
    private String bitRate;
    private Integer length;
    private Long size;
}