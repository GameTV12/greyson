package eu.greyson.bank.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.greyson.bank.shared.enums.ConfigKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Configuration {

    @Getter
    @NotNull
    private final ConfigKey key;

    @NotBlank
    private final String value;

    @JsonIgnore
    public Integer getValueAsInteger() {
        return Integer.valueOf(value.trim());
    }

    @JsonIgnore
    public Long getValueAsLong() {
        return Long.valueOf(value.trim());
    }

    @JsonIgnore
    public Short getValueAsShort() {
        return Short.valueOf(value.trim());
    }

    @JsonIgnore
    public Byte getValueAsByte() {
        return Byte.valueOf(value.trim());
    }

    @JsonIgnore
    public String getValueAsString() {
        return value;
    }

}
