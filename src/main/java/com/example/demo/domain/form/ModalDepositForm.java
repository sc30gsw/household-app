package com.example.demo.domain.form;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * モーダルウィンドウ用家計簿収入金額フォームクラス
 */
@Data
public class ModalDepositForm implements Serializable {
	
	private static final long serialVersionUID = 220660942865569949L;

	/**カテゴリーID*/
	@NotNull
	private Integer categoryId;
	
	/**入金*/
	@NotBlank
	@Pattern(regexp = "^[0-9]+$")
	private String deposit;
	
	/**備考(メモ)*/
	private String note;
	
	/**出金日*/
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date activeDate;
}
