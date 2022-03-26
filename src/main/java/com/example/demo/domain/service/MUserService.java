package com.example.demo.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.MUser;
import com.example.demo.domain.form.SignupForm;
import com.example.demo.domain.repository.MUserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * ユーザーマスービスクラス
 */
@Service
@Transactional
@Slf4j
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
		log.trace("{}", "ユーザー登録処理を開始します");
		
		MUser user = new MUser();

		// フォームをユーザーマスタにコピー
		BeanUtils.copyProperties(form, user);

		// パスワードの暗号化
		user.setPassword(encoder.encode(form.getPassword()));
		user.setPasswordConfirm(encoder.encode(form.getPasswordConfirm()));

		// ロールに"ROLE_GENERAL"を設定
		user.setRole("ROLE_GENERAL");
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		// 現在日時を設定
		user.setCreateDate(now);
		user.setUpdateDate(now);
		// 削除フラグ"0"を設定
		user.setDeleteFlg("0");

		// ユーザー登録
		repository.registMUser(user);
		
		log.trace("{}", "ユーザー登録処理が完了しました");
	}

	/**
	 * ユーザーマスタに登録済みのデータを取得する
	 * 
	 * @param email メールアドレス
	 * @return ユーザーマスタに登録済みのユーザー1件
	 */
	public MUser getAuthenticableUser(String email) {
		log.info(email + "に合致するユーザーの検索を開始します");
		// メールアドレスでユーザーを検索
		Optional<MUser> option = repository.getAuthUserOne(email);
		// OptionalをMUserに変換
		MUser user = option.orElse(null);
		
		log.info(email + "に合致するユーザーの検索が完了しました");
		
		return user;
	}
}
