package com.example.demo.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * ユーザーマスタエンティティクラス
 */
@Data
public class MUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**ユーザーID*/
	private Integer userId;
	
	/**ユーザー名*/
	private String username;
	
	/**メールアドレス*/
	private String email;
	
	/**パスワード*/
	private String password;
	
	/**パスワード確認用*/
	private String passwordConfirm;
	
	/**年齢*/
	private Integer age;
	
	/**性別*/
	private String sex;
	
	/**ロール*/
	private String role;
	
	/**作成日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime createDate;
	
	/**更新日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime updateDate;
	
	/**削除日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime deleteDate;
	
	/**削除フラグ*/
	private String deleteFlg;
}
