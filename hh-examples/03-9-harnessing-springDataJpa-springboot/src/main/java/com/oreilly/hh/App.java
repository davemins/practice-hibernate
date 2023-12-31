package com.oreilly.hh;

import java.time.LocalTime;
import java.util.*;

import jakarta.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.oreilly.hh.dao.TrackDAO;
import com.oreilly.hh.data.*;

@SpringBootApplication
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args).close();
    }

    @Bean
    public CommandLineRunner demo(TrackDAO trackDAO) {
        return (args) -> {

            //
            // CreateTest
            //

            var track = new Track("Russian Trance",
                                  "vol2/album610/track02.mp3",
                                  LocalTime.of(00,03,30));
            trackDAO.save(track);

            track = new Track("Video Killed the Radio Star",
                              "vol2/album611/track12.mp3",
                              LocalTime.of(00,03,49));
            trackDAO.save(track);

            track = new Track("Gravity's Angel",
                              "vol2/album175/track03.mp3",
                              LocalTime.of(00,06,06));
            trackDAO.save(track);

            //
            // QueryTest
            //

            List<Track> tracks = trackDAO.findByPlayTimeLessThanEqual(LocalTime.of(00,07,00));

            for (Track t : tracks) {
                System.out.printf("Track: \"%s\" %s\n", t.getTitle(), t.getPlayTime());
            }
        };
    }
}
