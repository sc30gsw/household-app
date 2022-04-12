package com.example.demo.domain.form;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 家計簿更新フォームクラス
 */
@Data
public class UpdateHouseholdForm implements Serializable {

	private static final long serialVersionUID = -7821128021575754604L;
	
	/**家計簿ID*/
	private Integer householdId;

	/**カテゴリーID*/
	@NotNull
	private Integer categoryId;
	
	/**入金*/
	@NotBlank
	@Pattern(regexp = "^[0-9]+$")
	private String deposit;
	
	/**出金*/
	@NotBlank
	@Pattern(regexp = "^[0-9]+$")
	private String payment;
	
	/**備考(メモ)*/
	private String note;
	
	/**出金日*/
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date activeDate;

}
