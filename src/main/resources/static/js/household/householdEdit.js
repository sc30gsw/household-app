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

// 年月日を描画する関数
function drawEditDate() {
	startDate.replace(/-/g, '0');
	// 年を選択するselectボックスの取得
	const selectYear = document.querySelector('.search-year-filter');
	// 月を取得するselectボックスの取得
	const selectMonth = document.querySelector('.search-month-filter');
	// 年の取得
	let year = startDate.substr(0, 4);
	// 月の取得
	let month = startDate.substr(5, 2);
	if (month.includes('-')) {
		month = '0' + month.replace(/-/g, '');
	}

	// 年月日を表示する処理
	function showDetailDate(year, month) {
		// 年月日を作成する関数の呼び出し
		const drawDateHtml = createDetailDate(year, month)

		// div要素作成
		const div = document.createElement('div')
		// div要素にクラスを追加
		div.classList.add('date-range-datepicker')
		// div要素の子要素にカレンダーを設定
		div.innerHTML = drawDateHtml

		// <div id="draw-date-range"></div>内に上記で生成した要素を入れる
		document.querySelector('#draw-date-range').appendChild(div)

		month++
		// 月が12ヶ月を超えたら次の年の1月を表示するようにする
		if (month > 12) {
			year++
			month = 1
		}
	}

	// 年月日を作成する関数
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
		drawDateHtml += `<span class="draw-date-header-title">
							<h2 id="detail-draw-title" class="draw-date-detail-title" 
								data-startdate="${year}-${month}-${startDayCount}" 
								data-enddate="${year}-${month}-${endDayCount}">`
			+ year + '/' + month + '/' + startDayCount +
			'-' + year + '/' + month + '/' + endDayCount +
			'</h2>' +
			'</span>'

		return drawDateHtml
	}

	// 検索ボタンの取得
	const submit = document.getElementById('search-date-btn');

	submit.addEventListener('click', function() {
		// <div id="draw-date-range"></div>内のHTMLを空にする
		document.querySelector('#draw-date-range').innerHTML = ''
		// 月が1月より下の場合、年から1を引いて、月を12にする
		year = Number(selectYear.value);
		month = Number(selectMonth.value);

		// 年月日を表示する関数の呼び出し
		showDetailDate(year, month)
	})

	showDetailDate(year, month)
}

// ページ読み込み時にチェックボックスのチェックを外す処理
function checkOut() {
	const chbx = document.querySelectorAll('.table-check');
	chbx.forEach(function(ch) {
		if (ch.checked) {
			ch.checked = false;
		}
	});
}

// クリックされた(onclickで呼び出し)チェックボックス以外のチェックを外す処理
// チェックされたチェックボックスの行の値をフォームに格納する
function checkBox(obj) {
	// クリックされたオブジェクトを取得
	let that = obj.firstElementChild.firstElementChild;

	// 全てのチェックボックスを取得
	let boxes = document.querySelectorAll('.table-check');

	for (let i = 0; i < boxes.length; i++) {
		// チェックボックスにチェックが入っていた場合
		if (boxes[i].checked) {
			// チェックを外す
			boxes[i].checked = false;
		}

		that.checked = true;
	}
	
	// セクション要素を取得
	const section = document.getElementById('edit-form-section');
	// セクション要素を表示する
	section.style.display = "block";
	
	// フォームの要素を取得
	const householdIdInput = document.getElementById('householdId')
	const dateInput = document.getElementById('activeDate');
	const paymentInput = document.getElementById('payment');
	const depositInput = document.getElementById('deposit');
	const noteInput = document.getElementById('note');
	const categoryIdInput = document.getElementById('categoryId');
	const deleteHouseholdId = document.getElementById('delete-householdId');
	const dropdownMenuLink = document.getElementById('dropdownMenuLink');
	const dropdownSubLink = document.getElementById('js-middle-category-selected');
	const dateField = document.querySelector('.date-span-second');

	// テーブルのtr要素を取得
	const tableLists = document.querySelectorAll('.transaction_list');
	const lists = [].slice.call(tableLists);
	// テーブルの行番号を取得
	const index = lists.indexOf(obj);

	// テーブルの日付を取得
	const tableDate = document.querySelectorAll('.transaction-date');
	const tableDateSpan = tableDate[index].firstElementChild.firstElementChild.dataset.tabledate;

	// テーブルの家計簿ID取得
	const tableHouseholdId = tableDate[index].firstElementChild.dataset.tableid;

	// テーブルの内容を取得
	let tableNote = tableDate[index].firstElementChild.dataset.tablenote;
	if (tableNote === undefined) {
		tableNote = '';
	}

	// テーブルの支出を取得
	const tablePaymentAmount = document.querySelectorAll('.transaction-amount-minus');
	let tablePayment = tablePaymentAmount[index].firstElementChild.firstElementChild.dataset.tablepay;
	if (tablePayment === undefined) {
		tablePayment = 0;
	}

	// テーブルの収入取得
	const tableDepositAmount = document.querySelectorAll('.transaction-amount-deposit');
	let tableDeposit = tableDepositAmount[index].firstElementChild.firstElementChild.dataset.tabledepo;
	if (tableDeposit === undefined) {
		tableDeposit = 0;
	}

	// テーブルのカテゴリーID取得
	const tableLCategories = document.querySelectorAll('.transaction-lctg');
	const tableCategoryId = tableLCategories[index].firstElementChild.dataset.tablecategoryid;

	// 大項目の取得
	const tableLCategory = tableLCategories[index].firstElementChild.firstElementChild.textContent;

	// サブ項目の取得
	const tableMCategories = document.querySelectorAll('.transaction-mctg');
	const tableMCategory = tableMCategories[index].firstElementChild.firstElementChild.textContent;
	
	// フォーム要素の内容をチェクされた行の内容にする
	householdIdInput.value = tableHouseholdId;
	dateInput.value = tableDateSpan;
	dateField.textContent = tableDateSpan;
	paymentInput.value = tablePayment;
	depositInput.value = tableDeposit;
	deleteHouseholdId.value = tableHouseholdId;
	categoryIdInput.value = tableCategoryId;
	noteInput.value = tableNote;
	dropdownMenuLink.textContent = tableLCategory;
	dropdownSubLink.textContent = tableMCategory;
	
	// ドロップダウンのスタイルの変更
	dropdownMenuLink.style.backgroundColor ="#f7f7f7";
	dropdownMenuLink.style.color = "#333";
	dropdownMenuLink.style.borderColor = "rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25)";
	dropdownSubLink.style.backgroundColor = "#f7f7f7";
	dropdownSubLink.style.color = "#333";
	dropdownSubLink.style.borderColor = "rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25)";
}