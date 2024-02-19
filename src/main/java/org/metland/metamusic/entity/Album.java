package org.metland.metamusic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private Integer id;
    private String title;
    private Integer year;
    private Integer discTotal;
}