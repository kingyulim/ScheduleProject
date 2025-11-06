package com.scheduleproject.Controller;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Request.DeleteScheduleRequest;
import com.scheduleproject.Dto.Request.UpdateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Dto.Response.GetScheduleResponse;
import com.scheduleproject.Dto.Response.UpdateScheduleResponse;
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

    /**
     * 일정 업데이트
     * @param numId 일정 번호 파라미터
     * @param updateScheduleRequest 입력된 값 파라미터
     * @return 검사된 데이터 반환
     */
    @PutMapping("/schedules/{numId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(@PathVariable("numId") Long numId, @RequestBody UpdateScheduleRequest updateScheduleRequest){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.updateSchedule(numId, updateScheduleRequest));
    }

    /**
     * 일정 삭제
     * @param numId 일정 번호 파라미터
     * @param deleteScheduleRequest  입력된 값 파라미터
     * @return 검사된 데이터 반환
     */
    @DeleteMapping("/schedules/{numId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("numId") Long numId, DeleteScheduleRequest deleteScheduleRequest){
        scheduleService.deleteSchedule(numId, deleteScheduleRequest);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
