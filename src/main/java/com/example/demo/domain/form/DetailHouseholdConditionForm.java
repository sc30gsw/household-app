package com.example.demo.domain.form;

import java.io.Serializable;

import lombok.Data;

/**
 * 家計簿詳細検索条件フォームクラス
 */
@Data
public class DetailHouseholdConditionForm implements Serializable {

	private static final long serialVersionUID = -3759446828326403132L;

	/**検索開始日*/
	private String startDate;
	
	/**検索終了日*/
	private String endDate;
}
