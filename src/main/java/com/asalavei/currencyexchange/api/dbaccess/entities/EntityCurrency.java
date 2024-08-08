package com.asalavei.currencyexchange.api.dbaccess.entities;

/**
 * Entity representing currency data stored in the database
 */
public class EntityCurrency {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
     * Creates a new {@link Builder} instance for constructing {@link EntityCurrency} instances
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instances of the {@link EntityCurrency}
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

        public EntityCurrency build() {
            EntityCurrency entityCurrency = new EntityCurrency();

            entityCurrency.setId(id);
            entityCurrency.setCode(code);
            entityCurrency.setFullName(fullName);
            entityCurrency.setSign(sign);

            return entityCurrency;
        }
    }
}
