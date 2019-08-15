function loadAddPage() {
	addIngredientInputFields();
	addCategoryCheckboxes();
	addEventListerners();
}

function addEventListerners() {
	addButton = document.getElementById("addnewrecipe");
	addButton.addEventListener("click", addRecipe);
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
		checkbox.name = category;
		newDiv.appendChild(checkbox);
		description = document.createTextNode(category)
		newDiv.appendChild(description);
	}
}

async function addRecipe() {
	console.log("adding recipe to database...");
}

var unitOptions = ['cm','cup','cups','g','inch','ml','oz','pinch','tbsp','tsp']

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




	