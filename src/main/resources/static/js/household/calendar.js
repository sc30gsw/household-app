// 「日付」とカレンダーアイコンを囲んでいる要素を取得
const text = document.getElementById('cal-text');

// 取得した要素をクリックした時、イベント発火
text.addEventListener('click', function() {
	// 曜日を配列に格納する
	const weeks = ['日', '月', '火', '水', '木', '金', '土']
	// 日付を取得する
	const date = new Date()
	// 年の取得
	let year = date.getFullYear()
	// 月の取得
	let month = date.getMonth() + 1
	// 1ヶ月のみ表示する
	const config = {
		show: 1,
	}

	// カレンダーを表示する処理
	function showCalendar(year, month) {
		for (i = 0; i < config.show; i++) {
			// カレンダーを作成する関数の呼び出し
			const calendarHtml = createCalendar(year, month)
			
			// div要素作成
			const div = document.createElement('div')
			// div要素にクラスを追加
			div.classList.add('datepicker')
			// div要素の子要素にカレンダーを設定
			div.innerHTML = calendarHtml
			
			// <div id="calendar"></div>内に上記で生成した要素を入れる
			document.querySelector('#calendar').appendChild(div)

			month++
			// 月が12ヶ月を超えたら次の年の1月を表示するようにする
			if (month > 12) {
				year++
				month = 1
			}
		}
	}

	// カレンダーを作成する関数
	function createCalendar(year, month) {
		// 月の最初の日を取得
		const startDate = new Date(year, month - 1, 1)
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
					// 1行目で1日の曜日の前の日付を取得
					let num = lastMonthendDayCount - startDay + d + 1
					calendarHtml += '<td class="is-disabled">' + num + '</td>'
				} else if (dayCount > endDayCount) {
					// 末尾の日数を超えた場合の日付を取得
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

	// 「次へ」ボタンと「前に」ボタンでカレンダーを動かす(来月・先月の表示)処理
	function moveCalendar(e) {
		// <div id="calendar"></div>内のHTMLを空にする
		document.querySelector('#calendar').innerHTML = ''
		
		// イベントのターゲットが「前に」ボタンの場合、1月分の値を引く
		if (e.target.id === 'prev') {
			month--
			
			// 月が1月より下の場合、年から1を引いて、月を12にする
			if (month < 1) {
				year--
				month = 12
			}
		}
		
		// イベントのターゲットが「次へ」ボタンの場合、1月分の値を加える
		if (e.target.id === 'next') {
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

	// 日付をクリックすると入力欄とspanの内容がクリックされた要素になる処理
	document.addEventListener("click", function(e) {
		// hiddenタイプの日付の入力要素を取得する
		const calInput = document.getElementById('cal-date-input');
		// 日付の文字列が表示されるspan要素を取得
		const span = document.getElementById('date-span');
		
		// イベントのターゲットに"calendar_td"というクラス名が含まれていれば処理を実行
		if (e.target.classList.contains("calendar_td")) {
			// クリックされた日付を取得する
			const value = e.target.dataset.date
			// span要素のテキストをクリックされた日付に置き換える
			span.textContent = value
			// 日付の入力欄にクリックされた日付を設定する
			calInput.value = value.split('/').join('-')
		}
		
		// 「次へ」「前に」ボタンがクリックされた時、カレンダーを前後に動かす関数を呼び出す
		document.querySelector('#prev').addEventListener('click', moveCalendar)
		document.querySelector('#next').addEventListener('click', moveCalendar)
	})

	document.addEventListener('click', function(e) {
		//カレンダー要素の外をクリックした時、カレンダーを非表示にする
		if (!e.target.closest('.input-date') && !e.target.closest('#prev') && !e.target.closest('#next')) {
			document.querySelector('.cal-table').style.display = "none"
			//カレンダー要素内の場合は、表示する
		} else {
			document.querySelector('.cal-table').style.display = "block"
		}
	})

	showCalendar(year, month)
	
	// once: trueでカレンダーを1つだけ生成するようにする
}, { once: true })
