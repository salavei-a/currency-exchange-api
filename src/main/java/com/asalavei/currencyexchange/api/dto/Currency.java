package com.asalavei.currencyexchange.api.dto;

/**
 * Immutable DTO for transferring currency data between business logic layers
 */
public class Currency implements BaseDto<Integer> {
    private final Integer id;
    private final String code;
    private final String fullName;
    private final String sign;

    public Currency(Integer id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }

    /**
     * Creates a new {@link Builder} instance for constructing {@link Currency} instances
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Currency.Builder();
    }

    /**
     * Builder for creating instances of the {@link Currency}
     */
    public static final class Builder {
        private Integer id;
        private String code;
        private String fullName;
        private String sign;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Currency build() {
            return new Currency(id, code, fullName, sign);
        }
    }
}
