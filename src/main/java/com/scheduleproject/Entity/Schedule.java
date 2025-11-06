package com.scheduleproject.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends Datetime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long numId;

    @Column(length = 50, nullable = false)
    private String wriTitle;

    @Column(columnDefinition = "TEXT")
    private String wriContent;

    @Column(length = 20, nullable = false)
    private String wriName;

    @Column(length = 20, nullable = false)
    private String password;

    public Schedule(String wriTitleParm, String wriContentParm, String wriNameParm, String passwordParm) {
        this.wriTitle = wriTitleParm;
        this.wriContent = wriContentParm;
        this.wriName = wriNameParm;
        this.password = passwordParm;
    }

    public void scheduleUpdate(String wriTitleParm, String wriNameParm) {
        this.wriTitle = wriTitleParm;
        this.wriContent = wriNameParm;
    }
}
