function loadSearchPage() {
	addSearchFields();
	addEventListernersSearch();
	addCategoryCheckboxes();
}

function loadBrowsePage() {
	loadRecipes();
}

function loadAddPage() {
	addIngredientInputFields();
	addCategoryCheckboxes();
	addEventListernersAdd();
}

async function loadEditPage() {
	addCategoryCheckboxes();
	loadSingleRecipe();
	addEventListernersEdit();
}

function addEventListernersSearch() {
	searchButton = document.getElementById("searchbutton");
	searchButton.addEventListener("click", search);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addSearchFields);
}

function addEventListernersAdd() {
	addButton = document.getElementById("addnewrecipe");
	addButton.addEventListener("click", addRecipeToDatabase);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addIngredientInputFields);
}

function addEventListernersEdit() {
	addButton = document.getElementById("updaterecipe");
	addButton.addEventListener("click", editRecipe);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addIngredientInputFields);
	deleteRecipeButton = document.getElementById("deleterecipe");
	deleteRecipeButton.addEventListener("click", confirmDeleteRecipe);
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
	searchIngredients = getSearchIngredients();
	if (searchIngredients.length == 0) {
		alert("Please fill in some ingredients first. The search will only return recipes that use some of those ingredients.");
		addSearchFields();
	} else {
		searchCategories = getCategories();
		searchMaxPreptime = getPreptime();
		resultFields = document.getElementById("recipes");
		while (resultFields.firstChild) {
			resultFields.removeChild(resultFields.firstChild);
		}
		searchDetails = {
			ingredients: searchIngredients,
			categories: searchCategories,
			maxPreptime: searchMaxPreptime,
		}
		searchPOST(searchDetails);
	}
}

function getSearchIngredients() {
	inputFields = document.querySelectorAll(".ingredientsearchfield");
	parentField = document.getElementById("searchfields");
	ingredients = [];
	for (field of inputFields) {
		if (field.value) {
			ingredient = field.value;
			ingredients.push(ingredient);
		} else {
			if (document.querySelectorAll(".ingredientsearchfield").length > 10) {
				parentField.removeChild(field.parentNode);
			}
		}
	}
	return ingredients;
}

async function addSearchFields() {
	template = document.getElementById("searchfield");
	fieldDiv = document.getElementById("searchfields");
	for (i = 0; i < 10; i++) {
		newNode = template.content.cloneNode(true);
		fieldDiv.appendChild(newNode);
	}
	ingredientsearchfields = document.getElementsByClassName("ingredientsearchfield");
	suggestedIngredients = await loadIngredients();
	for (field of ingredientsearchfields) {
		autocomplete(field, suggestedIngredients);
	}
}

async function searchPOST(searchDetails) {
	try {
		response = await fetch("api/searchrecipes",  {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
			body: JSON.stringify(searchDetails)
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
	recipesDiv = document.getElementById("recipes");

	if (recipeList.length == 0) {
		newDiv = document.createElement("div");
		newDiv.className = "noRecipes";
		recipesDiv.appendChild(newDiv);
		description = document.createTextNode("No recipes were found  :-(");
		newDiv.appendChild(description);
	} else {

	template = document.getElementById("recipe");
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
		
		ingredientNodeUS = newNode.querySelector(".ingredientus");
		ingredientsUS = recipe["ingredientsUS"];
		createIngredientNodes(ingredientsUS, ingredientsNode, ingredientNodeUS);
		
		ingredientNodeMetric = newNode.querySelector(".ingredientmetric");
		ingredientsMetric = recipe["ingredientsMetric"];
		createIngredientNodes(ingredientsMetric, ingredientsNode, ingredientNodeMetric);

		newNode.querySelector(".instructions").innerHTML += recipe["instructions"];
		
		if (recipe["source"]) {
			sourcelink = newNode.querySelector(".sourcelink");
			source = recipe["source"];
			sourcelink.href = source;
			sourcelink.innerHTML += source;
			sourcelink.style.display = "inline";
		}
		
		editlink = newNode.querySelector(".editlink");
		stringID = recipe["stringID"];
		editlink.href = "edit.html?" + stringID;

		recipesDiv.appendChild(newNode);
	}
	addEventListernersRecipe();
	checkToggle();
	}
}

function createIngredientNodes(ingredients, ingredientsNode, currentIngredientNode) {
	i = ingredients.length;
	for (ingredient of ingredients) {
		ingredientText = "";
		if (ingredient["amount"]) {
			ingredientText += ingredient["amount"] + " ";
		}
		if (ingredient["unit"]) {
			ingredientText += ingredient["unit"] + " ";
		}
		ingredientText += ingredient["name"];
		if (ingredient["notes"]) {
			ingredientText += ingredient["notes"];
		}
		currentIngredientNode.textContent = ingredientText;
		i--;
		if (i > 0) {
			nextNode = currentIngredientNode.cloneNode(true);
			ingredientsNode.appendChild(nextNode);
			currentIngredientNode = nextNode;
		}
	}
}

async function addIngredientInputFields() {
	template = document.getElementById("ingredientinputfields");
	fieldDiv = document.getElementById("ingredientsinput");
	for (i = 0; i < 10; i++) {
		newNode = template.content.cloneNode(true);
		fieldDiv.appendChild(newNode);
	}
	ingredientInputFields = document.getElementsByClassName("ingredientnameinput");
	suggestedIngredients = await loadIngredients();
	for (field of ingredientInputFields) {
		autocomplete(field, suggestedIngredients);
	}
	unitInputFields = document.getElementsByClassName("ingredientunitinput");
	for (field of unitInputFields) {
		autocomplete(field, unitOptions);
	}
}

async function loadIngredients() {
	try {
		response = await fetch("api/loadingredients",  {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                }
            });
		console.log("fetching ingredients...");
		ingredientList = await response.json();
		return ingredientList;
	} catch (error) {
		console.error(error);
	}
}

async function loadCategories() {
	try {
		response = await fetch("api/loadcategories",  {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                }
            });
		console.log("fetching categories...");
		categoryList = await response.json();
		return categoryList;
	} catch (error) {
		console.error(error);
	}
}

async function addCategoryCheckboxes() {
	fieldDiv = document.getElementById("categoriesinput");
	categories = await loadCategories();
	template = document.getElementById("categorycheckboxes");
	for (category of categories) {
		newNode = template.content.cloneNode(true);
		checkbox = newNode.querySelector(".category");
		checkbox.name = category;
		fieldDiv.appendChild(newNode);
		description = document.createTextNode(" " + category.toLowerCase());
		checkbox.parentElement.appendChild(description);
	}
}

function uncheckAllCheckbox() {
    allcheckbox = document.getElementById("allcheckbox");
    allcheckbox.checked = false;
}

var unitOptions = ['cm','cup','cups','g','inch','mL','oz','pinch','tbsp','tsp', 'cloves']

function validateInput() {
	rcpname = document.getElementById("nameinput").value;
	if (!rcpname) {
		alert("Please fill in a name for the recipe.");
	}
}

function getCategories() {
	rcpcategories = new Array();
	categoryButtons = document.getElementsByClassName("category")
	for (i = 0; i < categoryButtons.length; i++) {
		if (categoryButtons[i].checked) {
			rcpcategories.push(categoryButtons[i].name);
		}
	}
	if (rcpcategories.length == 0) rcpcategories.push("other");
	return rcpcategories;
}

function getPreptime() {
	rcppreptime = document.getElementById("preptimeinput").value;
	if (rcppreptime < 0 || !rcppreptime) rcppreptime = "0";
	return rcppreptime;
}

function createRecipe() {
	rcpname = document.getElementById("nameinput").value;
	
	rcpcategories = getCategories();
	
	rcprating = "0";
	ratingButtons = document.getElementsByName("rating");
	for (i = 0; i < ratingButtons.length; i++) {
		if (ratingButtons[i].checked) {
			rcprating  = ratingButtons[i].value;
			break;
		}
	}

	rcppreptime = getPreptime();
	
	rcpingredients = new Array();
	ingredientNameFields = document.getElementsByClassName("ingredientnameinput");
	ingredientAmountFields = document.getElementsByClassName("ingredientamountinput");
	ingredientUnitFields = document.getElementsByClassName("ingredientunitinput");
	ingredientNotesFields = document.getElementsByClassName("ingredientnotesinput");
	for (i = 0; i < ingredientNameFields.length; i++) {
		if (ingredientNameFields[i].value.length > 0) {
			ingrName = ingredientNameFields[i].value;
			ingrAmount = ingredientAmountFields[i].value;
			ingrUnit = ingredientUnitFields[i].value;
			ingrNotes = ingredientNotesFields[i].value;
			currIngredient = {
				name: ingrName,
				amount: ingrAmount,
				unit: ingrUnit,
				notes: ingrNotes,
			}
			rcpingredients.push(currIngredient);
		}
	}
	if (rcpingredients.length == 0) {
		currIngredient = {
				name: "No ingredients",
		}
		rcpingredients.push(currIngredient);
	}
	
	rcpinstructions = document.getElementById("instructionsinput").value;
	rcpinstructions = rcpinstructions.replace(/(?:\r\n|\r|\n)/g, '<br>');
	if (rcpinstructions.length < 1 || !rcpinstructions) {
		rcpinstructions = "No instructions";
	}
	
	rcpsource = document.getElementById("sourceinput").value;
	
	recipe = { 
        name: rcpname,
        categories: rcpcategories,
        rating: rcprating,
        preptime: rcppreptime,
        ingredients: rcpingredients,
        instructions: rcpinstructions,
		source: rcpsource,
    }
	return recipe;
}

async function addRecipeToDatabase() {
	validateInput();
	recipe = createRecipe();
	try {
		response = await fetch("api/addrecipe",  {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
			body: JSON.stringify(recipe)
            });
		console.log("sending new recipe to database...");
		check = await response.json();
		console.log(await check);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "Recipe added succesfully";
	} catch (error) {
		console.error(error);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "An error occurred";
	}
}

async function loadSingleRecipe() {
	stringID = window.location.search.substring(1);
	console.log(stringID);
	query = "api/loadsinglerecipe/" + stringID;
	try {
		response = await fetch(query,  {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                }
            });
		console.log("fetching recipe...");
		recipe = await response.json();
		fillEditFields(await recipe);
	} catch (error) {
		console.error(error);
	}
}

async function fillEditFields(recipe) {
	nameinput = document.getElementById("nameinput");
	nameinput.value = recipe["name"];
	
	categoryinput = document.getElementsByClassName("category");
	for (field of categoryinput) {
		if (recipe["categories"].includes(field.name)) {
			field.checked = true;
		}
	}
	
	ratinginput = document.getElementsByClassName("radiobutton");
	for (field of ratinginput) {
		if (recipe["rating"] == field.value) {
			field.checked = true;
		}
	}
	
	preptimeinput = document.getElementById("preptimeinput");
	preptimeinput.value = recipe["preptime"];
	
	ingredients = recipe["ingredients"];
	template = document.getElementById("ingredientinputfields");
	fieldDiv = document.getElementById("ingredientsinput");
	for (i = 0; i < ingredients.length; i++) {
		newNode = template.content.cloneNode(true);
		newNode.querySelector(".ingredientamountinput").value = ingredients[i]["amount"] || "";
		newNode.querySelector(".ingredientunitinput").value = ingredients[i]["unit"] || "";
		newNode.querySelector(".ingredientnameinput").value = ingredients[i]["name"];
		newNode.querySelector(".ingredientnotesinput").value = ingredients[i]["notes"] || "";
		fieldDiv.appendChild(newNode);
	}
	ingredientInputFields = document.getElementsByClassName("ingredientnameinput");
	suggestedIngredients = await loadIngredients();
	for (field of ingredientInputFields) {
		autocomplete(field, suggestedIngredients);
	}
	unitInputFields = document.getElementsByClassName("ingredientunitinput");
	for (field of unitInputFields) {
		autocomplete(field, unitOptions);
	}
	
	instructionsinput = document.getElementById("instructionsinput");
	instructions = recipe["instructions"];
	instructionsinput.innerHTML = instructions.replace(/<br\s*[\/]?>/gi, "\n");
	
	if (recipe["source"]) {
		sourceinput = document.getElementById("sourceinput");
		sourceinput.value = recipe["source"];
	}
	
	updateButton = document.getElementById("updaterecipe");
	updateButton.value = recipe["stringID"];
	
	updateButton = document.getElementById("deleterecipe");
	updateButton.value = recipe["stringID"];
}

async function editRecipe() {
	validateInput();
	recipe = createRecipe();
	stringID = document.getElementById("updaterecipe").value;
	query = "api/updaterecipe/" + stringID;
	try {
		response = await fetch(query,  {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
			body: JSON.stringify(recipe)
            });
		console.log("sending update recipe to database...");
		check = await response.json();
		console.log(await check);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "Recipe updated succesfully";
	} catch (error) {
		console.error(error);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "An error occurred";
	}
}

function confirmDeleteRecipe() {
	if (confirm("Are you sure you want to delete this recipe from the database? This can't be undone.")) {
		deleteRecipe();
	}
}

async function deleteRecipe() {
	stringID = document.getElementById("updaterecipe").value;
	query = "api/deleterecipe/" + stringID;
	try {
		response = await fetch(query,  {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
            });
		console.log("deleting recipe from database...");
		check = await response.json();
		console.log(await check);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "Recipe was deleted from database";
	} catch (error) {
		console.error(error);
		bottomtext = document.getElementById("bottomtext");
		bottomtext.textContent = "An error occurred";
	}
}

function toggleIngredients() {
	ingredientsUS = document.getElementsByClassName("ingredientus");
	ingredientsMetric = document.getElementsByClassName("ingredientmetric");
	if (ingredientsUS[0].style.display === "none") {
		localStorage.setItem("toggled", "false");
		for (ingrUS of ingredientsUS) {
			ingrUS.style.display = "list-item";
		}
		for (ingrM of ingredientsMetric) {
			ingrM.style.display = "none";
		}
	} else {
		localStorage.setItem("toggled", "true");
		for (ingrUS of ingredientsUS) {
			ingrUS.style.display = "none";
		}
		for (ingrM of ingredientsMetric) {
			ingrM.style.display = "list-item";
		}
	}
}

function checkToggle() {
	if (localStorage.getItem("toggled") && localStorage.getItem("toggled") == "true") {
		toggleIngredients();
	}
}


/* copied code */
function autocomplete(inp, arr) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      /*create a DIV element that will contain the items (values):*/
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      /*append the DIV element as a child of the autocomplete container:*/
      this.parentNode.appendChild(a);
      /*for each item in the array...*/
      for (i = 0; i < arr.length; i++) {
        /*check if the item starts with the same letters as the text field value:*/
        if (arr[i].toLowerCase().includes(val.toLowerCase())) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          b.innerHTML = arr[i];
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          /*execute a function when someone clicks on the item value (DIV element):*/
          b.addEventListener("click", function(e) {
              /*insert the value for the autocomplete text field:*/
              inp.value = this.getElementsByTagName("input")[0].value;
              /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
              closeAllLists();
          });
          a.appendChild(b);
        }
      }
  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {
        /*If the arrow DOWN key is pressed,
        increase the currentFocus variable:*/
        currentFocus++;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 38) { //up
        /*If the arrow UP key is pressed,
        decrease the currentFocus variable:*/
        currentFocus--;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 13) {
        /*If the ENTER key is pressed, prevent the form from being submitted,*/
        e.preventDefault();
        if (currentFocus > -1) {
          /*and simulate a click on the "active" item:*/
          if (x) x[currentFocus].click();
        }
      }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
}




	