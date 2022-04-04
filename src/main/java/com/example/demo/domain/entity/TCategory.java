package com.example.demo.domain.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * カテゴリートランエンティティクラス
 */
@Data
public class TCategory implements Serializable {
	
	private static final long serialVersionUID = 3952651193994878229L;
	
	/**カテゴリーID*/
	private Integer categoryId;
	
	/**カテゴリーコード*/
	private String categoryCode;
	
	/**サブカテゴリー名*/
	private String subCategoryName;

}
