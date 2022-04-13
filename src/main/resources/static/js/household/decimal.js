// 小数点の切り捨てを行う関数
function decimalFormat() {
	const decimal = document.querySelectorAll('.outgo-number-decimal');
	
	decimal.forEach(function(d) {
		// 小数点以下2桁残して切り捨てる
		const decimalData = Math.round(d.dataset.decimal * 100) / 100;
		// %表示する
		d.textContent = `${decimalData}%`
	})
}