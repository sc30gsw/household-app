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
		if(window.pageYOffset > 10) {
			toTopBtn.style.opacity = '1';
		} else if(window.pageYOffset < 10) {
			toTopBtn.style.opacity = '0';
		}
	});
}