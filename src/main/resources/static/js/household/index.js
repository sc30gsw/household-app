// トップページの円グラフ(月の収支計算用)
function pieChart() {
	// data-labels-pluginの登録
	Chart.register(ChartDataLabels);

	const type = 'pie';

	const data = {
		labels: ["収入", "支出"],
		datasets: [{
			backgroundColor: [
				// 収入
				"#136FFF",
				// 支出
				"#FF9872"
			],
			data: [45000, 55000]
		}]
	};
	
	const depositRatio = data.datasets.filter(function(d) {
		const sum = d.data[0] + d.data[1];
		return d.data[0] / sum * 100;
	});

	const options = {
		responsive: true,
		plugins: {
			legend: {
				display: false
			},
			title: {
				display: true,
				text: '支出内訳',
				font: {
					size: 20
				}
			},
			datalabels: {
				color: "#fff",
				formatter: function(value, ctx) {
					const label = ctx.chart.data.labels[ctx.dataIndex];
					return label;
				}
			},
			tooltip: {
				callbacks: {
					label: function(tooltipItem) {
						const sum = tooltipItem.dataset.data[0] + tooltipItem.dataset.data[1];
						const payRatio = Math.floor(tooltipItem.dataset.data[tooltipItem.dataIndex] / sum * 100);
						const parsed = tooltipItem.parsed .toLocaleString();
						return  `${tooltipItem.label} : ${parsed} 円 | 収支割合 :  ${payRatio}%`;
					}
				}
			}
		}
	};
	// 円グラフを描画する要素を取得
	const ctx = document.getElementById("assetPie").getContext('2d');
	// 円グラフを描画する処理
	const pieChart = new Chart(ctx, {
		type: type,
		data: data,
		options: options
	})
};

// ドロップダウンのサブメニューを表示する処理
function subMenu() {
	// ドロップダウンのメインカテゴリー要素(li要素)を取得
	const parent = document.querySelectorAll('.dropdown-submenu');
	const item = Array.prototype.slice.call(parent, 0);

	item.forEach(function(element) {
		// メインカテゴリー要素のリンクを取得する
		const lists = document.querySelectorAll('.c_name');
		const list = Array.prototype.slice.call(lists, 0);

		list.forEach(function(l) {
			// メインカテゴリー要素(li要素)にマウスが乗った時クラスを追加する
			element.addEventListener("mouseover", function() {
				element.querySelector(".sub_menu").classList.add("open");
			}, false);
			// メインカテゴリー要素(li要素)からマウスが離れた時クラスを除外する
			l.addEventListener("mouseout", function() {
				element.querySelector(".sub_menu").classList.remove("open");
			}, false);
		});

	});
};

// hiddenタイプの入力欄にカテゴリーのID値を設定する処理
function getAnchorValue() {
	document.addEventListener('click', function(e) {
		// 大カテゴリーのドロップダウンボタンの要素を取得
		const dropdownMenuLink = document.getElementById('dropdownMenuLink')
		// サブカテゴリーのドロップダウンボタンの要素を取得
		const dropdownSecondary = document.getElementById('js-middle-category-selected')
		// 大カテゴリーのリンクを配列で取得
		const links = document.querySelectorAll('.c_name')
		// hiddenタイプのカテゴリーID入力要素を取得
		const categoryInput = document.getElementById('categoryId')

		// 大カテゴリーのみ選択された時のドロップダウンの動作と入力欄の値の設定
		for (let i = 0; i < links.length; i++) {

			// 大カテゴリークリック時にドロップダウンのテキストをクリックした大カテゴリーのものにする
			if (e.target.closest('#dropdownMenuLink')) {

				// 各大カテゴリーのリンクが押された時イベント発火
				links[i].addEventListener('click', function() {
					// ドロップダウンのテキストを、クリックされた大カテゴリーのテキストに置き換える
					dropdownMenuLink.text = links[i].textContent
					dropdownSecondary.text = links[i].textContent
					// 大カテゴリーとサブカテゴリーにクラスを追加
					dropdownMenuLink.classList.add("menu-onclick")
					dropdownSecondary.classList.add("menu-onclick")
					// カテゴリーID入力欄に選択された値を設定する
					categoryInput.value = links[i].dataset.value
				})
			}
		}

		// クリックされた要素がサブカテゴリーのリンクだった場合の処理
		if (e.target.closest('.m_c_name')) {

			// 大カテゴリーとサブカテゴリーにクラスを追加
			dropdownMenuLink.classList.add("menu-onclick")
			dropdownSecondary.classList.add("menu-onclick")

			// サブカテゴリーの選択により、大カテゴリーのテキストを変化させる
			switch (e.target.closest('.m_c_name').textContent) {

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
			dropdownSecondary.textContent = e.target.closest('.m_c_name').textContent

			// クリックされたサブカテゴリーのdata-value属性を入力欄の値に設定する
			categoryInput.value = e.target.closest('.m_c_name').dataset.value

		}
	})
}