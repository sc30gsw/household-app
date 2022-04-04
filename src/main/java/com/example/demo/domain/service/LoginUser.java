package com.example.demo.domain.service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.domain.entity.MUser;

/**
 * 認証したユーザーをインスタンス化して扱えるサービスクラス
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {

	// ユーザーマスタエンティティ
	private MUser user;
	
	/**
	 * ユーザーマスタを検索し、SpringSecurityで使用するユーザー認証情報のインスタンスを作成する処理
	 * 
	 * @param user ユーザーマスタエンティティ
	 */
	public LoginUser(MUser user) {
		// org.springframework.security.core.userdetails.Userのコンストラクタ
		super(user.getEmail(), user.getPassword(), convertGrantedAuthorities(user.getRole()));
		this.user = user;
	}
	
	/**
	 * ユーザーマスタのデータを取得する
	 * 
	 * @return ユーザーマスタ
	 */
	public MUser getUser() {
		return user;
	}
	
	/**
	 * カンマ区切りのロールをSimpleGrantedAuthorityのコレクションに変換する
	 * 
	 * @param role ロール
	 * @return SimpleGrantedAuthorityのコレクション
	 */
	static Set<GrantedAuthority> convertGrantedAuthorities(String role) {
		// roleが存在しない場合
		if (role == null || role.isEmpty()) {
			// 空セットを返す
			return Collections.emptySet();
		}
		
		// ロールをカンマ区切りにし、SimleGrantedAuthorityのコレクションに変換
		Set<GrantedAuthority> authorities = Stream.of(role.split(","))
				// SimpleGrantedAuthorityクラスのインスタンスに変換
				.map(SimpleGrantedAuthority::new)
				// Setコレクションにする
				.collect(Collectors.toSet());
		
		return authorities;
	}
}
