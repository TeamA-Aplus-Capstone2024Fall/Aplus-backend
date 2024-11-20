package housit.housit_backend.service;

import housit.housit_backend.domain.chore.Chore;
import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.finance.PredictedExpense;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.finance.SavingGoal;
import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.*;
import housit.housit_backend.dto.request.MemberSaveDto;
import housit.housit_backend.dto.request.MemberSettingSaveDto;
import housit.housit_backend.dto.request.RoomSaveDto;
import housit.housit_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
            return null;
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

    public HomeDto getHome(Long roomId, Long memberId) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Member member = memberRepository.findMemberById(memberId);
        List<Food> expiringSoonFoods = foodRepository.getExpiringSoonFoods(roomId, member.getFoodDays());
        List<Food> outOfFavoriteFoods = foodRepository.getOutOfFavoriteFoods(roomId, member.getMinimumFoodQuantity());
        List<PredictedIncome> predictedIncomes = financeRepository.findSoonPredictedIncomes(room, member.getFinanceDays());
        List<PredictedExpense> predictedExpenses = financeRepository.findSoonPredictedExpenses(room, member.getFinanceDays());
        List<SavingGoal> savingGoals = financeRepository.findSoonSavingGoals(room, member.getFinanceDays());
        List<Event> events = eventRepository.getSoonEvents(roomId, member.getEventDays(), member.getMemberId());
        List<Chore> chores = choreRepository.getSoonChores(roomId, member.getChoreDays(), member.getMemberId());
        List<Chore> soonChores = filterSoonChores(chores, member.getChoreDays());

        // expiringSoonFoods를 expiringSoonFoodsDto로 변환
        // outOfFavoriteFoods를 outOfFavoriteFoodsDto로 변환
        // events를 eventDtos로 변환
        List<FoodDto> expiringSoonFoodsDto = expiringSoonFoods.stream().map(FoodDto::entityToDto).collect(Collectors.toList());
        List<FoodDto> outOfFavoriteFoodsDto = outOfFavoriteFoods.stream().map(FoodDto::entityToDto).collect(Collectors.toList());
        List<EventDto> eventDtos = events.stream().map(EventDto::entityToDto).collect(Collectors.toList());
        List<ChoreDto> choreDtos = soonChores.stream().map(ChoreDto::entityToDto).toList();

        return new HomeDto(expiringSoonFoodsDto,
                outOfFavoriteFoodsDto,
                predictedIncomes,
                savingGoals,
                predictedExpenses,
                choreDtos,
                eventDtos);
    }

    public Long getRoomId(String roomName) {
        Room room = roomRepository.findRoomByRoomName(roomName);
        if (room == null) return null;
        return room.getRoomId();
    }

    public MemberDto updateMemberSetting(Long memberId, MemberSettingSaveDto memberSettingSaveDto) {
        Member member = memberRepository.findMemberById(memberId);
        if(member != null){
            member.updateMemberSetting(memberSettingSaveDto);
            memberRepository.saveMember(member);
            return MemberDto.entityToDto(member);
        }
        return null;
    }

    private LocalDate calculateClosestChoreDate(LocalDate enrolledDate, Integer choreFrequency) {
        if (choreFrequency == null || choreFrequency <= 0) return null; // Frequency가 없거나 비정상적인 경우 제외

        LocalDate today = LocalDate.now();
        long daysSinceEnrolled = ChronoUnit.DAYS.between(enrolledDate, today);

        if (daysSinceEnrolled < 0) {
            // 아직 시작되지 않은 경우, 바로 enrolledDate 반환
            return enrolledDate;
        }

        // 오늘 이후 가장 가까운 미래 날짜 계산
        long nextCycle = (daysSinceEnrolled / choreFrequency + 1) * choreFrequency;
        return enrolledDate.plusDays(nextCycle);
    }

    public List<Chore> filterSoonChores(List<Chore> chores, int choreDays) {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(choreDays);

        return chores.stream()
                .filter(chore -> {
                    LocalDate closestFutureDate = calculateClosestChoreDate(chore.getEnrolledDate(), chore.getChoreFrequency());
                    return closestFutureDate != null
                            && !closestFutureDate.isBefore(today)
                            && !closestFutureDate.isAfter(maxDate);
                })
                .sorted(Comparator.comparing(chore -> calculateClosestChoreDate(chore.getEnrolledDate(), chore.getChoreFrequency())))
                .collect(Collectors.toList());
    }
}
