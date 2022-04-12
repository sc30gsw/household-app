package com.example.demo.domain.form;

import java.io.Serializable;

import lombok.Data;

/**
 * 家計簿削除フォームクラス
 */
@Data
public class DeleteHouseholdForm implements Serializable {

	private static final long serialVersionUID = 5801873794857252076L;
	
	/**家計簿ID*/
	private Integer householdId;
}
