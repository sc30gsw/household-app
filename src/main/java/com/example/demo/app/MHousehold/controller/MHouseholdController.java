package com.example.demo.app.MHousehold.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

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

import com.example.demo.domain.form.EasyHouseholdForm;
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
}
