package com.scheduleproject.Controller;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Dto.Response.GetScheduleResponse;
import com.scheduleproject.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * 테이블 생성 검사 controller
     * @param createScheduleRequest CreateScheduleRequest 입력값 파라미터
     * @return 검사된 데이터 반환
     */
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(@RequestBody CreateScheduleRequest createScheduleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(createScheduleRequest));
    }

    /**
     * 단건 조회 검사 Controller
     * @param numId auto_increment 번호 파라미터
     * @return 검사된 데이터 반환
     */
    @GetMapping("/schedules/{numId}")
    public ResponseEntity<GetScheduleResponse> getSchedule(@PathVariable("numId") Long numId){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedule(numId));
    }

    /**
     * 다건 조회
     * @param wriName 작성자 이름 파라미터
     * @return 검사된 데이터 반환
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(@RequestParam(required = false) String wriName){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedules(wriName));
    }
}
