package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * セキュリティの設定クラス
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**ユーザー認証サービス*/
	@Autowired
	private UserDetailsService service;
	
	/**
	 * パスワードを暗号化を行う
	 * 
	 * @return　ハッシュ化したパスワード
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	/**セキュリティの対象外を設定する*/
	public void configure(WebSecurity web) throws Exception {
		// セキュリティを適用しない
		web.ignoring()
				.antMatchers("/webjars/**")
				.antMatchers("/css/**")
				.antMatchers("/js/**");
	}
	
	@Override
	/**セキュリティの各種設定*/
	protected void configure(HttpSecurity http) throws Exception {
		// ログイン不要ページの設定
		http.authorizeRequests()
				// 直リンク可
				.antMatchers("/login").permitAll()
				.antMatchers("/signup").permitAll()
				// 上記以外は直リンク不可
				.anyRequest().authenticated();
		
		// ログイン処理の設定
		http.formLogin()
				// ログイン処理のパス
				.loginProcessingUrl("/login")
				// ログインページのリンク先設定
				.loginPage("/login")
				// ログイン失敗時の遷移先
				.failureUrl("/login?error")
				// ログインページのユーザーID
				.usernameParameter("email")
				// ログインページのパスワード
				.passwordParameter("password")
				// ログイン成功時の遷移先
				.defaultSuccessUrl("/household/index", true);
		
		// ログアウト処理の設定
		http.logout()
				// ログアウトのリクエスト先URL
				.logoutUrl("/logout")
				// ログアウト成功時の遷移先
				.logoutSuccessUrl("/login?logout");
			
	}
	
	/**
	 * ユーザーマスタの認証
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// パスワードのハッシュ化
		PasswordEncoder encoder = passwordEncoder();
		
		// ユーザーデータ(ユーザーマスタ)の認証
		auth
			.userDetailsService(service)
			.passwordEncoder(encoder);
	}

}
