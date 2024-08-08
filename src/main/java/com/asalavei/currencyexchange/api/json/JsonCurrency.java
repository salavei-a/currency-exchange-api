package com.asalavei.currencyexchange.api.json;

/**
 * DTO representing currency data that can be serialized to and deserialized from JSON
 */
public class JsonCurrency extends BaseJsonDto<Integer> {
    private String code;
    private String fullName;
    private String sign;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * Creates a new {@link Builder} instance for constructing {@link JsonCurrency} instances
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instances of the {@link JsonCurrency}
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

        public JsonCurrency build() {
            JsonCurrency jsonCurrency = new JsonCurrency();

            jsonCurrency.setId(id);
            jsonCurrency.setCode(code);
            jsonCurrency.setFullName(fullName);
            jsonCurrency.setSign(sign);

            return jsonCurrency;
        }
    }
}
