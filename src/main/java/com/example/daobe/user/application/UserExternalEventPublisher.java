package com.example.daobe.user.application;

import com.example.daobe.user.domain.event.UserPokeEvent;

public interface UserExternalEventPublisher {

    void execute(UserPokeEvent payload);
}
