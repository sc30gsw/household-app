package com.example.demo.domain.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.entity.MUser;

/**
 * ユーザーマスタマッパーインターフェース
 */
@Mapper
public interface MUserMapper {

	/**
	 * ユーザー登録処理
	 * 
	 * @param user ユーザーマスタ
	 */
	public int insertMUser(MUser user);
	
	/**
	 * メールアドレスでユーザーを検索する処理
	 * 
	 * @param email メールアドレス
	 * @return ユーザー1件
	 */
	public Optional<MUser> findByEmail(String email);
}
