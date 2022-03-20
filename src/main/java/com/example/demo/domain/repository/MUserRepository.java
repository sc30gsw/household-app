package com.example.demo.domain.repository;

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
}
