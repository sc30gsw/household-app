package com.example.demo.domain.service;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.entity.MUser;
import com.example.demo.domain.form.SignupForm;
import com.example.demo.domain.repository.MUserRepository;

/**
 * ユーザーマスービスクラス
 */
@Service
public class MUserService {

	/**ユーザーマスタリポジトリクラス*/
	@Autowired
	MUserRepository repository;
	
	@Autowired
	PasswordEncoder encoder;
	
	/**
	 * ユーザー登録処理
	 * 
	 * @param form ユーザー登録用フォームクラス
	 */
	public void registMUserOne(SignupForm form) {
		MUser user = new MUser();
		
		// フォームをユーザーマスタにコピー
		BeanUtils.copyProperties(form, user);
		
		// パスワードの暗号化
		user.setPassword(encoder.encode(form.getPassword()));
		user.setPasswordConfirm(encoder.encode(form.getPasswordConfirm()));
		
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		// 現在日時を設定
		user.setCreateDate(now);
		user.setUpdateDate(now);
		// 削除フラグ"0"を設定
		user.setDeleteFlg("0");
		
		// ユーザー登録
		repository.registMUser(user);
	}
}
