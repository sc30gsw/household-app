package com.example.demo.common.exception;

/**
 * 業務エラー例外クラス
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -5623684714758034282L;
	
	public BusinessException(String message) {
		super(message);
	}
}
