function loadPage() {
	loadRecipes();
}

function addEventListerners() {
	nameButtons = document.querySelectorAll(".namebutton");

	for (n of nameButtons) {
		n.addEventListener("click", function() {
			this.classList.toggle("active");
			content = this.nextElementSibling;
			if (content.style.maxHeight){
				content.style.maxHeight = null;
			} else {
				content.style.maxHeight = content.scrollHeight + "px";
			} 
		});
	}

	//orderbuttons = document.querySelectorAll('.orderbutton');	
	//for (button of orderbuttons) {
	//	button.addEventListener("click", () => orderButtonClicked());
	//}
}

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
		//console.log(recipe);

		newNode = template.content.cloneNode(true);

		newNode.querySelector(".namebutton").textContent += recipe["name"];

		categories = recipe["categories"];
		categorytext = "";
		i = categories.length;
		for (category of categories) {
			categorytext += category.toLowerCase();
			i--;
			if (i > 0) categorytext += ", ";
		}

		newNode.querySelector(".categories").textContent += categorytext;

		rating = recipe["rating"];
		ratingtext = "";
		for (i = 1; i <= rating; i++) {
			ratingtext += "☆";
		}
		newNode.querySelector(".rating").textContent += ratingtext;

		newNode.querySelector(".preptime").textContent += recipe["preptime"] + " minutes";


		ingredientsNode = newNode.querySelector(".ingredients");
		currentIngredientNode = newNode.querySelector(".ingredient");

		ingredients = recipe["ingredients"];
		i = ingredients.length
		for (ingredient of ingredients) {
			ingredientText = ingredient["amount"] + " " + ingredient["unit"] + " " + ingredient["name"];
			currentIngredientNode.textContent = ingredientText;
			i--;
			if (i > 0) {
				nextNode = currentIngredientNode.cloneNode(true);
				ingredientsNode.appendChild(nextNode);
				currentIngredientNode = nextNode;
			}
		}

		newNode.querySelector(".instructions").innerHTML += recipe["instructions"];

		main.appendChild(newNode);
	}

	addEventListerners()
}


	