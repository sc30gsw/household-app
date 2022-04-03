package com.example.demo.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.app.MHousehold.controller.MHouseholdCondition;
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
	
	/**
	 * 月次家計取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> monthlyHousehold(MHouseholdCondition condition);
	
	/**
	 * 月次家計簿集計取得
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタ
	 */
	public MHousehold monthlySumHousehold(MHouseholdCondition condition);
}
