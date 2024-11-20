package com.example.daobe.notification.domain;

import static com.example.daobe.notification.exception.NotificationExceptionType.DOMAIN_INFO_IS_NULL;

import com.example.daobe.notification.exception.NotificationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationDomainInfo {

    @Column(name = "domain_id")
    private Long domainId;

    @Column(name = "domain_name")
    private String domainName;

    public NotificationDomainInfo(Long domainId, String domainName) {
        validate(domainId, domainName);
        this.domainId = domainId;
        this.domainName = domainName;
    }

    private void validate(Long domainId, String domainName) {
        if (domainId == null) {
            throw new NotificationException(DOMAIN_INFO_IS_NULL);
        }

        if (domainName == null) {
            throw new NotificationException(DOMAIN_INFO_IS_NULL);
        }
    }

    public void updateDomainName(String domainName) {
        this.domainName = domainName;
    }
}
