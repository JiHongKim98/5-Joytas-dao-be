package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_INVITED_USER_EXCEPTION;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto.ObjetInfo;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInviteDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeResult;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final UserRepository userRepository;
    private final LoungeService loungeService;
    private final LoungeSharerRepository loungeSharerRepository;
    private final ObjetRepository objetRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LoungeCreateResponseDto create(LoungeCreateRequestDto request, Long userId) {
        User findUser = findUserById(userId);
        LoungeCreateResponseDto response = loungeService.createLounge(request, findUser);
        Lounge findLounge = loungeService.findLoungeById(response.loungeId());

        LoungeSharer userLounge = LoungeSharer.builder()
                .user(findUser)
                .lounge(findLounge)
                .build();
        loungeSharerRepository.save(userLounge);
        return LoungeCreateResponseDto.of(findLounge);
    }

    public LoungeDetailInfoDto getLoungeDetail(Long userId, Long loungeId) {
        Lounge findLounge = findLoungeById(loungeId);
        findUserById(userId);

        // ACTIVE 상태가 아닌 라운지라면 예외 발생
        findLounge.validateLoungeStatus();

        List<LoungeDetailInfoDto.ObjetInfo> objetInfos = objetRepository
                .findLoungeObjetByUser(loungeId, userId).stream()
                .map(ObjetInfo::of)
                .toList();

        return loungeService.createLoungeDetailInfo(loungeId, objetInfos);
    }

    @Transactional
    public LoungeResult inviteUser(Long userId, LoungeInviteDto request) {
        User findUser = findUserById(request.userId());
        Lounge findLounge = findLoungeById(request.loungeId());

        // TODO: 알림 기능 구현 이후 유저가 초대 수락시 해당 메서드 실행
        // 해당 라운지에 이미 소속되어 있는지 확인
        if (isNotExistUserInLounge(findUser, findLounge)) {
            LoungeSharer loungeSharer = LoungeSharer.builder()
                    .user(findUser)
                    .lounge(findLounge)
                    .build();
            loungeSharerRepository.save(loungeSharer);
            eventPublisher.publishEvent(new LoungeInviteEvent(userId, loungeSharer));
            return LoungeResult.LOUNGE_INVITE_SUCCESS;
        }
        throw new LoungeException(ALREADY_INVITED_USER_EXCEPTION);
    }

    @Transactional
    public void delete(Long userId, Long loungeId) {
        User findUser = findUserById(userId);
        Lounge findLounge = findLoungeById(loungeId);
        findLounge.softDelete(findUser);
    }

    // find
    public List<LoungeInfoDto> findLoungeByUserId(Long userId) {
        return loungeService.findLoungeByUserId(userId);
    }

    private Lounge findLoungeById(Long id) {
        return loungeService.findLoungeById(id);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }

    private boolean isNotExistUserInLounge(User user, Lounge lounge) {
        return !loungeSharerRepository.existsByUserIdAndLoungeId(user.getId(), lounge.getId());
    }
}
