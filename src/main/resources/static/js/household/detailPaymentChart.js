function detailPieChart() {
	const categoryCode = [];
	const paySumList = [];

	monthlyCategoryList.forEach(function(list) {
		categoryCode.push(list.category.categoryCode);
		paySumList.push(list.payment);
	});

	// data-labels-pluginの登録
	Chart.register(ChartDataLabels);
	// チャートのタイプで円グラフを指定
	const type = 'pie';
	// データの設定
	const data = {
		// ラベルの設定
		labels: categoryCode,
		// データをセット
		datasets: [{
			// 円グラフの背景色を設定
			backgroundColor: [
				// 食費
				"rgb(223, 76, 82)",
				// 日用品
				"rgb(156, 220, 170)",
				// 趣味・娯楽
				"rgb(235, 88, 143)",
				// 交際費
				"rgb(73, 117, 181)",
				// 交通費
				"rgb(75, 69, 96)",
				// 衣服・美容
				"rgb(251, 180, 76)",
				// 自動車
				"rgb(76, 78, 81)",
				// 健康・医療
				"rgb(253, 138, 106)",
				// 教養・教育
				"rgb(111, 133, 194)",
				// 特別な支出
				"rgb(173, 195, 131)",
				// 水道・光熱費
				"rgb(46, 170, 217)",
				// 通信費
				"rgb(138, 73, 181)",
				// 住宅
				"rgb(52, 159, 118)",
				// その他
				"rgb(189, 185, 180)",
				// 未分類
				"rgb(254, 150, 38)"
			],
			// 値の設定
			data: paySumList
		}]
	};

	// オプションの設定
	const options = {
		// レスポンシブwebデザインの自動補正を設定
		responsive: true,
		plugins: {
			// 凡例の設定
			legend: {
				// 凡例の除去
				display: false
			},
			// タイトル部の設定
			title: {
				display: true,
				// タイトル名の設定
				text: '大項目支出内訳',
				// タイトルフォントの設定
				font: {
					// フォントサイズを設定
					size: 20
				}
			},
			// data-labels(チャート内のラベル)の設定
			datalabels: {
				// 色の設定
				color: "#fff",
				// 円グラフからラベルのみ取得する
				formatter: function(value, ctx) {
					const label = ctx.chart.data.labels[ctx.dataIndex];
					return label;
				}
			},
			// チャートhover時に表示する文字列等の設定
			tooltip: {
				callbacks: {
					// ラベルをカスタマイズする
					label: function(tooltipItem) {
						// 各値が全体に占める割合を算出する(小数点2桁を残し切り捨て)
						const payRatio = Math.round(tooltipItem.dataset.data[tooltipItem.dataIndex] / categoryPayTotal * 100 * 100) / 100;
						// 金額をカンマ区切りに変換
						const parsed = tooltipItem.parsed.toLocaleString();
						return `${tooltipItem.label} : ${parsed} 円 | 収支割合 :  ${payRatio}%`;
					}
				}
			}
		}
	};
	// 円グラフを描画する要素を取得
	const ctx = document.getElementById("detailPie").getContext('2d');
	// 円グラフを描画する処理
	const pieChart = new Chart(ctx, {
		type: type,
		data: data,
		options: options
	})
}

function detailPieChartSec() {
	const subCategory = [];
	const payList = [];
	
	monthlyCategoryPayList.forEach(function(item) {
		subCategory.push(item.category.subCategoryName);
		payList.push(item.payment);
	});

	// data-labels-pluginの登録
	Chart.register(ChartDataLabels);
	// チャートのタイプで円グラフを指定
	const type = 'pie';
	// データの設定
	const data = {
		// ラベルの設定
		labels: subCategory,
		// データをセット
		datasets: [{
			// 円グラフの背景色を設定
			backgroundColor: [
				"rgb(223, 76, 82, 0.6)",
				"rgb(156, 220, 170, 0.6)",
				"rgb(235, 88, 143, 0.6)",
				"rgb(73, 117, 181, 0.6)",
				"rgb(75, 69, 96, 0.6)",
				"rgb(251, 180, 76, 0.6)",
				"rgb(76, 78, 81, 0.6)",
				"rgb(253, 138, 106, 0.6)",
				"rgb(111, 133, 194, 0.6)",
				"rgb(173, 195, 131, 0.6)",
				"rgb(46, 170, 217, 0.6)",
				"rgb(138, 73, 181, 0.6)",
				"rgb(52, 159, 118, 0.6)",
				"rgb(189, 185, 180, 0.6)",
				"rgb(254, 150, 38, 0.6)"
			],
			// 値の設定
			data: payList
		}]
	};

	// オプションの設定
	const options = {
		// レスポンシブwebデザインの自動補正を設定
		responsive: true,
		plugins: {
			// 凡例の設定
			legend: {
				// 凡例の除去
				display: false
			},
			// タイトル部の設定
			title: {
				display: true,
				// タイトル名の設定
				text: '中項目支出内訳',
				// タイトルフォントの設定
				font: {
					// フォントサイズを設定
					size: 20
				}
			},
			// data-labels(チャート内のラベル)の設定
			datalabels: {
				// 色の設定
				color: "#fff",
				// 円グラフからラベルのみ取得する
				formatter: function(value, ctx) {
					const label = ctx.chart.data.labels[ctx.dataIndex];
					return label;
				}
			},
			// チャートhover時に表示する文字列等の設定
			tooltip: {
				callbacks: {
					// ラベルをカスタマイズする
					label: function(tooltipItem) {
						// 各値が全体に占める割合を算出する(小数点2桁を残し切り捨て)
						const payRatio = Math.round(tooltipItem.dataset.data[tooltipItem.dataIndex] / categoryPayTotal * 100 * 100) / 100;
						// 金額をカンマ区切りに変換
						const parsed = tooltipItem.parsed.toLocaleString();
						return `${tooltipItem.label} : ${parsed} 円 | 収支割合 :  ${payRatio}%`;
					}
				}
			}
		}
	};
	// 円グラフを描画する要素を取得
	const ctx = document.getElementById("detailPieSec").getContext('2d');
	// 円グラフを描画する処理
	const pieChart = new Chart(ctx, {
		type: type,
		data: data,
		options: options
	})
}

