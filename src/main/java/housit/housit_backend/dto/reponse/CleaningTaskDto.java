package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.cleaning.CleaningDay;
import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.cleaning.CleaningTaskMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CleaningTaskDto {
    private Long cleaningTaskId;
    private String cleaningArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;
    private CleaningDay cleaningDay;  // 청소 요일 (Nullable)
    private Integer cleaningFrequency;  // 청소 주기 (예: 주 1회)
    private List<MemberDto> cleaningTaskMembers = new ArrayList<>();

    public static CleaningTaskDto entityToDto(CleaningTask cleaningTask) {
        List<CleaningTaskMember> cleaningTaskMembers = cleaningTask.getCleaningTaskMembers();
        List<MemberDto> memberDtos = new ArrayList<>();

        for (CleaningTaskMember cleaningTaskMember : cleaningTaskMembers) {
            Member member = cleaningTaskMember.getMember();
            memberDtos.add(MemberDto.entityToDto(member));
        }

        return CleaningTaskDto.builder()
                .cleaningTaskId(cleaningTask.getCleaningTaskId())
                .cleaningArea(cleaningTask.getCleaningArea())
                .color(cleaningTask.getColor())
                .description(cleaningTask.getDescription())
                .icon(cleaningTask.getIcon())
                .cleaningDay(cleaningTask.getCleaningDay())
                .cleaningFrequency(cleaningTask.getCleaningFrequency())
                .cleaningTaskMembers(memberDtos)
                .build();
    }
}
