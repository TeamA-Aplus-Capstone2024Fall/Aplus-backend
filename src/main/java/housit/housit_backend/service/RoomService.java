package housit.housit_backend.service;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.finance.PredictedExpense;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.finance.SavingGoal;
import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.*;
import housit.housit_backend.dto.request.MemberSaveDto;
import housit.housit_backend.dto.request.RoomSaveDto;
import housit.housit_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final FinanceRepository financeRepository;
    private final FoodRepository foodRepository;
    private final ChoreRepository choreRepository;
    private final EventRepository eventRepository;

    @Transactional
    public RoomCreateResponseDto createRoom(RoomSaveDto roomSaveDto) {
        if(roomRepository.findRoomByRoomName(roomSaveDto.getRoomName()) != null) {
            throw new IllegalArgumentException("Room already exists");
        }

        Room createdRoom = roomSaveDto.toRoomEntity();
        roomRepository.saveRoom(createdRoom);

        Member masterMember = roomSaveDto.toMasterMemberEntity(createdRoom);
        memberRepository.saveMember(masterMember);

        createdRoom.initializeMasterMemberRoomId(masterMember.getMemberId());
        roomRepository.saveRoom(createdRoom);

        return createdRoom.toRoomCreateResponseDto();
    }

    @Transactional
    public List<RoomDto> getRoomsWithMembers(Pageable pageable) {
        List<Room> allRooms = roomRepository.findAllRooms(pageable);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : allRooms) {
            List<Member> members = memberRepository.getAllMembers(room.getRoomId());
            List<MemberDto> memberDtos = new ArrayList<>();
            for (Member member : members) memberDtos.add(MemberDto.entityToDto(member));
            RoomDto roomDto = new RoomDto(room, memberDtos);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    @Transactional
    public Boolean validateRoomPassword(Long roomId, String roomPassword) {
        Optional<Room> room = roomRepository.findRoomById(roomId);
        if (room.isEmpty()) return false;
        return room.get().validatePassword(roomPassword);
    }

    @Transactional
    public MemberDto createMember(Long roomId, MemberSaveDto memberSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Member member = Member.createMember(memberSaveDto, room);
        Member saveMember = memberRepository.saveMember(member);
        return MemberDto.entityToDto(saveMember);
    }

    @Transactional
    public List<MemberDto> getMembers(Long roomId) {
        List<Member> allMembers = memberRepository.getAllMembers(roomId);
        List<MemberDto> memberDtoList = new ArrayList<>();
        for (Member member : allMembers) {
            memberDtoList.add(MemberDto.entityToDto(member));
        }
        return memberDtoList;
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteRoom(roomId);
    }

    @Transactional
    public Boolean validateMemberPassword(Long memberId, String memberPassword) {
        Member member = memberRepository.findMemberById(memberId);
        if (member == null) return false;
        return member.validatePassword(memberPassword);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteMember(memberId);
    }

    @Transactional
    public MemberDto updateMember(Long memberId, MemberSaveDto memberSaveDto) {
        Member member = memberRepository.findMemberById(memberId);
        if(member != null){
            member.updateMember(memberSaveDto.getMemberName(),
                    memberSaveDto.getMemberPassword(),
                    memberSaveDto.getMemberIcon());
            memberRepository.saveMember(member);
            return MemberDto.entityToDto(member);
        }
        return null;
    }

    public HomeDto getHome(Long roomId) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Member member = memberRepository.findMemberById(1L); // 일단 임시로 1번째 멤버로 설정
        List<Food> expiringSoonFoods = foodRepository.getExpiringSoonFoods(roomId, 7); // 임시로 7일로 설정
        List<Food> outOfFavoriteFoods = foodRepository.getOutOfFavoriteFoods(roomId, 5); // 임시로 5개로 설정
        List<PredictedIncome> predictedIncomes = financeRepository.findSoonPredictedIncomes(room, 7); // 임시로 7일로 설정
        List<PredictedExpense> predictedExpenses = financeRepository.findSoonPredictedExpenses(room, 7); // 임시로 7일로 설정
        List<SavingGoal> savingGoals = financeRepository.findSoonSavingGoals(room, 7); // 임시로 7일로 설정
        List<Event> events = eventRepository.getSoonEvents(roomId, 7); // 임시로 7일로 설정

        // expiringSoonFoods를 expiringSoonFoodsDto로 변환
        // outOfFavoriteFoods를 outOfFavoriteFoodsDto로 변환
        // events를 eventDtos로 변환
        List<FoodDto> expiringSoonFoodsDto = expiringSoonFoods.stream().map(FoodDto::entityToDto).collect(Collectors.toList());
        List<FoodDto> outOfFavoriteFoodsDto = outOfFavoriteFoods.stream().map(FoodDto::entityToDto).collect(Collectors.toList());
        List<EventDto> eventDtos = events.stream().map(EventDto::entityToDto).collect(Collectors.toList());

        return new HomeDto(expiringSoonFoodsDto,
                outOfFavoriteFoodsDto,
                predictedIncomes,
                savingGoals,
                predictedExpenses,
                new ArrayList<>(), // 임시로 비워둠
                eventDtos);
    }
}
