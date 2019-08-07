function loadPage() {
	displayOrderList();
	displayTicketsInShoppingBasket();
	addEventListerners();
}

function addEventListerners() {
	finalizePayButton = document.getElementById("finalizepaymentbutton");
	finalizePayButton.addEventListener("click", () => finalizePay());
	
	cancelOrderButtons = document.getElementsByClassName("cancelorder");	
	for (button of cancelOrderButtons) {
		button.addEventListener("click", () => cancelOrder());
	}
}

function displayTicketsInShoppingBasket() {
	savedNumberOfTickets = 0;
	tickets = Object.values(localStorage);
    i = tickets.length;
    while (i--) {
		savedTicket = JSON.parse(tickets[i]);
		number = savedTicket["total"];
		savedNumberOfTickets += number;
    }
	document.getElementsByClassName("badge")[0].innerHTML = savedNumberOfTickets;
}

function getOrders() {
	orderList = [];
	tickets = Object.values(localStorage),
    i = tickets.length;
    while (i--) {
		orderList[i] = JSON.parse(tickets[i]);
    }
	return orderList;
}

function displayOrderList() {
	template = document.getElementById("ticket");
	main = document.querySelector("main");
	finalizePayButton = document.getElementById("finalizepaymentbutton");
	orderList = getOrders();
	
	if (orderList.length == 0) {
		finalizePayButton.disabled = true;
	}
	
	for (ticket of orderList) {
		attraction = ticket["parkname"];
		ticketsAdults = ticket["adults"];
		ticketsKids = ticket["kids"];
		totalPrice = calculateTotalPrice(attraction);
		
		newNode = template.content.cloneNode(true);
			
		newNode.querySelector(".name").textContent += attraction;
		newNode.querySelector(".adults").textContent += ticketsAdults;
		newNode.querySelector(".kids").textContent += ticketsKids;
		newNode.querySelector(".total").querySelector(".price").textContent += totalPrice;
			
		main.insertBefore(newNode, finalizePayButton);
	}
}	

function finalizePay() {
	orders = getOrders();
	placeOrder(orders);
	
	localStorage.clear();
	window.location = "orderplaced.html";
}

async function placeOrder(orders) {
	try {
		let res = await fetch('/api/placeorder', {
			method: 'POST',
			body: JSON.stringify(orders),
			headers: { 'Content-type': 'application/json' },
		})
		let response = await res;
		console.log(res);
	} catch(e) {
		console.log(e);
	}
};

function cancelOrder() {
	attraction = event.target.parentNode.children[0].innerText.substring(10);
	localStorage.removeItem(attraction);
	location.reload();
}	

function calculateTotalPrice(attraction) {
	ticket = JSON.parse(localStorage.getItem(attraction));
	adultsPrice = ticket["adultsPrice"];
	numberOfAdults = ticket["adults"];
	kidsPrice = ticket["kidsPrice"];
	numberOfKids = ticket["kids"];
	minNumberOfAdults = ticket["minNumberOfAdults"];
	minNumberOfKids = ticket["minNumberOfKids"];
	totalPrice = adultsPrice * numberOfAdults + kidsPrice * numberOfKids;
	discount = ticket["discount"];
	if (numberOfAdults >= minNumberOfAdults && numberOfKids >= minNumberOfKids) {
		totalPrice *= (100 - discount) / 100;
	}
	ticket["totalPrice"] = totalPrice;
	localStorage.setItem(attraction, JSON.stringify(ticket));
	return totalPrice.toFixed(2);
}