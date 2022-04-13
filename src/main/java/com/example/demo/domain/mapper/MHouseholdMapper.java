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
	 * 月次家計簿集計取得
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタ
	 */
	public MHousehold monthlySumHousehold(MHouseholdCondition condition);
	
	/**
	 * 最近の家計簿履歴を取得する処理
	 * 
	 * @param userId ユーザーID
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> getLatestHouseholdList(Integer userId);
	
	/**
	 * 月次家計簿集計リスト取得
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> monthlyHouseholdList(MHouseholdCondition condition);
	
	/**
	 * 月次カテゴリー別支出集計リスト取得
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> getMonthlyHouseholdCategorySumList(MHouseholdCondition condition);
	
	/**
	 * 月次カテゴリー別支出内訳リスト取得
	 * 
	 * @param condition 家計簿検索条件
	 * @return 家計簿マスタリスト
	 */
	public List<MHousehold> getMonthlyHouseholdCategoryList(MHouseholdCondition condition);
	
	/**
	 * 家計簿更新処理
	 * 
	 * @param householdId 家計簿ID
	 * @return
	 */
	public int updateMHousehold(MHousehold household);
	
	/**
	 * 家計簿削除処理
	 * 
	 * @param householdId 家計簿ID
	 * @return
	 */
	public int deleteMHousehold(Integer householdId);
}
