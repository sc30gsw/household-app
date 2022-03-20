package com.example.demo.domain.mapper;

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
}
