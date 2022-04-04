package com.example.demo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.mapper.MUserMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * ユーザー認証サービスクラス
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

	/**ユーザーマスタマッパー*/
	@Autowired
	private MUserMapper mapper;
	
	
	/**
	 * メールアドレスで検索したユーザーマスタのデータをSimpleLoginUserクラスのインスタンスに変換する
	 */
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		assert(email != null);
		log.debug("loadByUsername(email):[{}]", email);
		return mapper.findByEmail(email)
						// SimpleLoginUserクラスのインスタンスに変換
						.map(LoginUser::new)
						// 変換できなければ例外をスロー
						.orElseThrow(() -> new UsernameNotFoundException("User not found by email:[" + email + "]"));
	}
}
