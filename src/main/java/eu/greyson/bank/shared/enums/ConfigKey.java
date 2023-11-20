package eu.greyson.bank.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigKey {
    AUTH_USERNAME("bank.auth.username"),
    AUTH_PASSWORD("bank.auth.password");

    private final String value;
}
