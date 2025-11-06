package com.scheduleproject.Service;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Entity.Schedule;
import com.scheduleproject.Repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    /**
     * 스케줄 테이블 생성
     * @param createScheduleRequest CreateScheduleRequest 에 있는 json 형태의 입력값 파라미터 
     * @return api에 보여줄 CreateScheduleResponse 반환
     */
    @Transactional
    public CreateScheduleResponse createSchedule(CreateScheduleRequest createScheduleRequest) {
        Schedule schedule = new Schedule(
                createScheduleRequest.getWriTitle(),
                createScheduleRequest.getWriContent(),
                createScheduleRequest.getWriName(),
                createScheduleRequest.getPassword()
        );

        Schedule saveSchedule = scheduleRepository.save(schedule);

        return new CreateScheduleResponse(
                saveSchedule.getNumId(),
                saveSchedule.getWriTitle(),
                saveSchedule.getWriContent(),
                saveSchedule.getWriName(),
                saveSchedule.getCreatedDateTime(),
                saveSchedule.getModifiedDateTime()
        );
    }
}
