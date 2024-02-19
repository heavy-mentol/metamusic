package org.metland.metamusic.controller;

import org.metland.metamusic.entity.Song;
import org.metland.metamusic.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("db")
public class LibraryController {
    private LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService){
        this.libraryService = libraryService;
    }

    @GetMapping(path = "songFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> readSongFiles(){
        List<Song> songFiles = libraryService.readSongFiles();
        return new ResponseEntity(songFiles, HttpStatus.OK);
    }
}