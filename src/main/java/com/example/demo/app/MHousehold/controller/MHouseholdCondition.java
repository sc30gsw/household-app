package com.example.demo.app.MHousehold.controller;

import java.io.Serializable;

import lombok.Data;

/**
 * 家計簿検索条件保持クラス
 */
@Data
public class MHouseholdCondition implements Serializable {
	
	private static final long serialVersionUID = -4570874744237894856L;
	
	/**ユーザーID*/
	private Integer userId;
	
	/**検索開始日*/
	private String startDate;
	
	/**検索終了日*/
	private String endDate;

}
