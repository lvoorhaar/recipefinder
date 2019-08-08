function loadPage() {
	loadRecipes();
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
}*/





async function loadRecipes() {
	try {
		response = await fetch("api/loadrecipes",  {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                }
            });
		console.log("fetching recipes...");
		recipeList = await response.json();
		creatNodes(recipeList);
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
		newNode.querySelector(".preptime").textContent += recipe["preptime"] + " minutes";
		newNode.querySelector(".ingredients").textContent += recipe["ingredients"];
		newNode.querySelector(".instructions").textContent += recipe["instructions"];
			
		main.appendChild(newNode);
	}
	//addEventListerners()
}


