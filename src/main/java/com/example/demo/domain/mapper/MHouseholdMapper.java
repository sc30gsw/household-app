package com.example.demo.domain.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.entity.MHousehold;

/**
 * 家計簿マスタマッパーインターフェース
 */
@Mapper
public interface MHouseholdMapper {

	/**
	 * 家計簿登録処理
	 * 
	 * @param household 家計簿マスタ
	 * @return
	 */
	public int insertMHousehold(MHousehold household);
}
