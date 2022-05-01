package com.example.demo.app.MHousehold.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.entity.MHousehold;
import com.example.demo.domain.form.DeleteHouseholdForm;
import com.example.demo.domain.form.DetailHouseholdConditionForm;
import com.example.demo.domain.form.EasyHouseholdForm;
import com.example.demo.domain.form.ModalDepositForm;
import com.example.demo.domain.form.UpdateHouseholdForm;
import com.example.demo.domain.service.LoginUser;
import com.example.demo.domain.service.MHouseholdService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 家計簿マスタコントローラークラス
 */
@Controller
@RequestMapping("/household")
@Slf4j
public class MHouseholdController {

	/**家計簿マスタサービスクラス*/
	@Autowired
	MHouseholdService service;

	/**
	 * ホーム画面に遷移する処理
	 * 
	 * @param form カンタン入力用フォーム
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return　household/index
	 */
	@GetMapping("/index")
	public String getIndex(EasyHouseholdForm form, Model model, MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.trace("{}", "月次家計簿集計取得処理の呼び出しを開始します");
		val monthlySumHousehold = service.getSumMonthlyHousehold(condition, loginUser);
		log.trace("{}", "月次家計簿集計取得処理の呼び出しが完了しました");

		// 家計簿集計がnullでない場合
		if (monthlySumHousehold != null) {
			// 月次集計上の収入
			val monthlyDeposit = monthlySumHousehold.getDeposit();
			// 月次集計上の支出
			val monthlyPayment = monthlySumHousehold.getPayment();
			// 月次集計の合計
			val monthlySum = monthlyDeposit - monthlyPayment;

			// 通貨形式のフォーマットの取得
			NumberFormat curFormat = NumberFormat.getCurrencyInstance();
			// それぞれの値を通貨形式のフォーマットに変更
			val monthlyFormatDeposit = curFormat.format(monthlyDeposit);
			val monthlyFormatPayment = curFormat.format(monthlyPayment);
			val monthlyFormatSum = curFormat.format(monthlySum);

			// 円グラフに登録する(JavaScriptに渡す)データをModelに登録
			model.addAttribute("depositJs", monthlyDeposit);
			model.addAttribute("paymentJs", monthlyPayment);

			// HTMLに渡すデータをModelに登録
			model.addAttribute("deposit", monthlyFormatDeposit);
			model.addAttribute("payment", monthlyFormatPayment);
			model.addAttribute("monthlySum", monthlySum);
			model.addAttribute("monthlyFormatSum", monthlyFormatSum);
		}

		// 月の初日と月末日を取得しフォーマットする
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		val startDate = sdf.format(service.getStartDate());
		val endDate = sdf.format(service.getEndDate());

		// 月の初日と月末日をModelに登録
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);

		// カンタン入力用フォームをModelに登録
		model.addAttribute("form", form);

		log.trace("{}", "最近の家計簿リスト検索の呼び出しを開始します");
		val latestHouseholdResult = service.searchLatestHouseholdList(loginUser);
		log.trace("{}", "最近の家計簿リスト検索の呼び出しが完了しました");

		// 最近の家計簿リストをModelに登録
		model.addAttribute("latestHouseholdList", latestHouseholdResult);

		return "household/index";
	}

	/**
	 *  家計簿詳細画面に遷移する処理
	 *  
	 * @param form 家計簿詳細検索条件フォーム 
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/householdDetail
	 */
	@GetMapping("/detail")
	public String getHouseholdDetail(DetailHouseholdConditionForm form, Model model, MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		// 月次家計簿集計取得処理の呼び出し
		log.trace("{}", "月次家計簿集計取得処理の呼び出しを開始します");
		val monthlySumHousehold = service.getSumMonthlyHousehold(condition, loginUser);
		log.trace("{}", "月次家計簿集計取得処理の呼び出しが完了しました");
		// 月次家計簿集計がnullでない場合
		if (monthlySumHousehold != null) {
			// 月次家計簿集計の合計を算出
			val monthlyHouseholdCalculated = householdSumCalc(monthlySumHousehold);
			// 月次家計簿集計の合計をModelに登録
			model.addAttribute("monthlyHouseholdCalculated", monthlyHouseholdCalculated);
		}

		// 月次家計簿リスト取得処理の呼び出し
		log.trace("{}", "月次家計簿リスト取得処理の呼び出しを開始します");
		val monthlyHouseholdList = service.getMonthlyHouseholdList(condition, loginUser);
		log.trace("{}", "月次家計簿リスト取得処理の呼び出しが完了しました");

		// カテゴリーコードとサブカテゴリー名のリストを作成
		List<String> categoryCodeList = new ArrayList<>();
		List<String> subCategoryNameList = new ArrayList<>();

		// 月次家計簿からカテゴリーコードとサブカテゴリーコードを取得し、上記リストに追加
		createCategoryList(monthlyHouseholdList, categoryCodeList, subCategoryNameList);

		// カテゴリーコードとサブカテゴリー名のリストから重複を除外
		List<String> distinctCategoryCode = categoryCodeList.stream().distinct().collect(Collectors.toList());
		List<String> distinctSubCategoryName = subCategoryNameList.stream().distinct().collect(Collectors.toList());

		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlySumHousehold);
		// 家計簿詳細検索条件フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", condition.getStartDate());
		// 月次家計簿リストをModelに登録
		model.addAttribute("householdList", monthlyHouseholdList);
		// 重複を除いたカテゴリーコードをModelに登録
		model.addAttribute("categoryCodeList", distinctCategoryCode);
		// 重複を除いたサブカテゴリー名をModelに登録
		model.addAttribute("subCategoryNameList", distinctSubCategoryName);

		return "household/householdDetail";
	}

	/**
	 * 家計簿詳細を検索する処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/householdDetail
	 */
	@PostMapping("/detail/search")
	public String postHouseholdDetailSearch(DetailHouseholdConditionForm form, Model model,
			MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		// フォームから月の初日を取得
		val formStartDate = form.getStartDate();

		log.trace("{}", "月次家計簿集計検索処理の呼び出しを開始します");
		val monthlySumHousehold = service.getSearchMonthlySumHousehold(form, condition, loginUser);
		log.trace("{}", "月次家計簿集計検索処理の呼び出しが完了しました");

		// 月次家計簿集計がnullでない場合
		if (monthlySumHousehold != null) {
			// 月次家計簿集計の合計を算出
			val monthlyHouseholdCalculated = householdSumCalc(monthlySumHousehold);
			// 月次家計簿集計の合計をModelに登録
			model.addAttribute("monthlyHouseholdCalculated", monthlyHouseholdCalculated);
		}
		log.trace("{}", "月次家計簿リスト検索処理の呼び出しを開始します");
		val monthlyHouseholdList = service.getSearchMonthlyHouseholdList(form, condition, loginUser);
		log.trace("{}", "月次家計簿リスト検索処理の呼び出しが完了しました");

		// カテゴリーコードとサブカテゴリー名のリストを作成
		List<String> categoryCodeList = new ArrayList<>();
		List<String> subCategoryNameList = new ArrayList<>();

		// 月次家計簿からカテゴリーコードとサブカテゴリーコードを取得し、上記リストに追加
		createCategoryList(monthlyHouseholdList, categoryCodeList, subCategoryNameList);

		// カテゴリーコードとサブカテゴリー名のリストから重複を除外
		List<String> distinctCategoryCode = categoryCodeList.stream().distinct().collect(Collectors.toList());
		List<String> distinctSubCategoryName = subCategoryNameList.stream().distinct().collect(Collectors.toList());

		// 家計簿詳細検索条件フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", formStartDate);
		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlySumHousehold);
		// 月次家計簿リストをModelに登録
		model.addAttribute("householdList", monthlyHouseholdList);
		// 重複を除いたカテゴリーコードをModelに登録
		model.addAttribute("categoryCodeList", distinctCategoryCode);
		// 重複を除いたサブカテゴリー名をModelに登録
		model.addAttribute("subCategoryNameList", distinctSubCategoryName);

		return "household/householdDetail";
	}

	/**
	 * 収支内訳画面に遷移する処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/detailChart
	 */
	@GetMapping("/detail/chart")
	public String getDetailChart(DetailHouseholdConditionForm form, Model model, MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		// 月次家計簿集計取得処理の呼び出し
		log.trace("{}", "月次家計簿集計取得処理の呼び出しを開始します");
		val monthlySumHousehold = service.getSumMonthlyHousehold(condition, loginUser);
		log.trace("{}", "月次家計簿集計取得処理の呼び出しが完了しました");
		// 月次家計簿集計がnullでない場合
		if (monthlySumHousehold != null) {
			// 月次家計簿集計の合計を算出
			val monthlyHouseholdCalculated = householdSumCalc(monthlySumHousehold);
			// 月次家計簿集計の合計をModelに登録
			model.addAttribute("monthlyHouseholdCalculated", monthlyHouseholdCalculated);
		}
		log.trace("{}", "カテゴリー別支出内訳集計リスト取得処理の呼び出しを開始します");
		val monthlyCategorySumPayList = service.getMonthlySumCategoryPayment(condition, loginUser);
		log.trace("{}", "カテゴリー別支出内訳集計リスト取得処理の呼び出しが完了しました");

		// カテゴリー別支出内訳集計リストの支出を合計する
		val categoryPayTotal = monthlyCategorySumPayList.stream().mapToInt(list -> list.getPayment()).sum();

		log.trace("{}", "月次カテゴリー別支出内訳リスト取得処理の呼び出しを開始します");
		val monthlyCategoryPayList = service.getMonthlyCategoryPayment(condition, loginUser);
		log.trace("{}", "月次カテゴリー別支出内訳リスト取得処理の呼び出しが完了しました");

		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlySumHousehold);
		// 家計簿詳細検索条件フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", condition.getStartDate());

		// カテゴリー別支出内訳集計リストをModelに登録
		model.addAttribute("monthlyCategorySumLPayList", monthlyCategorySumPayList);
		// カテボリー別支出内訳リストをModelに登録
		model.addAttribute("monthlyCategoryPayList", monthlyCategoryPayList);

		// カテゴリーの合計支出をModelに登録
		model.addAttribute("categoryPayTotal", categoryPayTotal);

		// カテゴリーごとの集計結果をModelに登録
		model.addAttribute("sumFoodList", service.filterFoodCategory(monthlyCategorySumPayList));
		model.addAttribute("sumComoddityList", service.filterCommodityCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHobbyList", service.filterHobbyCategory(monthlyCategorySumPayList));
		model.addAttribute("sumSocialExList", service.filterSocialExpenceCategory(monthlyCategorySumPayList));
		model.addAttribute("sumTransportList", service.filterTransportCategory(monthlyCategorySumPayList));
		model.addAttribute("sumFasshonList", service.filterFasshonCategory(monthlyCategorySumPayList));
		model.addAttribute("sumCarList", service.filterCarCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHealthList", service.filterHealthCategory(monthlyCategorySumPayList));
		model.addAttribute("sumLiberalList", service.filterLiberalCategory(monthlyCategorySumPayList));
		model.addAttribute("sumSpecialExList", service.filterSpecialExpenceCategory(monthlyCategorySumPayList));
		model.addAttribute("sumUnilList", service.filterUtilitiesCategory(monthlyCategorySumPayList));
		model.addAttribute("sumCommunicationList", service.filterCommunicationCostCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHouseList", service.filterHouseCostCategory(monthlyCategorySumPayList));
		model.addAttribute("sumOtherList", service.filterOthersCategory(monthlyCategorySumPayList));
		model.addAttribute("sumUnsortList", service.filterUnsortedCategory(monthlyCategorySumPayList));

		// カテゴリーごとのリストをModelに登録
		model.addAttribute("foodList", service.filterFoodCategory(monthlyCategoryPayList));
		model.addAttribute("comoddityList", service.filterCommodityCategory(monthlyCategoryPayList));
		model.addAttribute("hobbyList", service.filterHobbyCategory(monthlyCategoryPayList));
		model.addAttribute("socialExList", service.filterSocialExpenceCategory(monthlyCategoryPayList));
		model.addAttribute("transportList", service.filterTransportCategory(monthlyCategoryPayList));
		model.addAttribute("fasshonList", service.filterFasshonCategory(monthlyCategoryPayList));
		model.addAttribute("carList", service.filterCarCategory(monthlyCategoryPayList));
		model.addAttribute("healthList", service.filterHealthCategory(monthlyCategoryPayList));
		model.addAttribute("liberalList", service.filterLiberalCategory(monthlyCategoryPayList));
		model.addAttribute("specialExList", service.filterSpecialExpenceCategory(monthlyCategoryPayList));
		model.addAttribute("unilList", service.filterUtilitiesCategory(monthlyCategoryPayList));
		model.addAttribute("communicationList", service.filterCommunicationCostCategory(monthlyCategoryPayList));
		model.addAttribute("houseList", service.filterHouseCostCategory(monthlyCategoryPayList));
		model.addAttribute("otherList", service.filterOthersCategory(monthlyCategoryPayList));
		model.addAttribute("unsortList", service.filterUnsortedCategory(monthlyCategoryPayList));

		return "household/detailChart";
	}

	/**
	 * 収支内訳を検索する処理
	 * 
	 * @param form 家計簿詳細検索フォーム
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/detailChart
	 */
	@PostMapping("/detail/chart/search")
	public String postDetailChart(DetailHouseholdConditionForm form, Model model,
			MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {
		// フォームから月の初日を取得
		val formStartDate = form.getStartDate();

		log.trace("{}", "月次家計簿集計検索処理の呼び出しを開始します");
		val monthlySumHousehold = service.getSearchMonthlySumHousehold(form, condition, loginUser);
		log.trace("{}", "月次家計簿集計検索処理の呼び出しが完了しました");

		// 月次家計簿集計がnullでない場合
		if (monthlySumHousehold != null) {
			// 月次家計簿集計の合計を算出
			val monthlyHouseholdCalculated = householdSumCalc(monthlySumHousehold);
			// 月次家計簿集計の合計をModelに登録
			model.addAttribute("monthlyHouseholdCalculated", monthlyHouseholdCalculated);
		}

		log.trace("{}", "カテゴリー別支出内訳集計リスト検索処理の呼び出しを開始します");
		val monthlyCategorySumPayList = service.searchMonthlySumCategoryPayment(form, condition, loginUser);
		log.trace("{}", "カテゴリー別支出内訳集計リスト検索処理の呼び出しが完了しました");

		// カテゴリー別支出内訳集計リストの支出を合計する
		val categoryPayTotal = monthlyCategorySumPayList.stream().mapToInt(list -> list.getPayment()).sum();

		log.trace("{}", "月次カテゴリー別支出内訳リスト検索処理の呼び出しを開始します");
		val monthlyCategoryPayList = service.searchMonthlyCategoryPayment(form, condition, loginUser);
		log.trace("{}", "月次カテゴリー別支出内訳リスト検索処理の呼び出しが完了しました");

		// 家計簿詳細検索条件フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", formStartDate);
		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlySumHousehold);

		// カテゴリー別支出内訳集計リストをModelに登録
		model.addAttribute("monthlyCategorySumLPayList", monthlyCategorySumPayList);
		// カテゴリー別支出内訳リストをModelに登録
		model.addAttribute("monthlyCategoryPayList", monthlyCategoryPayList);

		// カテゴリーの合計支出をModelに登録
		model.addAttribute("categoryPayTotal", categoryPayTotal);

		// カテゴリーごとの集計結果をModelに登録
		model.addAttribute("sumFoodList", service.filterFoodCategory(monthlyCategorySumPayList));
		model.addAttribute("sumComoddityList", service.filterCommodityCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHobbyList", service.filterHobbyCategory(monthlyCategorySumPayList));
		model.addAttribute("sumSocialExList", service.filterSocialExpenceCategory(monthlyCategorySumPayList));
		model.addAttribute("sumTransportList", service.filterTransportCategory(monthlyCategorySumPayList));
		model.addAttribute("sumFasshonList", service.filterFasshonCategory(monthlyCategorySumPayList));
		model.addAttribute("sumCarList", service.filterCarCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHealthList", service.filterHealthCategory(monthlyCategorySumPayList));
		model.addAttribute("sumLiberalList", service.filterLiberalCategory(monthlyCategorySumPayList));
		model.addAttribute("sumSpecialExList", service.filterSpecialExpenceCategory(monthlyCategorySumPayList));
		model.addAttribute("sumUnilList", service.filterUtilitiesCategory(monthlyCategorySumPayList));
		model.addAttribute("sumCommunicationList", service.filterCommunicationCostCategory(monthlyCategorySumPayList));
		model.addAttribute("sumHouseList", service.filterHouseCostCategory(monthlyCategorySumPayList));
		model.addAttribute("sumOtherList", service.filterOthersCategory(monthlyCategorySumPayList));
		model.addAttribute("sumUnsortList", service.filterUnsortedCategory(monthlyCategorySumPayList));

		// カテゴリーごとのリストをModelに登録
		model.addAttribute("foodList", service.filterFoodCategory(monthlyCategoryPayList));
		model.addAttribute("comoddityList", service.filterCommodityCategory(monthlyCategoryPayList));
		model.addAttribute("hobbyList", service.filterHobbyCategory(monthlyCategoryPayList));
		model.addAttribute("socialExList", service.filterSocialExpenceCategory(monthlyCategoryPayList));
		model.addAttribute("transportList", service.filterTransportCategory(monthlyCategoryPayList));
		model.addAttribute("fasshonList", service.filterFasshonCategory(monthlyCategoryPayList));
		model.addAttribute("carList", service.filterCarCategory(monthlyCategoryPayList));
		model.addAttribute("healthList", service.filterHealthCategory(monthlyCategoryPayList));
		model.addAttribute("liberalList", service.filterLiberalCategory(monthlyCategoryPayList));
		model.addAttribute("specialExList", service.filterSpecialExpenceCategory(monthlyCategoryPayList));
		model.addAttribute("unilList", service.filterUtilitiesCategory(monthlyCategoryPayList));
		model.addAttribute("communicationList", service.filterCommunicationCostCategory(monthlyCategoryPayList));
		model.addAttribute("houseList", service.filterHouseCostCategory(monthlyCategoryPayList));
		model.addAttribute("otherList", service.filterOthersCategory(monthlyCategoryPayList));
		model.addAttribute("unsortList", service.filterUnsortedCategory(monthlyCategoryPayList));

		return "household/detailChart";
	}

	/**
	 * モーダルウィンドウの支出金額の登録処理を行い、詳細画面にリダイレクトする処理
	 * 
	 * @param easyHouseholdForm 家計簿カンタン入力フォーム
	 * @param result
	 * @param loginUser ログインユーザー
	 * @return household/detail
	 * @throws Exception 
	 */
	@PostMapping("/modalPayment")
	public String postModalPaymentHousehold(
			@ModelAttribute("easyHouseholdForm") @Validated EasyHouseholdForm easyHouseholdForm, BindingResult result,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {

		log.info("{}", "バリデーションを開始します");
		if (result.hasErrors()) {
			// 詳細画面に遷移する
			return "redirect:/household/detail";
		}
		log.info("{}", "バリデーションが完了しました");

		// 家計簿カンタン入力処理の呼び出し
		log.trace("{}", "家計簿カンタン入力処理(モーダルウィンドウ)の呼び出しを開始します");
		service.easyInputMHousehold(easyHouseholdForm, loginUser);
		log.info(easyHouseholdForm.toString());
		log.trace("{}", "家計簿カンタン入力処理(モーダルウィンドウ)の呼び出しが完了しました");

		return "redirect:/household/detail";
	}

	/**
	 * モーダルウィンドウの主入金額の登録処理を行い、詳細画面にリダイレクトする処理
	 * 
	 * @param modalDepositForm モーダルウィンドウ用家計簿収入金額フォーム
	 * @param result
	 * @param loginUser ログインユーザー
	 * @return household/detail
	 */
	@PostMapping("/modalDeposit")
	public String postModalDepositHousehold(
			@ModelAttribute("modalDepositForm") @Validated ModalDepositForm modalDepositForm, BindingResult result,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.info("{}", "バリデーションを開始します");
		if (result.hasErrors()) {
			// 詳細画面に遷移する
			return "redirect:/household/detail";
		}
		log.info("{}", "バリデーションが完了しました");

		log.trace("{}", "家計簿カンタン収入登録処理の呼び出しを開始します");
		service.registDepositHousehold(modalDepositForm, loginUser);
		log.trace("{}", "家計簿カンタン収入登録処理の呼び出しが完了しました");

		return "redirect:/household/detail";
	}

	/**
	 * 家計簿編集画面に遷移する処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param updateForm 家計簿更新フォームクラス
	 * @param deleteForm 家計簿削除フォームクラス
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/edit
	 */
	@GetMapping("/edit")
	public String getEditHousehold(DetailHouseholdConditionForm form, UpdateHouseholdForm updateForm,
			DeleteHouseholdForm deleteForm, Model model,
			MHouseholdCondition condition, @AuthenticationPrincipal LoginUser loginUser) {

		// 月次家計簿リスト取得処理の呼び出し
		log.trace("{}", "月次家計簿リスト取得処理の呼び出しを開始します");
		val monthlyHouseholdList = service.getMonthlyHouseholdList(condition, loginUser);
		log.trace("{}", "月次家計簿リスト取得処理の呼び出しが完了しました");

		// 編集画面で検索処理を行うユーザーとログインユーザーのIDが異なる場合
		if (condition.getUserId() != loginUser.getUser().getUserId()) {
			// 詳細画面にリダイレクトする
			return "redirect:/household/detail";
		}

		// カテゴリーコードとサブカテゴリー名のリストを作成
		List<String> categoryCodeList = new ArrayList<>();
		List<String> subCategoryNameList = new ArrayList<>();

		// 月次家計簿からカテゴリーコードとサブカテゴリーコードを取得し、上記リストに追加
		createCategoryList(monthlyHouseholdList, categoryCodeList, subCategoryNameList);

		// カテゴリーコードとサブカテゴリー名のリストから重複を除外
		List<String> distinctCategoryCode = categoryCodeList.stream().distinct().collect(Collectors.toList());
		List<String> distinctSubCategoryName = subCategoryNameList.stream().distinct().collect(Collectors.toList());

		// 家計簿詳細検索条件フォームをModelに登録
		model.addAttribute("form", form);
		// 家計簿更新フォームをModelに登録
		model.addAttribute("updateForm", updateForm);
		// 家計簿削除フォームをModelに登録
		model.addAttribute("deleteForm", deleteForm);
		// 月の初日をModelに登録
		model.addAttribute("startDate", condition.getStartDate());
		// 月次家計簿リストをModelに登録
		model.addAttribute("householdList", monthlyHouseholdList);
		// 重複を除いたカテゴリーコードをModelに登録
		model.addAttribute("categoryCodeList", distinctCategoryCode);
		// 重複を除いたサブカテゴリー名をModelに登録
		model.addAttribute("subCategoryNameList", distinctSubCategoryName);

		return "household/edit";
	}

	/**
	 * 編集画面で家計簿詳細表を検索する処理
	 * 
	 * @param form 家計簿詳細検索条件フォーム
	 * @param updateForm 家計簿更新フォーム
	 * @param deleteForm 家計簿削除フォーム
	 * @param model
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @return household/edit
	 */
	@PostMapping("/edit/search")
	public String postEditSearchHousehold(DetailHouseholdConditionForm form, UpdateHouseholdForm updateForm,
			DeleteHouseholdForm deleteForm, Model model, MHouseholdCondition condition,
			@AuthenticationPrincipal LoginUser loginUser) {

		// フォームから月の初日を取得
		val formStartDate = form.getStartDate();

		log.trace("{}", "月次家計簿リスト検索処理の呼び出しを開始します");
		val monthlyHouseholdList = service.getSearchMonthlyHouseholdList(form, condition, loginUser);
		log.trace("{}", "月次家計簿リスト検索処理の呼び出しが完了しました");

		// 編集画面で検索処理を行うユーザーとログインユーザーのIDが異なる場合
		if (condition.getUserId() != loginUser.getUser().getUserId()) {
			// 詳細画面にリダイレクトする
			return "redirect:/household/detail";
		}
		// カテゴリーコードとサブカテゴリー名のリストを作成
		List<String> categoryCodeList = new ArrayList<>();
		List<String> subCategoryNameList = new ArrayList<>();

		// 月次家計簿からカテゴリーコードとサブカテゴリーコードを取得し、上記リストに追加
		createCategoryList(monthlyHouseholdList, categoryCodeList, subCategoryNameList);

		// カテゴリーコードとサブカテゴリー名のリストから重複を除外
		List<String> distinctCategoryCode = categoryCodeList.stream().distinct().collect(Collectors.toList());
		List<String> distinctSubCategoryName = subCategoryNameList.stream().distinct().collect(Collectors.toList());

		// フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", formStartDate);
		// 家計簿更新フォームをModelに登録
		model.addAttribute("updateForm", updateForm);
		// 家計簿削除フォームをModelに登録
		model.addAttribute("deleteForm", deleteForm);
		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlyHouseholdList);
		// 月次家計簿リストをModelに登録
		model.addAttribute("householdList", monthlyHouseholdList);
		// 重複を除いたカテゴリーコードをModelに登録
		model.addAttribute("categoryCodeList", distinctCategoryCode);
		// 重複を除いたサブカテゴリー名をModelに登録
		model.addAttribute("subCategoryNameList", distinctSubCategoryName);

		return "household/edit";
	}

	/**
	 * 家計簿を更新し、編集画面にリダイレクトする処理
	 * 
	 * @param updateForm 家計簿更新フォーム
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return household/edit
	 * @throws Exception
	 */
	@PostMapping("/update")
	public String postMHouseholdUpdate(@ModelAttribute("updateForm") @Validated UpdateHouseholdForm updateForm,
			BindingResult result, RedirectAttributes redirectAttributes) throws Exception {

		log.info("バリデーションチェック開始");
		if (result.hasErrors()) {
			// フラッシュメッセージをリダイレクト先(/edit)に渡す
			redirectAttributes.addFlashAttribute("errorMessage", "更新に失敗しました(支出・収入のいずれか、日付、カテゴリーを選択してください)");
			// 編集画面に戻る
			return "redirect:/household/edit";
		}
		log.info("バリデーションチェックが完了しました");

		log.trace("{}", "家計簿更新処理の呼び出しを開始します");
		service.updateInputMHousehold(updateForm);
		log.info(updateForm.toString());
		log.trace("{}", "家計簿更新処理の呼び出しが完了しました");

		// フラッシュメッセージをリダイレクト先(/edit)に渡す
		redirectAttributes.addFlashAttribute("updateMessage", "家計簿を更新しました");

		return "redirect:/household/edit";
	}

	/**
	 * 家計簿を削除し、編集画面にリダイレクトする処理
	 * 
	 * @param deleteForm 家計簿削除フォーム
	 * @param model
	 * @param redirectAttributes
	 * @return household/edit
	 */
	@PostMapping("/delete")
	public String postMHouseholdDelete(DeleteHouseholdForm deleteForm, RedirectAttributes redirectAttributes) {

		log.trace("{}", "家計簿削除処理の呼び出しを開始します");
		service.deleteInputMHouhosed(deleteForm);
		log.trace("{}", "家計簿削除処理の呼び出しが完了しました");

		// フラッシュメッセージをリダイレクト先(/edit)に渡す
		redirectAttributes.addFlashAttribute("deleteMessage", "家計簿を削除しました");
		return "redirect:/household/edit";
	}

	/**
	 * 家計簿マスタリストからカテゴリーコードとサブカテゴリー名を抽出しリストを作成する処理
	 * 
	 * @param householdList 家計簿マスタリスト
	 * @param categoryCodeList カテゴリーコードリスト
	 * @param subCategoryNameList サブカテゴリー名リスト
	 */
	private void createCategoryList(List<MHousehold> householdList, List<String> categoryCodeList,
			List<String> subCategoryNameList) {
		householdList.stream().forEach(list -> {
			val categoryCode = list.getCategory().getCategoryCode();
			val subCategoryName = list.getCategory().getSubCategoryName();
			categoryCodeList.add(categoryCode);
			subCategoryNameList.add(subCategoryName);
		});
	}

	/**
	 * 月次家計簿集計の合計を算出する処理
	 * 
	 * @param household 家計簿マスタ
	 * @return 月次家計簿集計の合計
	 */
	private Integer householdSumCalc(MHousehold household) {
		val deposit = household.getDeposit();
		val payment = household.getPayment();
		val calc = deposit - payment;

		return calc;
	}
}
