package com.example.daobe.objet.domain;

import com.example.daobe.common.entity.BaseTimeEntity;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "objets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Objet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objet_id")
    private Long objetId;

    @JoinColumn(name = "lounge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lounge lounge;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "objet")
    private List<ObjetSharer> objetSharers;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ObjetType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ObjetStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @Builder
    public Objet(
            Lounge lounge,
            User user,
            String name,
            String imageUrl,
            String explanation,
            ObjetType type,
            ObjetStatus status

    ) {
        this.lounge = lounge;
        this.user = user;
        this.name = name;
        this.imageUrl = imageUrl;
        this.explanation = explanation;
        this.type = type;
        this.status = status;
    }

    public ObjetCreateResponseDto toObjetCreateResponseDto() {
        return new ObjetCreateResponseDto(objetId);
    }

    public void updateUserObjets(List<ObjetSharer> objetSharers) {
        this.objetSharers = objetSharers;
    }

    public void updateDetails(String name, String description) {
        this.name = name;
        this.explanation = description;
    }

    public void updateDetailsWithImage(String name, String description, String imageUrl) {
        this.name = name;
        this.explanation = description;
        this.imageUrl = imageUrl;
    }

    public void updateStatus(ObjetStatus status) {
        this.status = status;
    }
}