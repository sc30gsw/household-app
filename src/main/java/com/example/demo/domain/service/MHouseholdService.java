package com.example.demo.domain.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.MHousehold.controller.MHouseholdCondition;
import com.example.demo.common.constants.CategoryConstants;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.domain.entity.MHousehold;
import com.example.demo.domain.form.DeleteHouseholdForm;
import com.example.demo.domain.form.DetailHouseholdConditionForm;
import com.example.demo.domain.form.EasyHouseholdForm;
import com.example.demo.domain.form.ModalDepositForm;
import com.example.demo.domain.form.UpdateHouseholdForm;
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
	public void easyInputMHousehold(EasyHouseholdForm form, @AuthenticationPrincipal LoginUser loginUser) throws Exception {
		log.trace("{}", "家計簿カンタン入力処理を開始します");

		MHousehold household = new MHousehold();
		// ログインユーザーを取得
		val user = loginUser.getUser();
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();

		// フォームを家計簿マスタにコピー
		BeanUtils.copyProperties(form, household);

		// フォームの値を数値に変換して設定
		household.setPayment(Integer.parseInt(form.getPayment()));

		// 支出金額が0以下の場合
		if (0 >= household.getPayment()) {
			throw new BusinessException("金額の登録時にエラーが発生しました");
		}

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
	 * 月次家計簿集計取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタ
	 */
	public MHousehold getSumMonthlyHousehold(MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "月次家計簿集計取得処理を開始ます");

		//ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();
		// 検索条件を設定する
		conditionSetting(condition, userId);

		log.trace("{}", "月次家計簿集計取得処理が完了しました");

		return repository.monthlyGetSumHousehold(condition);
	}

	/**
	 * 最近の家計簿リスト検索
	 * 
	 * @param loginUser ログインユーザー
	 * @return　最近の家計簿リスト
	 */
	public List<MHousehold> searchLatestHouseholdList(@AuthenticationPrincipal LoginUser loginUser) {
		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();

		log.trace("{}", "最近の家計簿リストの検索を開始ます");
		val latestHouseholdList = repository.getManyLatestHousehold(userId);

		// カテゴリーコード値の設定を行う
		settingCategoryCode(latestHouseholdList);
		log.trace("{}", "最近の家計簿リストの検索が完了しました");

		return latestHouseholdList;
	}

	/**
	 * 月次家計簿リスト取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 月次家計簿マスタリスト
	 */
	public List<MHousehold> getMonthlyHouseholdList(MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "月次家計簿リスト取得処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();
		// 検索条件を設定する
		conditionSetting(condition, userId);

		// 月次家計簿リストを取得
		val monthlyHouseholdList = repository.monthlyGetHouseholdList(condition);

		// カテゴリーコードを設定する
		settingCategoryCode(monthlyHouseholdList);

		log.trace("{}", "月次家計簿リスト取得処理が完了しました");

		return monthlyHouseholdList;
	}

	/**
	 * 月次家計簿集計検索処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタ
	 */
	public MHousehold getSearchMonthlySumHousehold(DetailHouseholdConditionForm form, MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "月次家計簿集計検索処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();

		// フォームを検索条件にコピー
		BeanUtils.copyProperties(form, condition);
		// 検索条件にユーザーIDを設定
		condition.setUserId(userId);

		// 月次家計簿集計を取得
		val monthlySearchSumHousehold = repository.monthlyGetSumHousehold(condition);

		log.trace("{}", "月次家計簿集計検索処理が完了しました");

		return monthlySearchSumHousehold;
	}

	/**
	 * 月次家計簿リスト検索処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 月次家計簿マスタリスト
	 */
	public List<MHousehold> getSearchMonthlyHouseholdList(DetailHouseholdConditionForm form,
			MHouseholdCondition condition, @AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "月次家計簿リスト検索処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();

		// フォームを検索条件にコピー
		BeanUtils.copyProperties(form, condition);
		// 検索条件にユーザーIDを設定
		condition.setUserId(userId);

		// 月次家計簿リストを取得
		val monthlySearchHouseholdList = repository.monthlyGetHouseholdList(condition);

		// カテゴリーコードを設定する
		settingCategoryCode(monthlySearchHouseholdList);

		log.trace("{}", "月次家計簿リスト検索処理が完了しました");

		return monthlySearchHouseholdList;
	}

	/**
	 * カテゴリー別支出内訳集計リスト取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> getMonthlySumCategoryPayment(MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "カテゴリー別支出内訳集計リスト取得処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();
		// 検索条件を設定する
		conditionSetting(condition, userId);

		// カテゴリー別支出内訳集計リストを取得
		val monthlySumCategoryPayList = repository.monthlySumGetHouseholdCategoryList(condition);

		// カテゴリーコードを設定する
		settingCategoryCode(monthlySumCategoryPayList);

		log.trace("{}", "カテゴリー別支出内訳集計リスト取得処理が完了しました");

		return monthlySumCategoryPayList;
	}

	/**
	 * カテゴリー別支出内訳集計リスト検索処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> searchMonthlySumCategoryPayment(DetailHouseholdConditionForm form,
			MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "カテゴリー別支出内訳集計リスト検索処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();

		// フォームを検索条件にコピー
		BeanUtils.copyProperties(form, condition);
		// 検索条件にユーザーIDを設定
		condition.setUserId(userId);

		// カテゴリー別支出内訳リストを取得
		val monthlySumCategoryPayList = repository.monthlySumGetHouseholdCategoryList(condition);

		// カテゴリーコードを設定する
		settingCategoryCode(monthlySumCategoryPayList);

		log.trace("{}", "カテゴリー別支出内訳集計リスト検索処理が完了しました");

		return monthlySumCategoryPayList;
	}

	/**
	 * 月次カテゴリー別支出内訳リスト取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> getMonthlyCategoryPayment(MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "月次カテゴリー別支出内訳リスト取得処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();
		// 検索条件を設定する
		conditionSetting(condition, userId);

		// カテゴリー別支出内訳リストを取得
		val monthlyCategoryPayList = repository.monthlyGetHouseholdCategoryList(condition);

		log.trace("{}", "月次カテゴリー別支出内訳リスト取得処理が完了しました");

		return monthlyCategoryPayList;
	}

	/**
	 * 月次カテゴリー別支出内訳リスト検索処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> searchMonthlyCategoryPayment(DetailHouseholdConditionForm form,
			MHouseholdCondition condition, @AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "月次カテゴリー別支出内訳リスト検索処理を開始します");

		// ユーザーIDの取得
		val userId = loginUser.getUser().getUserId();

		// フォームを検索条件にコピー
		BeanUtils.copyProperties(form, condition);
		// 検索条件にユーザーIDを設定
		condition.setUserId(userId);

		// カテゴリー別支出内訳リストを取得
		val monthlyCategoryPayList = repository.monthlyGetHouseholdCategoryList(condition);

		log.trace("{}", "月次カテゴリー別支出内訳リスト検索処理が完了しました");

		return monthlyCategoryPayList;
	}

	/**
	 * 家計簿カンタン収入登録処理
	 * 
	 * @param form モーダルウィンドウ用家計簿収入金額フォーム
	 * @param loginUser ログインユーザー
	 */
	public void registDepositHousehold(ModalDepositForm form, @AuthenticationPrincipal LoginUser loginUser) {
		log.trace("{}", "家計簿カンタン収入登録処理を開始します");

		MHousehold household = new MHousehold();
		// ログインユーザーを取得
		val user = loginUser.getUser();
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();

		// フォームを家計簿マスタにコピー
		BeanUtils.copyProperties(form, household);

		// フォームの値を数値に変換して設定
		household.setDeposit(Integer.parseInt(form.getDeposit()));

		// 出金日がnullの場合、今日の日付を設定する
		if (household.getActiveDate() == null) {
			long miliseconds = System.currentTimeMillis();
			Date date = new Date(miliseconds);

			// 出金日を設定
			household.setActiveDate(date);
		}

		// ユーザーIDを設定
		household.setUserId(user.getUserId());
		// 出金額を設定
		household.setPayment(0);
		// 作成日時を設定
		household.setCreateDate(now);
		// 更新日時を設定
		household.setUpdateDate(now);

		// 家計簿登録処理を呼び出し
		repository.registMHousehold(household);
		log.info("{}", "家計簿登録処理を呼び出し");

		log.trace("{}", "家計簿カンタン収入登録処理が完了しました");
	}

	/**
	 * 家計簿更新処理
	 * 
	 * @param form 家計簿更新フォーム
	 * @throws Exception
	 */
	public void updateInputMHousehold(UpdateHouseholdForm form) throws Exception {
		log.trace("{}", "家計簿更新処理を開始します");

		MHousehold household = new MHousehold();

		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();

		// フォームを家計簿マスタにコピー
		BeanUtils.copyProperties(form, household);

		// フォームの値を数値に変換して設定
		household.setPayment(Integer.parseInt(form.getPayment()));
		household.setDeposit(Integer.parseInt(form.getDeposit()));

		// 支出金額と収入金額の両方が0以下の場合
		if (0 >= household.getPayment() && 0 >= household.getDeposit()) {
			throw new BusinessException("支出金額・収入金額の更新時にエラーが発生しました");
		}

		// 出金日がnullの場合、今日の日付を設定する
		if (household.getActiveDate() == null) {
			long miliseconds = System.currentTimeMillis();
			Date date = new Date(miliseconds);

			// 出金日を設定
			household.setActiveDate(date);
		}

		// 更新日時を設定
		household.setUpdateDate(now);

		// 家計簿更新処理を呼び出し
		log.info("{}", "家計簿更新処理を呼び出し");
		repository.updateOneMHousehold(household);
		log.trace("{}", "家計簿更新処理が完了しました");
	}

	/**
	 * 家計簿削除処理
	 * 
	 * @param form 家計簿削除フォーム
	 */
	public void deleteInputMHouhosed(DeleteHouseholdForm form) {
		log.trace("{}", "家計簿削除処理を開始します");

		log.info("{}", "家計簿削除処理を呼び出し");
		repository.deleteOneMHousehold(form.getHouseholdId());
		log.trace("{}", "家計簿削除処理が完了しました");
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

	/**
	 * カテゴリーコード値の設定を行う処理
	 * 
	 * @param latestList 最近の家計簿リスト
	 */
	private void settingCategoryCode(List<MHousehold> householdList) {

		householdList.stream().forEach(list -> {
			// カテゴリーを取得
			val category = list.getCategory();

			// カテゴリーコードに応じてカテゴリーの共通定数を設定する
			switch (category.getCategoryCode()) {
			case "1":
				category.setCategoryCode(CategoryConstants.FOOD_EXPENSE);
				break;

			case "2":
				category.setCategoryCode(CategoryConstants.COMMODITY);
				break;

			case "3":
				category.setCategoryCode(CategoryConstants.HOBBIES);
				break;

			case "4":
				category.setCategoryCode(CategoryConstants.SOCIAL_EXPENSE);
				break;

			case "5":
				category.setCategoryCode(CategoryConstants.TRANSPORTATION_EXPENSE);
				break;

			case "6":
				category.setCategoryCode(CategoryConstants.FASSHON);
				break;

			case "7":
				category.setCategoryCode(CategoryConstants.CAR);
				break;

			case "8":
				category.setCategoryCode(CategoryConstants.HEALTH_CARE);
				break;

			case "9":
				category.setCategoryCode(CategoryConstants.LIBERAL_ARTS);
				break;

			case "10":
				category.setCategoryCode(CategoryConstants.SPECIAL_EXPENSE);
				break;

			case "11":
				category.setCategoryCode(CategoryConstants.UTILITIES_BILLS);
				break;

			case "12":
				category.setCategoryCode(CategoryConstants.COMMUNICATION_COST);
				break;

			case "13":
				category.setCategoryCode(CategoryConstants.HOUSEING_COST);
				break;

			case "14":
				category.setCategoryCode(CategoryConstants.OTHERS_COST);
				break;

			case "15":
				category.setCategoryCode(CategoryConstants.UNSORTED);
				break;

			default:
				category.setCategoryCode(CategoryConstants.UNSORTED);
				break;
			}
		});
	}

	/**
	 * 家計簿マスタリストから食費項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 食費リスト
	 */
	public List<MHousehold> filterFoodCategory(List<MHousehold> householdList) {
		val foodList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("1") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.FOOD_EXPENSE))
				.collect(Collectors.toList());

		return foodList;
	}

	/**
	 * 家計簿マスタリストから日用品項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 日用品リスト
	 */
	public List<MHousehold> filterCommodityCategory(List<MHousehold> householdList) {
		val comodityList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("2") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.COMMODITY))
				.collect(Collectors.toList());

		return comodityList;
	}

	/**
	 * 家計簿マスタリストから趣味・娯楽項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 趣味・娯楽リスト
	 */
	public List<MHousehold> filterHobbyCategory(List<MHousehold> householdList) {
		val hobbyList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("3") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.HOBBIES))
				.collect(Collectors.toList());

		return hobbyList;
	}

	/**
	 * 家計簿マスタリストから交際費項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 交際費リスト
	 */
	public List<MHousehold> filterSocialExpenceCategory(List<MHousehold> householdList) {
		val socialExList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("4") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.SOCIAL_EXPENSE))
				.collect(Collectors.toList());

		return socialExList;
	}

	/**
	 * 家計簿マスタリストから交通費項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 交通費リスト
	 */
	public List<MHousehold> filterTransportCategory(List<MHousehold> householdList) {
		val transportList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("5") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.TRANSPORTATION_EXPENSE))
				.collect(Collectors.toList());

		return transportList;
	}

	/**
	 * 家計簿マスタリストから衣服・美容項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 衣服・美容リスト
	 */
	public List<MHousehold> filterFasshonCategory(List<MHousehold> householdList) {
		val fasshonList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("6") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.FASSHON))
				.collect(Collectors.toList());

		return fasshonList;
	}

	/**
	 * 家計簿マスタリストから自動車項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 自動車リスト
	 */
	public List<MHousehold> filterCarCategory(List<MHousehold> householdList) {
		val carList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("7") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.CAR))
				.collect(Collectors.toList());

		return carList;
	}

	/**
	 * 家計簿マスタリストから健康・医療項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 健康・医療リスト
	 */
	public List<MHousehold> filterHealthCategory(List<MHousehold> householdList) {
		val healthList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("8") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.HEALTH_CARE))
				.collect(Collectors.toList());

		return healthList;
	}

	/**
	 * 家計簿マスタリストから教養・教育項目を抽出する処理
	 * @param householdList 家計簿マスタリスト
	 * @return 教養・教育リスト
	 */
	public List<MHousehold> filterLiberalCategory(List<MHousehold> householdList) {
		val liberalList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("9") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.LIBERAL_ARTS))
				.collect(Collectors.toList());

		return liberalList;
	}

	/**
	 * 家計簿マスタリストから特別な支出項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 特別な支出リスト
	 */
	public List<MHousehold> filterSpecialExpenceCategory(List<MHousehold> householdList) {
		val specialExList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("10") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.SPECIAL_EXPENSE))
				.collect(Collectors.toList());

		return specialExList;
	}

	/**
	 * 家計簿マスタリストから水道・光熱費項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 水道・光熱費リスト
	 */
	public List<MHousehold> filterUtilitiesCategory(List<MHousehold> householdList) {
		val utilityList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("11") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.UTILITIES_BILLS))
				.collect(Collectors.toList());

		return utilityList;
	}

	/**
	 * 家計簿マスタリストから通信費項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 通信費リスト
	 */
	public List<MHousehold> filterCommunicationCostCategory(List<MHousehold> householdList) {
		val communicationCostList = householdList.stream()
				.filter(list -> list.getCategory().getCategoryCode().equals("12") ||
						list.getCategory().getCategoryCode().equals(CategoryConstants.COMMUNICATION_COST))
				.collect(Collectors.toList());

		return communicationCostList;
	}

	/**
	 * 家計簿マスタリストから住宅項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 住宅リスト
	 */
	public List<MHousehold> filterHouseCostCategory(List<MHousehold> householdList) {
		val houseCostList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("13") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.HOUSEING_COST))
				.collect(Collectors.toList());

		return houseCostList;
	}

	/**
	 * 家計簿マスタリストからその他項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return その他リスト
	 */
	public List<MHousehold> filterOthersCategory(List<MHousehold> householdList) {
		val otherList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("14") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.OTHERS_COST))
				.collect(Collectors.toList());

		return otherList;
	}

	/**
	 * 家計簿マスタリストから未分類項目を抽出する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @return 未分類リスト
	 */
	public List<MHousehold> filterUnsortedCategory(List<MHousehold> householdList) {
		val unsortedList = householdList.stream().filter(list -> list.getCategory().getCategoryCode().equals("15") ||
				list.getCategory().getCategoryCode().equals(CategoryConstants.UNSORTED))
				.collect(Collectors.toList());

		return unsortedList;
	}
}