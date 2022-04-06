// カレンダーを描画する関数
function drawCalendar() {
	startDate.replace(/-/g, '0');
	// 年を選択するselectボックスの取得
	const selectYear = document.querySelector('.search-year-filter');
	// 月を取得するselectボックスの取得
	const selectMonth = document.querySelector('.search-month-filter');
	// 曜日を配列に格納する
	const weeks = ['日', '月', '火', '水', '木', '金', '土']
	// 年の取得
	let year = startDate.substr(0, 4);
	// 月の取得
	let month = startDate.substr(5, 2).replace(/-/g, '');

	// カレンダーを表示する処理
	function showCalendar(year, month) {
		// カレンダーを作成する関数の呼び出し
		const calendarHtml = createCalendar(year, month)

		// div要素作成
		const div = document.createElement('div')
		// div要素にクラスを追加
		div.classList.add('detail-datepicker')
		// div要素の子要素にカレンダーを設定
		div.innerHTML = calendarHtml

		// <div id="detail-calendar"></div>内に上記で生成した要素を入れる
		document.querySelector('#detail-calendar').appendChild(div)

		month++
		// 月が12ヶ月を超えたら次の年の1月を表示するようにする
		if (month > 12) {
			year++
			month = 1
		}
	}

	// カレンダーを作成する関数
	function createCalendar(year, month) {
		// 月の最初の日を取得
		const startDate = new Date(year, month - 1, 1)
		// 月の最初の日を取得
		const startDayCount = startDate.getDate()
		// 月の最後の日を取得
		const endDate = new Date(year, month, 0)
		// 月の末日
		const endDayCount = endDate.getDate()
		// 前月の最後の日の情報
		const lastMonthEndDate = new Date(year, month - 2, 0)
		// 前月の末日
		const lastMonthendDayCount = lastMonthEndDate.getDate()
		// 月の最初の日の曜日を取得
		const startDay = startDate.getDay()
		// 日にちのカウント
		let dayCount = 1
		// HTMLを組み立てる変数
		let calendarHtml = ''

		calendarHtml += `<span class="fc-header-title"><h2 id="cal-detail-title" class="cal-detail-title" data-startdate="${year}-${month}-${startDayCount}" data-enddate="${year}-${month}-${endDayCount}">` + year + '/' + month.replace('0','') + '/' + startDayCount + '-' + year + '/' + month.replace('0','') + '/' + endDayCount + '</h2></span>'
		calendarHtml += `<table class="cal-detail-table">`
		calendarHtml += '<thead>'

		calendarHtml += '<tr>'
		// 曜日の行を作成
		for (let i = 0; i < weeks.length; i++) {
			calendarHtml += `<th class="detaildow">` + weeks[i] + '</th>'
		}
		calendarHtml += '</tr>'
		calendarHtml += '</thead>'

		for (let w = 0; w < 6; w++) {
			calendarHtml += '<tr>'

			for (let d = 0; d < 7; d++) {
				if (w == 0 && d < startDay) {
					// 1行目で1日の曜日の前の日付を取得
					let num = lastMonthendDayCount - startDay + d + 1;
					// 1日の前の日付と符号する年を取得
					const endYear = lastMonthEndDate.getFullYear();
					// 1日の前の日付と符号する月を取得
					const endMonth = lastMonthEndDate.getMonth() + 2;
					calendarHtml += `<td class="is-detail-disabled" data-date="${endYear}-${endMonth}-${num}">${num}</td>`
				} else if (dayCount > endDayCount) {
					// 末尾の日数を超えた場合の日付を取得
					let num = dayCount - endDayCount
					// 末尾の日数を超えた場合の日の情報を取得
					const nextMonthStartDate = new Date(year, month + 2, 0);
					// 末尾の日数を超えた場合の日と符号する年を取得
					const nextYear = nextMonthStartDate.getFullYear();
					// 末尾の日数を超えた場合の日と符号する月を取得
					let nextMonth = nextMonthStartDate.getMonth();
					// 11月の場合のみ、末尾の日数を超えた場合の日と符号する月に12月を格納する
					if (month === 11) {
						nextMonth = 12;
					}
					calendarHtml += `<td class="is-detail-disabled" data-date="${nextYear}-${nextMonth}-${num}">${num}</td>`
					dayCount++
				} else {
					calendarHtml += `<td class="calendar_tdetail_d" tabindex="-1" data-date="${year}-${month}-${dayCount}">${dayCount}</td>`
					dayCount++
				}
			}
			calendarHtml += '</tr>'
		}
		calendarHtml += '</table>'

		return calendarHtml
	}

	// 検索ボタンの取得
	const submit = document.getElementById('search-date-btn');

	submit.addEventListener('click', function() {
		// <div id="calendar"></div>内のHTMLを空にする
		document.querySelector('#detail-calendar').innerHTML = ''
		// 年・月を数値に変換する
		year = Number(selectYear.value);
		month = Number(selectMonth.value);

		// カレンダーを表示する関数の呼び出し
		showCalendar(year, month)
	})

	showCalendar(year, month)
}

// 年月日を描画する関数
function drawDetailDate() {
	startDate.replace(/-/g, '0');
	// 年を選択するselectボックスの取得
	const selectYear = document.querySelector('.search-year-filter');
	// 月を取得するselectボックスの取得
	const selectMonth = document.querySelector('.search-month-filter');
	// 年の取得
	let year = startDate.substr(0, 4);
	// 月の取得
	let month = startDate.substr(5, 2).replace(/-/g, '');

	// カレンダーを表示する処理
	function showDetailDate(year, month) {
		// カレンダーを作成する関数の呼び出し
		const drawDateHtml = createDetailDate(year, month)

		// div要素作成
		const div = document.createElement('div')
		// div要素にクラスを追加
		div.classList.add('date-range-datepicker')
		// div要素の子要素にカレンダーを設定
		div.innerHTML = drawDateHtml

		// <div id="detail-calendar"></div>内に上記で生成した要素を入れる
		document.querySelector('#draw-date-range').appendChild(div)

		month++
		// 月が12ヶ月を超えたら次の年の1月を表示するようにする
		if (month > 12) {
			year++
			month = 1
		}
	}

	// カレンダーを作成する関数
	function createDetailDate(year, month) {
		// 月の最初の日を取得
		const startDate = new Date(year, month - 1, 1)
		// 月の最初の日を取得
		const startDayCount = startDate.getDate()
		// 月の最後の日を取得
		const endDate = new Date(year, month, 0)
		// 月の末日
		const endDayCount = endDate.getDate()
		// HTMLを組み立てる変数
		let drawDateHtml = ''

		// HTMLを組み立てる
		drawDateHtml += `<span class="draw-date-header-title"><h2 id="detail-draw-title" class="draw-date-detail-title" data-startdate="${year}-${month}-${startDayCount}" data-enddate="${year}-${month}-${endDayCount}">` + year + '/' + month.replace('0','') + '/' + startDayCount + '-' + year + '/' + month.replace('0','') + '/' + endDayCount + '</h2></span>'

		return drawDateHtml
	}
	
	// 検索ボタンの取得
	const submit = document.getElementById('search-date-btn');

	submit.addEventListener('click', function() {
		// <div id="calendar"></div>内のHTMLを空にする
		document.querySelector('#draw-date-range').innerHTML = ''
		// 月が1月より下の場合、年から1を引いて、月を12にする
		year = Number(selectYear.value);
		month = Number(selectMonth.value);

		// カレンダーを表示する関数の呼び出し
		showDetailDate(year, month)
	})

	showDetailDate(year, month)
}