package com.scheduleproject.Dto.Request;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private String wriTitle;
    private String wriContent;
    private String wriName;
    private String password;
}
