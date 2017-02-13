package com.iyuile.caelum.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @Description 响应的错误返回信息
 */
public class ErrorResponse {

    private String message;
    private ErrorModel errors;
    @SerializedName(alternate = {"status_code"}, value = "code")
    private int code;

    public static class ErrorModel {
        private String[] telephone;
        private String[] nickname;
        private String[] password;
        private String[] password_confirmation;
        private String[] verity_code;
        //购物车
        private String[] model_id;

        public String[] getTelephone() {
            return telephone;
        }

        public void setTelephone(String[] telephone) {
            this.telephone = telephone;
        }

        public String[] getNickname() {
            return nickname;
        }

        public void setNickname(String[] nickname) {
            this.nickname = nickname;
        }

        public String[] getPassword() {
            return password;
        }

        public void setPassword(String[] password) {
            this.password = password;
        }

        public String[] getPassword_confirmation() {
            return password_confirmation;
        }

        public void setPassword_confirmation(String[] password_confirmation) {
            this.password_confirmation = password_confirmation;
        }

        public String[] getVerity_code() {
            return verity_code;
        }

        public void setVerity_code(String[] verity_code) {
            this.verity_code = verity_code;
        }

        public String[] getModel_id() {
            return model_id;
        }

        public void setModel_id(String[] model_id) {
            this.model_id = model_id;
        }
    }

    ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorModel getErrors() {
        return errors;
    }

    public void setErrors(ErrorModel errors) {
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
