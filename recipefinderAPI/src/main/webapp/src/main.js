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

function addEventListernersSearch() {
	searchButton = document.getElementById("searchbutton");
	searchButton.addEventListener("click", search);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addSearchFields);
}

function addEventListernersAdd() {
	addButton = document.getElementById("addnewrecipe");
	addButton.addEventListener("click", validateInput);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addIngredientInputFields);
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
	ingredients = getSearchIngredients();
	categories = getCategories();
	maxPreptime = getPreptime();
	resultFields = document.getElementById("recipes");
	while (resultFields.firstChild) {
		resultFields.removeChild(resultFields.firstChild);
	}
	searchPOST(ingredients);
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
			parentField.removeChild(field.parentNode);
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

		newNode.querySelector(".instructions").innerHTML += recipe["instructions"];

		recipesDiv.appendChild(newNode);
	}

	addEventListernersRecipe();
}

async function addIngredientInputFields() {
	template = document.getElementById("ingredientinputfields");
	fieldDiv = document.getElementById("ingredientsinput");
	addButton = document.getElementById("addfields");
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
	for (category of categories) {
		newDiv = document.createElement("div");
		fieldDiv.appendChild(newDiv);
		checkbox = document.createElement("input");
		checkbox.type = "checkbox";
		checkbox.className = "category";
		checkbox.name = category;
		newDiv.appendChild(checkbox);
		description = document.createTextNode(" " + category)
		newDiv.appendChild(description);
	}
}

var unitOptions = ['cm','cup','cups','g','inch','ml','oz','pinch','tbsp','tsp']

function validateInput() {
	rcpname = document.getElementById("nameinput").value;
	if (!rcpname) {
		alert("Please fill in a name for the recipe.");
	} else {
		createRecipe();
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
	if (rcpinstructions.length < 1 || !rcpinstructions) rcpinstructions = "No instructions";
	
	recipe = { 
        name: rcpname,
        categories: rcpcategories,
        rating: rcprating,
        preptime: rcppreptime,
        ingredients: rcpingredients,
        instructions: rcpinstructions,
    }
	sendRecipeToDatabase(recipe);
}

async function sendRecipeToDatabase(recipe) {
	try {
		response = await fetch("api/addrecipe",  {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
                },
			body: JSON.stringify(recipe)
            });
		console.log("sending recipe to database...");
		check = await response.json();
		console.log(await check);
	} catch (error) {
		console.error(error);
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




	