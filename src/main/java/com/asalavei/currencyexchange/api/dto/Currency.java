package com.asalavei.currencyexchange.api.dto;

/**
 * DTO for currency data
 */
public class Currency extends BaseDto<Integer> {
    private final String name;
    private final String code;
    private final String sign;

    public Currency(Integer id, String name, String code, String sign) {
        super(id);
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
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
        private String name;
        private String code;
        private String sign;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Currency build() {
            return new Currency(id, name, code, sign);
        }
    }
}
