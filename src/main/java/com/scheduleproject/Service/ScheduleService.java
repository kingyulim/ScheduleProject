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
