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
	 * ホーム画面でカンタン入力処理を行う処理
	 * 
	 * @param form カンタン入力フォーム
	 * @param result
	 * @param condition 家計簿検索条件
	 * @param loginUser ログインユーザー
	 * @param model
	 * @param redirectAttributes
	 * @return household/index
	 */
	@PostMapping("/payment")
	public String postPayment(@ModelAttribute("form") @Validated EasyHouseholdForm form, BindingResult result,
			MHouseholdCondition condition, @AuthenticationPrincipal LoginUser loginUser, Model model,
			RedirectAttributes redirectAttributes) {

		log.info("バリデーションチェック開始");
		// 入力チェック
		if (result.hasErrors()) {
			// false:ホーム画面に遷移
			return getIndex(form, model, condition, loginUser);
		}
		log.info("バリデーションチェックが完了しました");

		// 家計簿カンタン入力処理の呼び出し
		log.trace("{}", "家計簿カンタン入力処理の呼び出しを開始します");
		service.easyInputMHousehold(form, loginUser);
		log.info(form.toString());
		log.trace("{}", "家計簿カンタン入力処理の呼び出しが完了しました");

		// フラッシュメッセージをリダイレクト先(/index)に渡す
		redirectAttributes.addFlashAttribute("message", "投稿に成功しました");

		return "redirect:/household/index";
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

		// フォームをModelに登録
		model.addAttribute("form", form);
		// 月の初日をModelに登録
		model.addAttribute("startDate", formStartDate);
		// 月次家計簿集計をModelに登録
		model.addAttribute("monthlySumHousehold", monthlySumHousehold);
		// 月次家計簿リストをModelに登録
		model.addAttribute("householdList", monthlyHouseholdList);

		return "household/householdDetail";
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
		
		return "household/edit";
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
