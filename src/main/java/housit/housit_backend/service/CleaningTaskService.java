package housit.housit_backend.service;

import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.cleaning.CleaningTaskMember;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.CleaningTaskDto;
import housit.housit_backend.dto.request.CleaningTaskSaveRequestDto;
import housit.housit_backend.repository.CleaningTaskRepository;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CleaningTaskService {
    private final CleaningTaskRepository cleaningTaskRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public List<CleaningTaskDto> getCleaningTask(Long roomId) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<CleaningTask> cleaningTasks = cleaningTaskRepository.findAll(room);
        List<CleaningTaskDto> cleaningTaskDtos = new ArrayList<>();
        for (CleaningTask cleaningTask : cleaningTasks) {
            cleaningTaskDtos.add(CleaningTaskDto.entityToDto(cleaningTask));
        }
        return cleaningTaskDtos;
    }

    @Transactional
    public CleaningTaskDto createCleaningTask(Long roomId, CleaningTaskSaveRequestDto cleaningTaskSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<CleaningTaskMember> cleaningMembers = new ArrayList<>();
        List<Member> belongMembers = memberRepository.findBelongMembers(cleaningTaskSaveDto.getMemberIds());
        for (Member belongMember : belongMembers) {
            CleaningTaskMember cleaningTaskMember = new CleaningTaskMember();
            cleaningTaskMember.creatMember(belongMember);
            cleaningMembers.add(cleaningTaskMember);
        }

        CleaningTask cleaningTask = CleaningTask.createCleaningTask(room, cleaningTaskSaveDto, cleaningMembers);
        cleaningTaskRepository.save(cleaningTask);

        return CleaningTaskDto.entityToDto(cleaningTask);
    }

    @Transactional
    public CleaningTaskDto updateCleaningTask(Long roomId, Long cleanTaskId, CleaningTaskSaveRequestDto cleaningTaskSaveDto) {
        CleaningTask cleaningTask = cleaningTaskRepository.findById(cleanTaskId);
        List<Member> updatedMembers = memberRepository.findBelongMembers(cleaningTaskSaveDto.getMemberIds());

        // 새로운 멤버 리스트 생성
        List<CleaningTaskMember> newMembers = new ArrayList<>();

        // 새로운 멤버들로 CleaningTaskMember 생성
        for (Member member : updatedMembers) {
            CleaningTaskMember newMember = new CleaningTaskMember();
            newMember.creatMember(member);
            newMember.createCleaning(cleaningTask);
            newMembers.add(newMember);
        }

        cleaningTask.updateCleaningTask(cleaningTaskSaveDto, newMembers);
        return CleaningTaskDto.entityToDto(cleaningTask);
    }

    @Transactional
    public void deleteCleaningTask(Long roomId, Long cleanTaskId) {
        cleaningTaskRepository.delete(cleanTaskId);
    }
}
