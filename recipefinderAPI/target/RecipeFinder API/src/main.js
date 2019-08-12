function loadSearchPage() {
	addSearchFields();
	addEventListernersSearch();
}

function loadBrowsePage() {
	loadRecipes();
}

function addEventListernersSearch() {
	searchButton = document.getElementById("searchbutton");
	searchButton.addEventListener("click", search);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addSearchFields);
}

function addEventListernersRecipe() {
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
}

function search() {
	inputFields = document.querySelectorAll(".ingredientsearchfield");
	parentField = document.getElementById("searchfields");
	ingredients = [];
	for (field of inputFields) {
		if (field.value) {
			ingredient = field.value;
			ingredients.push(ingredient);
		} else {
			parentField.removeChild(field);
		}
	}
	console.log(ingredients);
	searchPOST(ingredients);
}

function addSearchFields() {
	template = document.getElementById("searchfield");
	fieldDiv = document.getElementById("searchfields");
	for (i = 0; i < 10; i++) {
		newNode = template.content.cloneNode(true);
		fieldDiv.appendChild(newNode);
	}
}

async function searchPOST(ingredients) {
	try {
		response = await fetch("api/searchrecipes",  {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
			body: JSON.stringify(ingredients)
            });
		console.log("searching recipes...");
		recipeList = await response.json();
		creatNodes(recipeList);
	} catch (error) {
		console.error(error);
	}
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
	recipesDiv = document.getElementById("recipes");

	for (recipe of recipeList) {

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

		recipesDiv.appendChild(newNode);
	}

	addEventListernersRecipe();
}


	