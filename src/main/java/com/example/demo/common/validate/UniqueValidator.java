package com.example.demo.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.domain.entity.MUser;
import com.example.demo.domain.mapper.MUserMapper;

/**
 * 一意性バリデーションクラス
 */
public class UniqueValidator implements ConstraintValidator<Unique, String> {

	/**ユーザーマスタマッパー*/
	@Autowired
	MUserMapper mapper;

	public void initialize(Unique constraintAnnotation) {
	}
	
	/**
	 * メールアドレスをキーに一致するデータがあるかを検索する
	 * 
	 * @param value 入力値
	 * true: 一致する値が存在しない / false: 一致する値が存在する
	 */
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// メールアドレスでユーザーマスタを検索する
		MUser user = mapper.findByEmail(value).orElse(null);
		if (user == null) {
			return true;
		}

		return false;
	}
}
