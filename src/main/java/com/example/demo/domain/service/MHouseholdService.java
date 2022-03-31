package com.example.demo.domain.service;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.MHousehold;
import com.example.demo.domain.form.EasyHouseholdForm;
import com.example.demo.domain.repository.MHouseholdRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 家計簿マスタサービスクラス
 */
@Service
@Transactional
@Slf4j
public class MHouseholdService {

	/**家計簿マスタリポジトリクラス*/
	@Autowired
	MHouseholdRepository repository;
	
	/**
	 * 家計簿カンタン入力処理
	 * 
	 * @param form 家計簿カンタン入力用フォーム
	 */
	public void easyInputMHousehold(EasyHouseholdForm form, @AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "家計簿カンタン入力処理を開始します");
		
		MHousehold household = new MHousehold();
		// ログインユーザーを取得
		val user = loginUser.getUser();
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		
		// フォームを家計簿マスタにコピー
		BeanUtils.copyProperties(form, household);
		
		// 出金日がnullの場合、今日の日付を設定する
		if(household.getActiveDate() == null) {
			long miliseconds = System.currentTimeMillis();
	        Date date = new Date(miliseconds);
			
	        // 出金日を設定
			household.setActiveDate(date);
		}
		
		// ユーザーIDを設定
		household.setUserId(user.getUserId());
		// 入金額を設定
		household.setDeposit(0);
		// 作成日時を設定
		household.setCreateDate(now);
		// 更新日時を設定
		household.setUpdateDate(now);
		
		// 家計簿登録処理を呼び出し
		repository.registMHousehold(household);
		log.info("{}", "家計簿登録処理を呼び出し");
		
		log.trace("{}", "家計簿カンタン入力処理が完了しました");
	}
}
