package main.dto.api.request;

import lombok.Data;

@Data
public class ProfileRequest {
	private String name;
	private String email;
	private String password;
	private Integer removePhoto;
	private String photo;
}
