package org.metland.metamusic.service;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacAudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.FlacTag;
import org.metland.metamusic.entity.Album;
import org.metland.metamusic.entity.Artist;
import org.metland.metamusic.entity.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    private File libraryPath;
    @Autowired
    public LibraryService(@Value("${library.path}") String libraryPath){
        this.libraryPath = libraryPathToLibraryFolder(libraryPath);
    }

    public List<Song> readSongFiles(){
        List<Song> songFiles = readFiles(libraryPath);
        return songFiles;
    }

    private File libraryPathToLibraryFolder(String libraryPath){
        if(libraryPath != null && !libraryPath.isBlank() && !libraryPath.isEmpty()){
            Path path = Paths.get(libraryPath);
            if(Files.exists(path) && Files.isDirectory(path))
                return new File(libraryPath);
            else
                throw new IllegalStateException("Library path either doesn't exist, or is not a directory.");
        }else
            throw new IllegalStateException("Library path is either null or blank.");
    }

    private Song songFromFile(File fileSong) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        AudioFile f = AudioFileIO.read(fileSong);
        FlacAudioHeader flacAudioHeader = (FlacAudioHeader) f.getAudioHeader();
        FlacTag tag = (FlacTag) f.getTag();
        Song song = Song.builder()
            .trackNumber(fieldValueToInteger(tag.getFirst(FieldKey.TRACK)))
            .title(tag.getFirst(FieldKey.TITLE))
            .album(albumFromFile(tag))
            .cdNumber(fieldValueToInteger(tag.getFirst(FieldKey.DISC_NO)))
            .artist(artistFromFile(tag))
            .genre(tag.getFirst(FieldKey.GENRE))
            .year(fieldValueToInteger(tag.getFirst(FieldKey.YEAR)))
            .bpm(tag.getFirst(FieldKey.BPM))
            .bitRate(flacAudioHeader.getBitRate())
            .length(flacAudioHeader.getTrackLength())
            .size(fileSong.length())
        .build();
        return song;
    }

    private Artist artistFromFile(Tag tag){
        Artist artist = Artist.builder()
            .name(tag.getFirst(FieldKey.ARTIST))
        .build();
        return artist;
    }

    private Album albumFromFile(Tag tag){
        Album album = Album.builder()
            .title(tag.getFirst(FieldKey.ALBUM))
            .year(fieldValueToInteger(tag.getFirst(FieldKey.YEAR)))
            .discTotal(fieldValueToInteger(tag.getFirst(FieldKey.DISC_TOTAL)))
        .build();
        return album;
    }

    private List<Song> readFiles(File directory){
        List<Song> fileSongs = new ArrayList<>();
        for(File file: directory.listFiles()){
            if(!file.isDirectory()){
                try {
                    Song song = songFromFile(file);
                    fileSongs.add(song);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                fileSongs.addAll(readFiles(file));
            }
        }
        return fileSongs;
    }

    private Integer fieldValueToInteger(String fieldValue){
        return fieldValue != null && !fieldValue.isBlank() && !fieldValue.isEmpty() ? Integer.valueOf(fieldValue) : null;
    }
}