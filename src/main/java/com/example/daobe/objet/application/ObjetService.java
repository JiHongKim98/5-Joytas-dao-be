package com.example.daobe.objet.application;

import static com.example.daobe.objet.exception.ObjetExceptionType.OBJET_NOT_FOUND_EXCEPTION;

import com.example.daobe.lounge.application.LoungeService;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetDeleteResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetInfoResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.objet.domain.event.ObjetCreateEvent;
import com.example.daobe.objet.domain.event.ObjetDeleteEvent;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetService {

    private final ApplicationEventPublisher eventPublisher;
    private final ObjetSharerService objetSharerService;
    private final ObjetRepository objetRepository;
    private final LoungeService loungeService;
    private final UserService userService;

    @Transactional
    public ObjetInfoResponseDto createNewObjet(ObjetCreateRequestDto request, Long userId) {
        Lounge findLounge = loungeService.getLoungeById(request.loungeId());
        User findUser = userService.getUserById(userId);

        Objet newObjet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .user(findUser)
                .lounge(findLounge)
                .imageUrl(request.objetImage())
                .build();
        objetRepository.save(newObjet);
        objetSharerService.createAndSaveObjetSharer(request, userId, newObjet);

        eventPublisher.publishEvent(new ObjetCreateEvent(newObjet));
        return ObjetInfoResponseDto.of(newObjet);
    }

    @Transactional
    public ObjetInfoResponseDto updateObjet(ObjetUpdateRequestDto request, Long objetId, Long userId) {
        Objet findObjet = objetRepository.findByIdAndActiveStatus(objetId)
                .orElseThrow(() -> new ObjetException(OBJET_NOT_FOUND_EXCEPTION));
        findObjet.updateObjetInfo(request.name(), request.description(), request.objetImage(), userId);

        Objet updatedObjet = objetRepository.save(findObjet);
        objetSharerService.updateObjetSharerList(updatedObjet, request.sharers());
        return ObjetInfoResponseDto.of(updatedObjet);
    }

    public List<ObjetResponseDto> getObjetListByUserId(Long userId, Long loungeId) {
        List<Objet> objetList = objetRepository.findActiveObjetListInLoungeOfSharer(userId, loungeId);
        return ObjetResponseDto.listOf(objetList);
    }

    public List<ObjetResponseDto> getObjetList(Long loungeId) {
        List<Objet> objetList = objetRepository.findActiveObjetListInLounge(loungeId);
        return ObjetResponseDto.listOf(objetList);
    }

    public ObjetDetailResponseDto getObjetDetail(Long objetId) {
        Objet findObjet = getObjetById(objetId);
        return ObjetDetailResponseDto.of(findObjet);
    }

    public List<ObjetMeResponseDto> getMyObjetList(Long userId) {
        List<ObjetSharer> objetSharerList = objetSharerService.getRecentObjetSharerList(userId);
        return objetSharerList.stream()
                .filter(objetSharer -> objetSharer.getObjet().getStatus() == ObjetStatus.ACTIVE)
                .map((objetSharer) -> ObjetMeResponseDto.of(objetSharer.getObjet()))
                .toList();
    }

    @Transactional
    public ObjetDeleteResponseDto deleteObjet(Long objetId, Long userId) {
        Objet findObjet = getObjetById(objetId);
        findObjet.softDelete(userId);
        objetRepository.save(findObjet);
        eventPublisher.publishEvent(new ObjetDeleteEvent(findObjet.getId()));
        return ObjetDeleteResponseDto.of(findObjet);
    }


    private Objet getObjetById(Long objetId) {
        return objetRepository.findByIdAndActiveStatus(objetId)
                .orElseThrow(() -> new ObjetException(OBJET_NOT_FOUND_EXCEPTION));
    }
}
