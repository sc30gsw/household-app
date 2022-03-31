package com.example.demo.app.MHousehold.controller;

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

import com.example.demo.domain.form.EasyHouseholdForm;
import com.example.demo.domain.service.LoginUser;
import com.example.demo.domain.service.MHouseholdService;

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
	 * @return household/index
	 */
	@GetMapping("/index")
	public String getIndex(EasyHouseholdForm form, Model model) {

		// カンタン入力用フォームをModelに登録
		model.addAttribute("form", form);

		return "household/index";
	}

	@PostMapping("/payment")
	public String postPayment(@ModelAttribute("form") @Validated EasyHouseholdForm form, BindingResult result,
			@AuthenticationPrincipal LoginUser loginUser, Model model) {

		log.info("バリデーションチェック開始");
		// 入力チェック
		if (result.hasErrors()) {
			// false:ホーム画面に遷移
			return getIndex(form, model);
		}
		log.info("バリデーションチェックが完了しました");

		// 家計簿カンタン入力処理の呼び出し
		service.easyInputMHousehold(form, loginUser);
		log.info(form.toString());
		log.trace("{}", "家計簿カンタン入力処理の呼び出し");

		return "redirect:/household/index";
	}
}
