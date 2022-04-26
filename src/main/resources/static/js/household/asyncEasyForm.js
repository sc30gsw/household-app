'use strict';

{

	/** トップページのカンタン入力フォームの登録を非同期で行う処理 */
	function easyHouseholdFormSubmit() {

		const submit = document.getElementById('submitBtn');
		const form = document.getElementById('easyHouseholdForm');

		// 送信ボタンを押したときに非同期通信を行う
		submit.addEventListener('click', e => {
			// フォームのデータをFormDataに渡す
			const formData = new FormData(form);
			// XMLHttpRequestを取得
			const XHR = new XMLHttpRequest();

			// 非同期通信を行うリクエストのURLを指定
			XHR.open('POST', '/household/payment', true);
			// 非同期通信のレスポンスを指定
			XHR.responseType = 'json';
			// サーバーにフォームのデータを送信(POST)する
			XHR.send(formData);

			// 非同期通信時に行う処理(コールバック関数)
			XHR.onload = () => {
				// HTTPステータスコードが200以外の場合
				if (XHR.status != 200) {
					// エラーメッセージを表示する
					alert(`Error ${XHR.status}: ${XHR.statusText}`);
					// バリデーションチェックを行う
					paymentValid();
					return null;
				};

				// HTTPステータスコードが200の場合
				// フォームの値をリセット(デフォルト)値にする
				resetField();
				// バリデーションメッセージを非表示にする
				validFieldReset();
				// ドロップダウンのスタイリングとテキストをデフォルトにする
				dropdownStyle();
				// フラッシュメッセージ(投稿成功メッセージ)を表示する
				flashMessage();
				// 2.5秒後にフラッシュメッセージの要素を取り除く
				setTimeout(flashMessageReset, 2500);
			};
			// イベントの初期化(二重でデータを送信することを防ぐ)
			e.preventDefault();
		});
	}

	/** バリデーションチェックを行う処理 */
	function paymentValid() {
		// バリデーションチェック対象のフィールドを取得
		const paymentInput = document.getElementById('payment');
		// バリデーション表示をする要素を取得
		const paymentValid = document.getElementById('paymentInvalid');
		
		// バリデーション該当時にバリデーション要素を表示する
		paymentValid.style.display = "block";

		switch (true) {
			// フィールド値が空の場合
			case paymentInput.value === '':
				paymentValid.textContent = '金額は必須入力です';
				break;

			// フィールド値が半角数字でない場合
			case !/^[0-9]+$/.test(paymentInput.value):
				paymentValid.textContent = '金額は半角数字で入力してください';
				break;

			// フィールド値が0の場合
			case paymentInput.value === '0':
				paymentValid.textContent = '金額は1以上を入力してください';
				break;
		}
	}

	/** バリデーション要素を非表示にする処理 */
	function validFieldReset() {
		const paymentValid = document.getElementById('paymentInvalid');
		paymentValid.style.display = 'none';
	}

	/** フィールド値を初期状態にする処理 */
	function resetField() {
		// 各フィールドを取得(input要素)
		const paymentInput = document.getElementById('payment');
		const categoryInput = document.getElementById('categoryId');
		const dateInput = document.getElementById('cal-date-input');
		const dateSpan = document.getElementById('date-span');
		const noteInput = document.getElementById('note');
		
		// 各フィールドを初期状態にする
		paymentInput.value = '';
		categoryInput.value = 70
		dateInput.value = '';
		dateSpan.textContent = '';
		noteInput.value = '';
	}

	/** ドロップダウンを初期状態にする処理 */
	function dropdownStyle() {
		const dropdownMenuLink = document.getElementById('dropdownMenuLink');
		const dropdownSubMenuLink = document.getElementById('js-middle-category-selected');
		
		// 各ドロップダウンからクラス属性を取り除く
		dropdownMenuLink.classList.remove('menu-onclick');
		dropdownSubMenuLink.classList.remove('menu-onclick');
		
		// 各ドロップダウンのテキストを初期状態にする
		dropdownMenuLink.textContent = '未分類';
		dropdownSubMenuLink.textContent = '未分類';
	}

	/** フラッシュメッセージを表示する処理 */
	function flashMessage() {
		// divタグの親要素となる要素を取得
		const parent = document.querySelector('.submit-box');
		// divタグの生成
		const div = document.createElement('div');
		// メッセージを表示するHTMLを作成
		const messageHTML = '<span class="easy-flash-message">投稿に成功しました</span>'
		
		// 生成したdivタグを親要素に追加
		parent.appendChild(div);
		// divタグ内にメッセージを追加
		div.innerHTML = messageHTML;
	}

	/** フラッシュメッセージを非表示にする処理 */
	function flashMessageReset() {
		// フラッシュメッセージの親要素を取得
		const parentNode = document.querySelector('.submit-box');
		// フラッシュメッセージの最後の子要素(上記関数内で生成したdivタグ)を取得
		const childNode = parentNode.lastElementChild;
		
		// divタグにクラス属性を追加(CSSで非表示設定済)
		childNode.classList.add('flashMessageBlock');
	}

	window.addEventListener('load', easyHouseholdFormSubmit);
}