package com.example.demo.domain.form;

import java.io.Serializable;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.demo.common.validate.Unique;

import lombok.Data;

/**
 * ユーザー登録用フォームクラス
 */
@Data
public class SignupForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**ユーザー名*/
	@NotBlank
	@Length(min = 4, max = 30)
	private String username;
	
	/**メールアドレス*/
	@NotBlank
	@Email
	@Unique
	private String email;
	
	/**パスワード*/
	@NotBlank
	@Length(min = 8, max = 24)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String password;
	
	/**パスワード確認用*/
	@NotBlank
	@Length(min = 8, max = 24)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String passwordConfirm;
	
	/**年齢*/
	@Min(value = 0)
	private Integer age;
	
	/**性別*/
	private String sex;
	
	/**
	 * パスワードとパスワード(確認用)が一致するかをチェックする
	 * true(一致) / false(不一致)
	 */
	@AssertTrue(message = "パスワードとパスワード(確認用)が一致していません")
	public boolean isPasswordValid() {
		// パスワードが存在しない場合
		if (password == null || password.isEmpty()) {
			return true;
		}
		
		return password.equals(passwordConfirm);
	}

}
