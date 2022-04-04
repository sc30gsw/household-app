package com.example.demo.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 家計簿マスタエンティティクラス
 */
@Data
public class MHousehold implements Serializable {
	
	private static final long serialVersionUID = -3710906047958073031L;
	
	/**家計簿ID*/
	private Integer householdId;
	
	/**ユーザーID*/
	private Integer userId;
	
	/**カテゴリーID*/
	private Integer categoryId;
	
	/**入金*/
	private Integer deposit;
	
	/**出金*/
	private Integer payment;
	
	/**備考(メモ)*/
	private String note;
	
	/**出金日*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date activeDate;
	
	/**作成日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime createDate;
	
	/**更新日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime updateDate;
	
	/**削除日時*/
	@DateTimeFormat(pattern = "yyyy_MM_dd HH:mm:ss")
	private LocalDateTime deleteDate;
	
	/**カテゴリートラン*/
	private TCategory category;
}
