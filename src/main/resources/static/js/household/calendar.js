const text = document.getElementById('cal-text');
text.addEventListener('click', function() {
	const weeks = ['日', '月', '火', '水', '木', '金', '土']
	const date = new Date()
	let year = date.getFullYear()
	let month = date.getMonth() + 1
	const config = {
		show: 1,
	}

	function showCalendar(year, month) {
		for (i = 0; i < config.show; i++) {
			const calendarHtml = createCalendar(year, month)
			const div = document.createElement('div')
			div.classList.add('datepicker')
			div.innerHTML = calendarHtml
			document.querySelector('#calendar').appendChild(div)

			month++
			if (month > 12) {
				year++
				month = 1
			}
		}
	}

	function createCalendar(year, month) {
		const startDate = new Date(year, month - 1, 1) // 月の最初の日を取得
		const endDate = new Date(year, month, 0) // 月の最後の日を取得
		const endDayCount = endDate.getDate() // 月の末日
		const lastMonthEndDate = new Date(year, month - 2, 0) // 前月の最後の日の情報
		const lastMonthendDayCount = lastMonthEndDate.getDate() // 前月の末日
		const startDay = startDate.getDay() // 月の最初の日の曜日を取得
		let dayCount = 1 // 日にちのカウント
		let calendarHtml = '' // HTMLを組み立てる変数

		calendarHtml += `<table class="cal-table">`
		calendarHtml += '<thead>'
		calendarHtml += '<tr>'
		calendarHtml += `<th id="prev">` + '<' + '</th>'
		calendarHtml += `<th colspan="5" class="switch">` + month + '月' + year + '</th>'
		calendarHtml += `<th id="next">` + '>' + '</th>'
		calendarHtml += '</tr>'

		calendarHtml += '<tr>'
		// 曜日の行を作成
		for (let i = 0; i < weeks.length; i++) {
			calendarHtml += `<th class="dow">` + weeks[i] + '</th>'
		}
		calendarHtml += '</tr>'
		calendarHtml += '</thead>'

		for (let w = 0; w < 6; w++) {
			calendarHtml += '<tr>'

			for (let d = 0; d < 7; d++) {
				if (w == 0 && d < startDay) {
					// 1行目で1日の曜日の前
					let num = lastMonthendDayCount - startDay + d + 1
					calendarHtml += '<td class="is-disabled">' + num + '</td>'
				} else if (dayCount > endDayCount) {
					// 末尾の日数を超えた
					let num = dayCount - endDayCount
					calendarHtml += '<td class="is-disabled">' + num + '</td>'
					dayCount++
				} else {
					calendarHtml += `<td class="calendar_td" tabindex="-1" data-date="${year}/${month}/${dayCount}">${dayCount}</td>`
					dayCount++
				}
			}
			calendarHtml += '</tr>'
		}
		calendarHtml += '</table>'

		return calendarHtml
	}

	function moveCalendar(e) {
		document.querySelector('#calendar').innerHTML = ''

		if (e.target.id === 'prev') {
			month--

			if (month < 1) {
				year--
				month = 12
			}
		}

		if (e.target.id === 'next') {
			month++

			if (month > 12) {
				year++
				month = 1
			}
		}

		showCalendar(year, month)
	}

	// 日付をクリックすると入力欄とspanの内容がクリックされた要素になる処理
	document.addEventListener("click", function(e) {
		const calInput = document.getElementById('cal-date-input');
		const span = document.getElementById('date-span');
		// イベントのターゲットに"calendar_td"というクラス名が含まれていれば処理を実行
		if (e.target.classList.contains("calendar_td")) {
			const value = e.target.dataset.date
			span.textContent = value
			calInput.value = value.split('/').join('-')
		}
		// 前後の月に移動するイベント
		document.querySelector('#prev').addEventListener('click', moveCalendar)
		document.querySelector('#next').addEventListener('click', moveCalendar)
	})

	document.addEventListener('click', function(e) {
		if (!e.target.closest('.input-date') && !e.target.closest('#prev') && !e.target.closest('#next')) {
			//カレンダー要素の外をクリックした時、カレンダーを非表示にする
			document.querySelector('.cal-table').style.display = "none"
		} else {
			//カレンダー要素ないの場合は、表示する
			document.querySelector('.cal-table').style.display = "block"
		}
	})

	showCalendar(year, month)
}, { once: true })
