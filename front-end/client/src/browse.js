function loadPage() {
	//loadRecipes();
	//displayTicketsInShoppingBasket();
}

/*function addEventListerners() {
	orderbuttons = document.querySelectorAll('.orderbutton');	
	for (button of orderbuttons) {
		button.addEventListener("click", () => orderButtonClicked());
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

function orderButtonClicked() {
	attraction = event.target.parentNode.parentNode.children[0].textContent;
	numberOfAdults = parseInt(event.target.parentNode.children[2].value);
	
	if (numberOfAdults < 0 || !numberOfAdults) {
		numberOfAdults = 0;
	}
	numberOfKids = parseInt(event.target.parentNode.children[4].value);
	if (numberOfKids < 0 || !numberOfKids) {
		numberOfKids = 0;
	}
	if (numberOfAdults > 0 || numberOfKids > 0) {
		adultsPrice = parseInt(event.target.parentNode.querySelector(".adultprice").querySelector(".price").textContent);
		kidsPrice = parseInt(event.target.parentNode.querySelector(".kidsprice").querySelector(".price").textContent);
		minNumberOfAdults = parseInt(event.target.parentNode.querySelector(".adults").textContent);
		minNumberOfKids = parseInt(event.target.parentNode.querySelector(".child").textContent);
		discount = parseInt(event.target.parentNode.querySelector(".percentage").textContent);
		saveOrderInShoppingBasket(attraction, numberOfAdults, numberOfKids, adultsPrice, kidsPrice, minNumberOfAdults, minNumberOfKids, discount);
	}
}

function saveOrderInShoppingBasket(attraction, numberOfAdults, numberOfKids, adultsPrice, kidsPrice, minNumberOfAdults, minNumberOfKids, discount) {
	if (localStorage.getItem(attraction)) {
		savedTicket = JSON.parse(localStorage.getItem(attraction));
		savedNumberOfAdults = parseInt(savedTicket["adults"]);
		numberOfAdults += savedNumberOfAdults;
		savedNumberOfKids = parseInt(savedTicket["kids"]);
		numberOfKids += savedNumberOfKids;
	}
	
	totalTickets = numberOfAdults + numberOfKids;
	
	ticket = { 
        parkname: attraction,
        adults: numberOfAdults,
        kids: numberOfKids,
        total: totalTickets,
		adultsPrice: adultsPrice,
		kidsPrice: kidsPrice,
		minNumberOfAdults: minNumberOfAdults,
		minNumberOfKids: minNumberOfKids,
		discount: discount
    };
	
	localStorage.setItem(attraction, JSON.stringify(ticket));
	
	displayTicketsInShoppingBasket();
}*/

async function loadRecipes() {
	try {
		response = await fetch("api/recipes");
		attractionList = await response.json();
		showAttractionList(recipeList);
	} catch (error) {
		console.error(error);
	}
}

function creatNodes(recipeList) {
	template = document.getElementById("recipe");
	main = document.querySelector("main");
	
	for (recipe of recipeList) {
		newNode = template.content.cloneNode(true);
		
		newNode.querySelector(".name").textContent += recipe["name"];
		newNode.querySelector(".categories").textContent += recipe["categories"];
		newNode.querySelector(".rating").textContent += recipe["rating"];
		newNode.querySelector(".preptime").textContent += recipe["preptime"];
		newNode.querySelector(".ingredients").textContent += recipe["ingredients"];
		newNode.querySelector(".instructions").textContent += recipe["instructions"];
			
		main.appendChild(newNode);
	}
	//addEventListerners()
}

/*function showTotalPrice() {
	orderNode = event.target.parentNode;
	adultsPrice = parseInt(orderNode.querySelector(".adultprice").querySelector(".price").textContent);
	numberOfAdults = orderNode.querySelector(".numberofadults").value || 0;
	kidsPrice = parseInt(orderNode.querySelector(".kidsprice").querySelector(".price").textContent);
	numberOfKids = orderNode.querySelector(".numberofkids").value || 0;
	minNumberOfAdults = parseInt(orderNode.querySelector(".adults").textContent);
	minNumberOfKids = parseInt(orderNode.querySelector(".child").textContent);
	totalPrice = adultsPrice * numberOfAdults + kidsPrice * numberOfKids;
	discount = parseInt(orderNode.querySelector(".percentage").textContent);
	if (numberOfAdults >= minNumberOfAdults && numberOfKids >= minNumberOfKids) {
		totalPrice *= (100 - discount) / 100;
	}
	orderNode.querySelector(".total").querySelector(".price").textContent = totalPrice.toFixed(2);
}*/
