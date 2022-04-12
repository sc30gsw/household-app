// hiddenタイプの入力欄にカテゴリーのID値を設定する処理(詳細画面用)
function getDetailAnchorValueSec() {
	// hiddenタイプのカテゴリーID入力要素を取得
	const categoryInput = document.querySelector('.category-input-sec')
	// デフォルトでカテゴリーID入力値を「70(未分類)」とする
	categoryInput.value = 70

	document.addEventListener('click', function(e) {
		// 大カテゴリーのドロップダウンボタンの要素を取得
		const dropdownMenuLink = document.getElementById('dropdownMenuLinkSecond')
		// サブカテゴリーのドロップダウンボタンの要素を取得
		const dropdownSecondary = document.getElementById('js-middle-category-selected-second')
		// 大カテゴリーのリンクを配列で取得
		const links = document.querySelectorAll('.c_name_sec')

		// 大カテゴリーのみ選択された時のドロップダウンの動作と入力欄の値の設定
		for (let i = 0; i < links.length; i++) {

			// 大カテゴリークリック時にドロップダウンのテキストをクリックした大カテゴリーのものにする
			if (e.target.closest('#dropdownMenuLinkSecond')) {

				// 各大カテゴリーのリンクが押された時イベント発火
				links[i].addEventListener('click', function() {
					// ドロップダウンのテキストを、クリックされた大カテゴリーのテキストに置き換える
					dropdownMenuLink.text = links[i].textContent
					dropdownSecondary.text = links[i].textContent
					// 大カテゴリーとサブカテゴリーにクラスを追加
					dropdownMenuLink.classList.add("menu-onclick-sec")
					dropdownSecondary.classList.add("menu-onclick-sec")
					// カテゴリーID入力欄に選択された値を設定する
					categoryInput.value = links[i].dataset.value
				})
			}
		}

		// クリックされた要素がサブカテゴリーのリンクだった場合の処理
		if (e.target.closest('.m_c_name_sec')) {

			// 大カテゴリーとサブカテゴリーにクラスを追加
			dropdownMenuLink.classList.add("menu-onclick-sec")
			dropdownSecondary.classList.add("menu-onclick-sec")

			// サブカテゴリーの選択により、大カテゴリーのテキストを変化させる
			switch (e.target.closest('.m_c_name_sec').textContent) {

				case '食費':
				case '食料品':
				case '外食':
				case 'カフェ':
					dropdownMenuLink.textContent = '食費'
					break

				case '日用品':
				case 'ドラッグストア':
				case 'おこづかい':
				case 'ペット用品':
					dropdownMenuLink.textContent = '日用品'
					break

				case '趣味・娯楽':
				case 'アウトドア':
				case 'スポーツ':
				case '映画・音楽・ゲーム':
				case '旅行':
					dropdownMenuLink.textContent = '趣味・娯楽'
					break

				case '交際費':
				case '飲み代':
				case 'プレゼント代':
				case '冠婚葬祭':
					dropdownMenuLink.textContent = '交際費'
					break

				case '交通費':
				case '電車':
				case 'バス':
				case 'タクシー':
				case '飛行機':
					dropdownMenuLink.textContent = '交通費'
					break

				case '衣服':
				case 'クリーニング':
				case '美容院・理髪':
				case '化粧品':
				case 'アクセサリー':
					dropdownMenuLink.textContent = '衣服・美容'
					break

				case '自動車ローン':
				case '道路料金':
				case 'ガソリン':
				case '駐車場':
				case '車検・整備':
				case '自動車保険':
					dropdownMenuLink.textContent = '自動車'
					break

				case 'フィットネス':
				case 'ボディケア':
				case '医療費':
				case '薬':
					dropdownMenuLink.textContent = '健康・医療'
					break

				case '書籍':
				case '新聞・雑誌':
				case '習い事':
				case '学費':
				case '塾':
					dropdownMenuLink.textContent = '教養・教育'
					break

				case '家具・家電':
				case '住宅・リフォーム':
					dropdownMenuLink.textContent = '特別な支出'
					break

				case '光熱費':
				case '電気代':
				case 'ガス・灯油代':
				case '水道代':
					dropdownMenuLink.textContent = '水道・光熱費'
					break

				case '携帯電話':
				case '固定電話':
				case 'インターネット':
				case '放送視聴料':
				case '情報サービス':
					dropdownMenuLink.textContent = '通信費'
					break

				case '住宅':
				case '家賃・地代':
				case 'ローン返済':
				case '管理費・積立金':
				case '地震・火災保険':
					dropdownMenuLink.textContent = '住宅'
					break

				case 'その他':
				case '仕送り':
				case '寄付金':
				case '雑費':
					dropdownMenuLink.textContent = 'その他'
					break

				case '未分類':
					dropdownMenuLink.textContent = '未分類'
					break

				default:
					dropdownMenuLink.textContent = '未分類'
			}

			// サブカテゴリーのボタンのテキストをクリックされたサブカテゴリーのものにする
			dropdownSecondary.textContent = e.target.closest('.m_c_name_sec').textContent

			// クリックされたサブカテゴリーのdata-value属性を入力欄の値に設定する
			categoryInput.value = e.target.closest('.m_c_name_sec').dataset.value

		}
	})
}