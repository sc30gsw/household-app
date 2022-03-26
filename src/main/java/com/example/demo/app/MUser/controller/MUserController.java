package com.example.demo.app.MUser.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.form.SignupForm;
import com.example.demo.domain.service.MUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * ユーザーマスタコントローラークラス
 */
@Controller
@RequestMapping("/")
@Slf4j
public class MUserController {

	/**ユーザーマスタサービスクラス*/
	@Autowired
	MUserService service;

	/**
	 * ユーザー登録用画面に遷移する処理
	 * 
	 * @param form ユーザーマスタ登録用フォームクラス
	 * @return user/signup
	 */
	@GetMapping("/signup")
	public String getSignup(@ModelAttribute("form") SignupForm form, Model model) {
		// ラジオボタンの初期設定
		Map<String, String> radioSex = initRadioSex();
		// Modelに登録
		model.addAttribute("radioSex", radioSex);

		return "user/signup";
	}

	/**
	 * ユーザー登録が完了し、トップページに遷移する処理
	 * 
	 * @param form ユーザーマスタ登録用フォームクラス
	 * @param result バリデーション結果
	 * @return household/index
	 */
	@PostMapping("/signup")
	public String postSignup(@ModelAttribute("form") @Validated SignupForm form, BindingResult result, Model model) {
		// 入力チェック
		log.info("バリデーションチェック開始");
		if (result.hasErrors()) {
			// false:登録画面に遷移
			return getSignup(form, model);
		}
		log.info("バリデーションチェックが完了しました");

		// ユーザー登録処理の呼び出し
		service.registMUserOne(form);
		log.info(form.toString());

		return "redirect:/household/index";
	}
	
	/**
	 * ログイン画面に遷移する処理
	 * 
	 * @return user/login
	 */
	@GetMapping("/login")
	public String getLogin() {
		return "user/login";
	}
	
	/**
	 * ログイン処理をし、トップページに遷移する処理
	 * 
	 * @return household/index
	 */
	@PostMapping("/login")
	public String postLogin() {
		return "redirect:/household/index";
	}
	
	/**
	 * ゲストログイン処理の画面に遷移する処理
	 * 
	 * @return user/guestLogin
	 */
	@GetMapping("/guest")
	public String getGuestLogn() {
		return "user/guestLogin";
	}

	/**
	 * 性別用のラジオボタンの初期値設定
	 * 
	 * @return 性別用ラジオボタン
	 */
	private Map<String, String> initRadioSex() {
		Map<String, String> radio = new LinkedHashMap<>();
		// 男性、女性をMapに格納
		radio.put("男性", "F");
		radio.put("女性", "M");
		return radio;
	}
}
