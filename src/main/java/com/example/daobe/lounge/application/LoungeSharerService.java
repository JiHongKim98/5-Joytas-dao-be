package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_EXISTS_LOUNGE_USER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_INVITED_LOUNGE_USER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_SHARER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION;

import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.LoungeSharerStatus;
import com.example.daobe.lounge.domain.event.LoungeInviteAcceptedEvent;
import com.example.daobe.lounge.domain.event.LoungeWithdrawEvent;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeSharerService {

    private static final int MAX_LOUNGE_COUNT = 4;

    private final ApplicationEventPublisher eventPublisher;
    private final LoungeSharerRepository loungeSharerRepository;

    public void createAndSaveLoungeSharer(User user, Lounge lounge) {
        if (isOverMaximumCountLounge(user.getId())) {
            throw new LoungeException(MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION);
        }

        LoungeSharer loungeSharer = LoungeSharer.builder()
                .user(user)
                .lounge(lounge)
                .status(LoungeSharerStatus.ACTIVE)
                .build();
        loungeSharerRepository.save(loungeSharer);
    }

    public void inviteUser(User user, Lounge lounge, Long inviterId) {
        validateInvite(user.getId(), lounge, inviterId);
        LoungeSharer loungeSharer = LoungeSharer.builder()
                .user(user)
                .lounge(lounge)
                .build();
        loungeSharerRepository.save(loungeSharer);
    }

    // FIXME:
    public void updateInvitedUserStatus(Long invitedUserId, Long loungeId) {
        LoungeSharer loungeSharer = loungeSharerRepository.findByUserIdAndLoungeId(invitedUserId, loungeId)
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION));
        if (loungeSharer.isActive()) {
            throw new LoungeException(ALREADY_EXISTS_LOUNGE_USER_EXCEPTION);
        }

        if (isOverMaximumCountLounge(invitedUserId)) {
            throw new LoungeException(MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION);
        }
        loungeSharer.updateStatusActive();
        loungeSharerRepository.save(loungeSharer);
        eventPublisher.publishEvent(new LoungeInviteAcceptedEvent(invitedUserId, loungeId));
    }

    public List<LoungeSharerInfoResponseDto> searchLoungeSharer(Long userId, String nickname, Lounge lounge) {
        validateLoungeSharer(userId, lounge.getId());
        List<LoungeSharer> byUserId = loungeSharerRepository
                .findActiveSharersByLoungeIdAndNickname(lounge.getId(), nickname);
        return LoungeSharerInfoResponseDto.of(byUserId);
    }

    public void withdraw(User user, Lounge lounge) {
        lounge.isActiveOrThrow();
        lounge.isPossibleToWithdrawOrThrow(user.getId());
        loungeSharerRepository.deleteByUserIdAndLoungeId(user.getId(), lounge.getId());
        eventPublisher.publishEvent(new LoungeWithdrawEvent(user.getId(), lounge.getId()));
    }

    public void validateLoungeSharer(Long userId, Long loungeId) {
        boolean isLoungeSharer = loungeSharerRepository.existsActiveLoungeSharerByUserIdAndLoungeId(userId, loungeId);
        if (!isLoungeSharer) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }
    }

    // TODO: 추후 도메인 로직으로 분리
    private void validateInvite(Long userId, Lounge lounge, Long inviterId) {
        lounge.isActiveOrThrow();
        validateLoungeSharer(inviterId, lounge.getId());
        validateAlreadyInvited(userId, lounge.getId());

        if (isExistUserInLounge(userId, lounge.getId())) {
            throw new LoungeException(ALREADY_EXISTS_LOUNGE_USER_EXCEPTION);
        }

        if (isOverMaximumCountLounge(userId)) {
            throw new LoungeException(MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION);
        }
    }

    private boolean isExistUserInLounge(Long userId, Long loungeId) {
        return loungeSharerRepository.existsActiveLoungeSharerByUserIdAndLoungeId(userId, loungeId);
    }

    private boolean isOverMaximumCountLounge(Long userId) {
        return loungeSharerRepository.countActiveLoungeSharerByUserId(userId) >= MAX_LOUNGE_COUNT;
    }

    private void validateAlreadyInvited(Long userId, Long loungeId) {
        boolean isAlreadyInvited = loungeSharerRepository.existsLoungeSharerByUserIdAndLoungeId(userId, loungeId);
        if (isAlreadyInvited) {
            throw new LoungeException(ALREADY_INVITED_LOUNGE_USER_EXCEPTION);
        }
    }
}
