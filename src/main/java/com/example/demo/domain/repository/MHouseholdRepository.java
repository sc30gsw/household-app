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
	 * 月次家計簿集計取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタ
	 */
	public MHousehold monthlyGetSumHousehold(MHouseholdCondition condition) {
		return mapper.monthlySumHousehold(condition);
	}
	
	/**
	 * 最近の家計簿履歴を取得する処理
	 * 
	 * @param userId ユーザーID
	 * @return 家計簿マスタのリスト
	 */
	public List<MHousehold> getManyLatestHousehold(Integer userId) {
		return mapper.getLatestHouseholdList(userId);
	}
	
	/**
	 * 月次家計簿集計リスト取得処理
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> monthlyGetHouseholdList(MHouseholdCondition condition) {
		return mapper.monthlyHouseholdList(condition);
	}
}
