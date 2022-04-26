package com.example.demo.app.MHousehold.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.form.EasyHouseholdForm;
import com.example.demo.domain.service.LoginUser;
import com.example.demo.domain.service.MHouseholdService;

import lombok.extern.slf4j.Slf4j;

/**
 * 家計簿マスタレストコントローラークラス
 */
@RestController
@RequestMapping("/household")
@Slf4j
public class MHouseholdRestController {
	
	/**家計簿マスタサービスクラス*/
	@Autowired
	MHouseholdService service;
	
	/**
	 * ホーム画面でカンタン入力処理を行う処理(非同期)
	 * 
	 * @param form カンタン入力フォーム
	 * @param loginUser ログインユーザー
	 * @return 0
	 * @throws Exception 
	 */
	@PostMapping("/payment")
	public int restPostPayment(EasyHouseholdForm form, @AuthenticationPrincipal LoginUser loginUser) throws Exception {

		// 家計簿カンタン入力処理の呼び出し
		log.trace("{}", "家計簿カンタン入力処理の呼び出しを開始します");
		service.easyInputMHousehold(form, loginUser);
		log.info(form.toString());
		log.trace("{}", "家計簿カンタン入力処理の呼び出しが完了しました");

		return 0;
	}

}
