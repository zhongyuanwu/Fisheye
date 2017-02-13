package com.iyuile.caelum.entity.response;

/**
 * 
 * @Description 响应的验证用户信息是否存在
 */
public class VerityUsersResponse {
	private String telephone;
	private String nickname;
	private String email;
	private String username;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
