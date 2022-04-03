package com.example.demo.domain.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.MHousehold.controller.MHouseholdCondition;
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
		if (household.getActiveDate() == null) {
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

	/**
	 * 月次家計簿検索処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> searchMonthlyHousehold(MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "月次家計簿検索処理を開始ます");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId(); 
		// 検索条件を設定する
		conditionSetting(condition, userId);
		
		log.trace("{}", "月次家計簿検索処理が完了しました");

		return repository.monthlyGetHousehold(condition);
	}
	
	/**
	 * 家計簿集計取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタ
	 */
	public MHousehold getSumMonthlyHousehold(MHouseholdCondition condition, @AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "月次家計簿集計取得処理を開始ます");

		//ユーザーIDの取得
		val userId = loginUser.getUser().getUserId(); 
		// 検索条件を設定する
		conditionSetting(condition, userId);
		
		log.trace("{}", "月次家計簿集計取得処理が完了しました");
		
		return repository.monthlyGetSumHousehold(condition);
	}

	/**
	 * 家計簿検索条件の設定を行う処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param userId ユーザーID
	 * @return 家計簿検索条件
	 */
	private MHouseholdCondition conditionSetting(MHouseholdCondition condition, Integer userId) {
		// 日付のフォーマットを行う
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		val startDate = sdf.format(getStartDate());
		val endDate = sdf.format(getEndDate());

		// 検索条件に値を設定する
		condition.setStartDate(startDate);
		condition.setEndDate(endDate);
		condition.setUserId(userId);

		return condition;
	}

	/**
	 * @return 月初の日付を取得する処理
	 */
	public Date getStartDate() {
		// Calendarクラスのインスタンスを取得
		Calendar cl = Calendar.getInstance();
		// 月の初日を取得する
		val firstDay = cl.getActualMinimum(Calendar.DATE);
		// Calendarに値を設定する
		cl.set(Calendar.DATE, firstDay);
		cl.set(Calendar.HOUR_OF_DAY, 00);
		cl.set(Calendar.MINUTE, 00);
		cl.set(Calendar.SECOND, 00);
		cl.set(Calendar.MILLISECOND, 00);

		return cl.getTime();
	}

	/**
	 * @return 月末の日付を取得する処理
	 */
	public Date getEndDate() {
		// Calendarクラスのインスタンスを取得
		Calendar cl = Calendar.getInstance();
		// 月の最終日を取得する
		val lastDay = cl.getActualMaximum(Calendar.DATE);
		// Calendarに値を設定する
		cl.set(Calendar.DATE, lastDay);
		cl.set(Calendar.HOUR_OF_DAY, 00);
		cl.set(Calendar.MINUTE, 00);
		cl.set(Calendar.SECOND, 00);
		cl.set(Calendar.MILLISECOND, 00);

		return cl.getTime();
	}
}
