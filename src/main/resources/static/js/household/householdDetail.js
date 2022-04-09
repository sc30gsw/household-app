// ページの最上部に戻る関数
function toTop() {
	const toTopBtn = document.querySelector('.cf-btn-to-top');
	toTopBtn.addEventListener('click', function() {
		window.scrollTo({
			top: 0, behavior: "smooth"
		});
	});

	// 縦スクロールが10の位置に来たときに要素を追加する
	window.addEventListener('scroll', function() {
		if (window.pageYOffset > 10) {
			toTopBtn.style.opacity = '1';
		} else if (window.pageYOffset < 10) {
			toTopBtn.style.opacity = '0';
		}
	});
}

// 日にちまでの値を導出し、input値に設定する関数
function inputDate() {
	// 年を選択するselectボックスの取得
	const selectYear = document.querySelector('.search-year-filter');
	// 月を取得するselectボックスの取得
	const selectMonth = document.querySelector('.search-month-filter');
	// 月の初日までの値を入力する入力欄の取得
	const inputStartDate = document.getElementById('startDate');
	// 月の末日までの値を入力する入力欄の取得
	const inputEndDate = document.getElementById('endDate');
	// 検索ボタンの取得
	const submit = document.getElementById('search-date-btn');
	// 月の初日
	const firstDate = 1;
	// 月の末日
	let lastDate = 31;

	submit.addEventListener('click', function() {

		// 月のセレクトボックスの値が2月の場合
		if (selectMonth.value === '2') {
			lastDate = 28;

			// 取得した年を数値に変換
			const num = Number(selectYear.value);

			// 年がうるう年の場合
			if ((num % 4 === 0 && num % 100 !== 0) || num % 400 === 0) {
				lastDate = 29;
			}

			// 2月でもうるう年でもない場合
		} else {
			// 4、6、9、11月の場合
			switch (selectMonth.value) {
				case '4':
				case '6':
				case '9':
				case '11':
					lastDate = 30;
					break;
				// 1、5、7、8、12月の場合
				default:
					lastDate = 31;
					break;
			}
		}

		// 入力欄に値を設定する
		inputStartDate.value = `${selectYear.value}-${selectMonth.value}-${firstDate}`;
		inputEndDate.value = `${selectYear.value}-${selectMonth.value}-${lastDate}`;
	});
}

// 表のカテゴリー部分で絞り込みを行う処理
document.addEventListener('change', function(e) {
	// 大項目のセレクトボックスを取得
	const selectLctg = document.getElementById('table-lctg-filter');
	// テーブルの行を取得
	const transactionList = document.querySelectorAll('.transaction_list');
	// 大項目のテキスト(span要素)一覧を取得
	const transactionLctg = document.querySelectorAll('.lctg-text');
	
	// 大項目のセレクトボックスの値が変化した場合
	if (e.target.id === 'table-lctg-filter') {
		// 大項目のセレクトボックスの値を取得
		const selectLctgValue = selectLctg.value;

		transactionList.forEach(function(lctgList ,index) {
			// 大項目のテキストにセレクトボックスの値が含まれている場合
			if(transactionLctg[index].textContent.indexOf(selectLctgValue) !== -1) {
				lctgList.style.display = "table-row";
				// 大項目のテキストにセレクトボックスの値が含まれていない場合
			} else {
				lctgList.style.display = "none";
			}
		});
	} 
	
	// 中項目のセレクトボックスの取得
	const selectMctg = document.getElementById('table-mctg-filter');
	// 中項目のテキスト(span要素)一覧を取得
	const transactionMctg = document.querySelectorAll('.mctg-text');
	
	// 中項目のセレクトボックスの値が変化した場合
	if (e.target.id === 'table-mctg-filter') {
		// 中項目のセレクトボックスの値を取得
		const selectMctgValue = selectMctg.value;
		
		transactionList.forEach(function(lctgList, index) {
			// 中項目のテキストにセレクトボックスの値が含まれている場合
			if(transactionMctg[index].textContent.indexOf(selectMctgValue) !== -1) {
				lctgList.style.display = "table-row";
				// 中項目のテキストにセレクトボックスの値が含まれていない場合
			} else {
				lctgList.style.display = "none";
			} 
		});
	}
});