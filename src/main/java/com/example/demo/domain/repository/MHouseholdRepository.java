package com.example.demo.domain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.app.MHousehold.controller.MHouseholdCondition;
import com.example.demo.domain.entity.MHousehold;
import com.example.demo.domain.mapper.MHouseholdMapper;

/**
 * 家計簿マスタリポジトリクラス
 */
@Repository
public class MHouseholdRepository {

	/**家計簿マスタマッパー*/
	@Autowired
	MHouseholdMapper mapper;
	
	/**
	 * 家計簿登録処理
	 * 
	 * @param household 家計簿マスタ
	 */
	public void registMHousehold(MHousehold household) {
		mapper.insertMHousehold(household);
	}
	
	/**
	 * 月次家計簿取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> monthlyGetHousehold(MHouseholdCondition condition) {
		return mapper.monthlyHousehold(condition);
	}
	
	/**
	 * 月次家計簿集計取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタ
	 */
	public MHousehold monthlyGetSumHousehold(MHouseholdCondition condition) {
		return mapper.monthlySumHousehold(condition);
	}
}
