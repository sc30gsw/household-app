// タブメニュー表示の動作を行う処理
function tabMenu() {
	// モーダルウィンドウ要素の取得
	const modal = document.getElementById('detail-modal');
	// デフォルトではモーダルウィンドウは非表示とする
	modal.style.display = "none";
	
	// タブメニューのメニューバー部分を取得
	const tabs = document.getElementsByClassName('tab-menu-list');
	
	// タブメニューのメニューバーがクリックされたときにメニューコンテンツを切り替える
	for(let i = 0; i< tabs.length; i ++) {
		tabs[i].addEventListener('click', tabSwitch, false);
	};
	
	// タブメニューのコンテンツを切り替える処理
	function tabSwitch() {
		// 最初のメニューからクラス属性を除去する
		document.getElementsByClassName('is-tab-active')[0].classList.remove('is-tab-active');
		// クリックされたメニューバーにクラス属性を付与する
		this.classList.add('is-tab-active');
		
		// 最初に表示されているメニューコンテンツからクラス属性を除去する
		document.getElementsByClassName('is-show')[0].classList.remove('is-show');
		// 連想配列の作成
		const arrayTabs = Array.prototype.slice.call(tabs);
		// クリックされた要素のメニューコンテンツが何番目かを取得する
		const index = arrayTabs.indexOf(this);
		
		// クリックされた要素のメニューコンテンツにクラス属性を付与
		document.getElementsByClassName('panel')[index].classList.add('is-show');
	};
};

// 該当箇所をクリックしたときにモーダルウィンドウを表示する処理
document.addEventListener('click', function(e) {
	// <body>タグ部を取得
	const detailBody = document.getElementById('detail-body');
	// <header>タグ部を取得
	const header = document.querySelector('.header');
	// コンテンツ部の要素を取得
	const container = document.querySelector('.container-large');
	// <footer>タグ部を取得
	const footer = document.querySelector('.footer-contents-container');
	// モーダルウィンドウの要素を取得
	const modal = document.getElementById('detail-modal');
	
	// 「手入力」ボタンまたはカレンダー内の「ペン」アイコンをクリックした場合
	if(e.target.closest('.cf-new-btn') || e.target.closest('.detail-cal-icon-link')){
		// <body>タグ内の背景色を変更
		detailBody.style.backgroundColor = "rgba(0,0,0,0.5)";
		// <header>の透明度を変更
		header.style.opacity = "0.1";
		// コンテンツ部の透明度を変更
		container.style.opacity = "0.1";
		// <footer>部の透明度の変更
		footer.style.opacity = "0.1";
		// モーダルウィンドウからフェードアウトのアニメーションを除去する
		modal.classList.remove('modal-fade-out');
		// モーダルウィンドウを表示する
		modal.style.display = "block";
		
		// <header>部、コンテンツ部、<footer>部、モーダルウィンドウの「閉じる(×)」ボタンをクリックした場合
	} else if(e.target.closest('.header') || e.target.closest('.container-large') 
		|| e.target.closest('.footer-contents-container') || e.target.closest('.modal-close')) {
			
		// <doby>タグ内の背景色をもとに戻す
		detailBody.style.backgroundColor = "#fff";
		// <header>の透明度をもとに戻す
		header.style.opacity = "1";
		// コンテンツ部の透明度をもとに戻す
		container.style.opacity = "1";
		// <footer>の透明度をもとに戻す
		footer.style.opacity = "1";
		// モーダルウィンドウにクラス属性を追加
		modal.classList.add('modal-fade-out');
		// モーダルウィンドウを非表示にする(フェードアウトのアニメーションを付与)
		setTimeout(function () {
			modal.style.display = "none";
		}, 1000);
	} 
});