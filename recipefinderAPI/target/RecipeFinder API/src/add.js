function loadAddPage() {
	addIngredientInputFields();
	addCategoryCheckboxes();
	addEventListerners();
}

function addEventListerners() {
	addButton = document.getElementById("addnewrecipe");
	addButton.addEventListener("click", validateInput);
	addFieldsButton = document.getElementById("addfields");
	addFieldsButton.addEventListener("click", addIngredientInputFields);
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

function validateInput() {
	rcpname = document.getElementById("nameinput").value;
	if (!rcpname) {
		alert("Please fill in a name for the recipe.");
	} else {
		createRecipe();
	}
}

function createRecipe() {
	rcpname = document.getElementById("nameinput").value;
	
	rcpcategories = new Array();
	categoryButtons = document.getElementsByClassName("category")
	for (i = 0; i < categoryButtons.length; i++) {
		if (categoryButtons[i].checked) {
			rcpcategories.push(categoryButtons[i].name);
		}
	}
	if (rcpcategories.length == 0) rcpcategories.push("other");
	
	rcprating = "0";
	ratingButtons = document.getElementsByName("rating");
	for (i = 0; i < ratingButtons.length; i++) {
		if (ratingButtons[i].checked) {
			rcprating  = ratingButtons[i].value;
			break;
		}
	}

	rcppreptime = document.getElementById("preptimeinput").value;
	if (rcppreptime < 0 || !rcppreptime) rcppreptime = "0";
	
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

var unitOptions = ['cm','cup','cups','g','inch','ml','oz','pinch','tbsp','tsp']

/* copied code */
function autocomplete(inp, arr) {
  var currentFocus;
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      this.parentNode.appendChild(a);
      for (i = 0; i < arr.length; i++) {
        if (arr[i].toLowerCase().includes(val.toLowerCase())) {
          b = document.createElement("DIV");
          b.innerHTML = arr[i];
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          b.addEventListener("click", function(e) {
              inp.value = this.getElementsByTagName("input")[0].value;
              closeAllLists();
          });
          a.appendChild(b);
        }
      }
  });
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {
        currentFocus++;
        addActive(x);
      } else if (e.keyCode == 38) { //up
        currentFocus--;
        addActive(x);
      } else if (e.keyCode == 13) {
        e.preventDefault();
        if (currentFocus > -1) {
          if (x) x[currentFocus].click();
        }
      }
  });
  function addActive(x) {
    if (!x) return false;
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
}




	