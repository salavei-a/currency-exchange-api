package com.asalavei.currencyexchange.api.json;

/**
 * JSON DTO for currency data
 */
public class JsonCurrency extends BaseJsonDto<Integer> {

    private String name;
    private String code;
    private String sign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

        public JsonCurrency build() {
            JsonCurrency jsonCurrency = new JsonCurrency();

            jsonCurrency.setId(id);
            jsonCurrency.setName(name);
            jsonCurrency.setCode(code);
            jsonCurrency.setSign(sign);

            return jsonCurrency;
        }
    }
}
