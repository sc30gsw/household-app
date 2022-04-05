// カレンダーを描画する関数
function drawCalendar() {
	// 曜日を配列に格納する
	const weeks = ['日', '月', '火', '水', '木', '金', '土']
	// 日付を取得する
	const date = new Date()
	// 年の取得
	let year = date.getFullYear()
	// 月の取得
	let month = date.getMonth() + 1

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

		// HTMLを組み立てる
		calendarHtml += `<div class="cal-span-field"><span class="prev">` + '＜' + '</span>'
		calendarHtml += `<span class="fc-header-title"><h2 id="cal-detail-title" class="cal-detail-title" data-startdate="${year}-${month}-${startDayCount}" data-enddate="${year}-${month}-${endDayCount}">` + year + '/' + month + '/' + startDayCount + '-' + year + '/' + month + '/' + endDayCount + '</h2></span>'
		calendarHtml += `<span class="next">` + '＞' + '</span></div>'
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

	// 「次へ」ボタンと「前に」ボタンでカレンダーを動かす(来月・先月の表示)処理
	function moveCalendar(e) {
		// <div id="calendar"></div>内のHTMLを空にする
		document.querySelector('#detail-calendar').innerHTML = ''

		// イベントのターゲットが「前に」ボタンの場合、1月分の値を引く
		if (e.target.className === 'prev') {
			month--

			// 月が1月より下の場合、年から1を引いて、月を12にする
			if (month < 1) {
				year--
				month = 12
			}
		}

		// イベントのターゲットが「次へ」ボタンの場合、1月分の値を加える
		if (e.target.className === 'next') {
			month++

			// 月が12月より上の場合、年に1を加え、月を1にする
			if (month > 12) {
				year++
				month = 1
			}
		}

		// カレンダーを表示する関数の呼び出し
		showCalendar(year, month)
	}

	// 前後の月にカレンダーを動かす処理
	document.addEventListener("mouseover", function() {

		// 「次へ」「前に」ボタンがクリックされた時、カレンダーを前後に動かす関数を呼び出す
		document.querySelectorAll('.prev').forEach(function(prev) {
			prev.addEventListener('click', moveCalendar)
		})
		document.querySelectorAll('.next').forEach(function(next) {
			next.addEventListener('click', moveCalendar)
		})

		const startDateInput = document.getElementById('detail-start-date')
		const endDateInput = document.getElementById('detail-end-date');
		const inputValue = document.getElementById('cal-detail-title');
		// 当月の初日を設定
		startDateInput.value = inputValue.dataset.startdate
		// 当月の末日を設定
		endDateInput.value = inputValue.dataset.enddate
	})

	showCalendar(year, month)
}

// ページを読み込んだときにinputタグに値を入れる
window.addEventListener("load", function() {
	const startDateInput = document.getElementById('detail-start-date')
	const endDateInput = document.getElementById('detail-end-date');
	const inputValue = document.getElementById('cal-detail-title');
	// 当月の初日を設定
	startDateInput.value = inputValue.dataset.startdate
	// 当月の末日を設定
	endDateInput.value = inputValue.dataset.enddate
})

// 年月日を描画する関数
function drawDetailDate() {
	// 日付を取得する
	const date = new Date()
	// 年の取得
	let year = date.getFullYear()
	// 月の取得
	let month = date.getMonth() + 1

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
		drawDateHtml += `<div class="date-span-field"><span class="prev">` + '＜' + '</span>'
		drawDateHtml += `<span class="draw-date-header-title"><h2 id="detail-draw-title" class="draw-date-detail-title" data-startdate="${year}-${month}-${startDayCount}" data-enddate="${year}-${month}-${endDayCount}">` + year + '/' + month + '/' + startDayCount + '-' + year + '/' + month + '/' + endDayCount + '</h2></span>'
		drawDateHtml += `<span class="next">` + '＞' + '</span></div>'

		return drawDateHtml
	}

	// 「次へ」ボタンと「前に」ボタンでカレンダーを動かす(来月・先月の表示)処理
	function moveCalendar(e) {
		// <div id="draw-date-range"></div>内のHTMLを空にする
		document.querySelector('#draw-date-range').innerHTML = ''

		// イベントのターゲットが「前に」ボタンの場合、1月分の値を引く
		if (e.target.className === 'prev') {
			month--

			// 月が1月より下の場合、年から1を引いて、月を12にする
			if (month < 1) {
				year--
				month = 12
			}
		}

		// イベントのターゲットが「次へ」ボタンの場合、1月分の値を加える
		if (e.target.className === 'next') {
			month++

			// 月が12月より上の場合、年に1を加え、月を1にする
			if (month > 12) {
				year++
				month = 1
			}
		}

		// カレンダーを表示する関数の呼び出し
		showDetailDate(year, month)
	}

	// 前後の月にカレンダーを動かす処理
	document.addEventListener("mouseover", function() {

		// 「次へ」「前に」ボタンがクリックされた時、カレンダーを前後に動かす関数を呼び出す
		document.querySelectorAll('.prev').forEach(function(prev) {
			prev.addEventListener('click', moveCalendar)
		})
		document.querySelectorAll('.next').forEach(function(next) {
			next.addEventListener('click', moveCalendar)
		})
		
		const startDateInput = document.getElementById('detail-start-date')
		const endDateInput = document.getElementById('detail-end-date');
		const inputValue = document.getElementById('detail-draw-title');
		// 当月の初日を設定
		startDateInput.value = inputValue.dataset.startdate
		// 当月の末日を設定
		endDateInput.value = inputValue.dataset.enddate
	})

	showDetailDate(year, month)
}