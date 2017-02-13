package com.iyuile.caelum.entity.response;


import com.iyuile.caelum.entity.UserEntity;

import java.io.Serializable;

/**
 * @Description 响应 {@link UserEntity}
 */
public class UserResponse implements Serializable {

    private UserEntity data;

    public UserEntity getData() {
        return data;
    }

    public void setData(UserEntity data) {
        this.data = data;
    }

}
