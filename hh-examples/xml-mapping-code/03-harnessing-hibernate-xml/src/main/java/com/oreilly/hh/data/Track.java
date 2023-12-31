package com.oreilly.hh.data;

import java.util.Date;

import jakarta.persistence.*;

//import org.hibernate.annotations.Index;
import jakarta.persistence.Index;
//import org.hibernate.annotations.IndexColumn;
import jakarta.persistence.OrderColumn;
//import org.hibernate.annotations.ElementCollection;
import jakarta.persistence.ElementCollection;

/**
 *       Represents a single playable track in the music database.
 *       @author Jim Elliott (with help from Hibernate)
 *
 */
public class Track  implements java.io.Serializable {

    private Integer id;

    private String title;

    private String filePath;

    /**
     * Playing time
     */
    private Date playTime;

    /**
     * When the track was created
     */
    private Date added;

    /**
     * How loud to play the track
     */
    private short volume;

    public Track() {
    }

    public Track(String title, String filePath, short volume) {
        this.title = title;
        this.filePath = filePath;
        this.volume = volume;
    }

    public Track(String title, String filePath, Date playTime, Date added, short volume) {
        this.title = title;
        this.filePath = filePath;
        this.playTime = playTime;
        this.added = added;
        this.volume = volume;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     *
     * Playing time
     */
    public Date getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    /**
     *
     * When the track was created
     */
    public Date getAdded() {
        return this.added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    /**
     *
     * How loud to play the track
     */
    public short getVolume() {
        return this.volume;
    }

    public void setVolume(short volume) {
        this.volume = volume;
    }
}

