package ru.bebriki.sstuconnect.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("ACTIVATE_ACCOUNT");


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
