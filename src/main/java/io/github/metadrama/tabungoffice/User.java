package io.github.metadrama.tabungoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	

	@Id
	@GeneratedValue
	private Long id;
	public String displayName;

	private String email;
	private String phone;
	private String password;

	public Long getId() {
		return id;
	};

	public String getPhone() {

		return phone;
	};

	public void setPhone(String phone) {
		this.phone = phone;
	};

	public String getEmail() {

		return email;
	};

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
