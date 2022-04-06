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