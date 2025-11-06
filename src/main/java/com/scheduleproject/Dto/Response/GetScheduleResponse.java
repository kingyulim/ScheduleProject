package com.scheduleproject.Dto.Response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetScheduleResponse {
    private final Long numId;
    private final String wriTitle;
    private final String wriContent;
    private final String wriName;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime modifiedDateTime;

    /**
     * api 응답 생성자
     * @param numIdParm auto_increment 파라미터
     * @param wriTitleParm 일정 제목 파라미터
     * @param wriContentParm 일정 내용 파라미터
     * @param wriNameParm 일정 이름 파라미터
     * @param createdDateTimeParm 최초 생성 시간 파라미터
     * @param modifiedDateTimeParm 수정 시간 파라미터
     */
    public GetScheduleResponse(
            Long numIdParm,
            String wriTitleParm,
            String wriContentParm,
            String wriNameParm,
            LocalDateTime createdDateTimeParm,
            LocalDateTime modifiedDateTimeParm
    ) {
        this.numId = numIdParm;
        this.wriTitle = wriTitleParm;
        this.wriContent = wriContentParm;
        this.wriName = wriNameParm;
        this.createdDateTime = createdDateTimeParm;
        this.modifiedDateTime = modifiedDateTimeParm;
    }
}
