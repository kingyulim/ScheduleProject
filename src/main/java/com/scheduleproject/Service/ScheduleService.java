package com.scheduleproject.Service;

import com.scheduleproject.Dto.Request.CreateScheduleRequest;
import com.scheduleproject.Dto.Request.DeleteScheduleRequest;
import com.scheduleproject.Dto.Request.UpdateScheduleRequest;
import com.scheduleproject.Dto.Response.CreateScheduleResponse;
import com.scheduleproject.Dto.Response.GetScheduleResponse;
import com.scheduleproject.Dto.Response.UpdateScheduleResponse;
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
     * @return 생성된 스케줄 필드 api json 반환
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
     * @param numId 조회 할 번호 파라미터
     * @return 조회된 데이터 api json 반환
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
     * @param wriName 일정 작성한 이름 파라미터
     * @return 다건 조회 api json 형태로 반환
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

    /**
     * 일정 업데이트
     * @param numId 수정 할 번호 파라미터
     * @param updateScheduleRequest 입력값 파라미터
     * @return 업데이트된 내용 api json 반환
     */
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long numId, UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(numId).orElseThrow(
                () -> new IllegalStateException("번호에 해당되는 일정이 없습니다.")
        );

        if(!schedule.getPassword().equals(updateScheduleRequest.getPassword())) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }

        schedule.scheduleUpdate(
                updateScheduleRequest.getWriTitle(),
                updateScheduleRequest.getWriName()
        );

        return new UpdateScheduleResponse(
                schedule.getNumId(),
                schedule.getWriTitle(),
                schedule.getWriContent(),
                schedule.getWriName(),
                schedule.getModifiedDateTime()
        );
    }

    /**
     * 일정 삭제
     * @param numId 삭제 할 일정 번호 파라미터
     * @param deleteScheduleRequest 입력된 값 파라미터
     */
    @Transactional
    public void deleteSchedule(Long numId, DeleteScheduleRequest deleteScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(numId).orElseThrow(
                () -> new IllegalStateException("번호에 해당되는 일정이 없습니다.")
        );

        if (schedule.getPassword().equals(deleteScheduleRequest.getPassword())) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }

        scheduleRepository.deleteById(numId);
    }
}
