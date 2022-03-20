package com.example.demo.domain.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.entity.MUser;
import com.example.demo.domain.mapper.MUserMapper;

/**
 * ユーザーマスタリポジトリクラス
 */
@Repository
public class MUserRepository {

	/**ユーザーマスタマッパー*/
	@Autowired
	MUserMapper mapper;
	
	/**
	 * ユーザーマスタ登録処理
	 * 
	 * @param user ユーザーマスタ
	 */
	public void registMUser(MUser user) {
		mapper.insertMUser(user);
	}
	
	/**
	 * ユーザーマスタに登録済みのデータを取得する
	 * 
	 * @param email メールアドレス
	 * @return ユーザーマスタに登録済みのユーザー1件
	 */
	public Optional<MUser> getAuthUserOne(String email){
		return mapper.findByEmail(email);
	}
}
