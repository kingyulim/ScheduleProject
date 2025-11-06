package com.scheduleproject.Repository;

import com.scheduleproject.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByWriNameOrderByModifiedDateTimeDesc(String wriName);
    List<Schedule> findAllByOrderByModifiedDateTimeDesc();
}
