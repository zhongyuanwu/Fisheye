package com.iyuile.caelum.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WangYao
 * @version 1
 * @Description 用户 实体
 */
public class UserEntity implements Serializable {

    /**
     * id : 2
     * nickname : wyjson
     * avatar : null
     * sex : 1
     * birthday : 2016-12-09 13:39:01
     * telephone : 13366664009
     * realname : 王尧
     */

    private Long id;
    private String nickname;
    private String avatar;
    private int sex;
    private String birthday;
    private String telephone;
    private String realname;

    public static UserEntity toJSONObjectFromData(String str) {

        return new Gson().fromJson(str, UserEntity.class);
    }

    public static List<UserEntity> toJSONArrayUserEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<UserEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
