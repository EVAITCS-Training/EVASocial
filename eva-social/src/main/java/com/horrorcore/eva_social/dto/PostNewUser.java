package com.horrorcore.eva_social.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostNewUser {
	private String email;
	private String password;
}
