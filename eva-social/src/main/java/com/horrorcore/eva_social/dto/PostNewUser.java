package com.horrorcore.eva_social.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostNewUser {
	@Email(message = "Please provide a valid email address")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password should be at least 8 characters")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
		message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
	)
	private String password;
}
