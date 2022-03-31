package com.example.demo.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
