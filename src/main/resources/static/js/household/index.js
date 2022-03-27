function pieChart() {
	const ctx = document.getElementById("assetPie");
	var myPieChart = new Chart(ctx, {
		type: 'pie',
		data: {
			labels: ["収入", "支出"],
			datasets: [{
				backgroundColor: [
					// 収入
					"#136FFF",
					// 支出
					"#FF9872"
				],
				data: [45, 55]
			}]
		},
		options: {
			responsive: true,
			legend: {
				display: false
			},
			ttitle: {
				display: true,
				text: '支出内訳',
				fontSize: 20
			}
		}
	})
};

function subMenu() {
	const parent = document.querySelectorAll('.dropdown-submenu');
	const item = Array.prototype.slice.call(parent, 0);

	item.forEach(function(element) {
		const links = document.querySelectorAll('.c_name');
		const link = Array.prototype.slice.call(links, 0);

		link.forEach(function(l) {
			element.addEventListener("mouseover", function() {
				element.querySelector(".sub_menu").classList.add("open");
			}, false);
			l.addEventListener("mouseout", function() {
				element.querySelector(".sub_menu").classList.remove("open");
			}, false);
		});

	});
};