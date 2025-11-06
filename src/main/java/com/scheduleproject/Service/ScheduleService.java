package com.scheduleproject.Service;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Dto.Response.GetScheduleResponse;
import com.scheduleproject.Entity.Schedule;
import com.scheduleproject.Repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 스케줄 단건 조회 객체
     * @param numId auto_increment 번호 파라미터
     * @return
     */
    @Transactional(readOnly = true)
    public GetScheduleResponse getSchedule(Long numId) {
        Schedule schedule = scheduleRepository.findById(numId).orElseThrow(
                () -> new IllegalStateException("해당 번호의 스케줄이 없습니다.")
        );

        return new GetScheduleResponse(
                schedule.getNumId(),
                schedule.getWriTitle(),
                schedule.getWriContent(),
                schedule.getWriName(),
                schedule.getCreatedDateTime(),
                schedule.getModifiedDateTime()
        );
    }

    /**
     * 다건 조회
     * @param wriName 일정 작성 이름 파라미터
     * @return 작성된 이름에 관련된 일정 반환
     */
    @Transactional(readOnly = true)
    public List<GetScheduleResponse> getSchedules(String wriName) {
        List<Schedule> schedules = (wriName == null || wriName.isBlank())
                ? scheduleRepository.findAllByOrderByModifiedDateTimeDesc()
                : scheduleRepository.findByWriNameOrderByModifiedDateTimeDesc(wriName);

        return schedules.stream()
                .map(s -> new GetScheduleResponse(
                        s.getNumId(),
                        s.getWriTitle(),
                        s.getWriContent(),
                        s.getWriName(),
                        s.getCreatedDateTime(),
                        s.getModifiedDateTime()
                ))
                .toList();
    }
}
