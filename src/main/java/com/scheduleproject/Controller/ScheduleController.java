package com.scheduleproject.Controller;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody CreateScheduleRequest createScheduleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(createScheduleRequest));
    }
}
