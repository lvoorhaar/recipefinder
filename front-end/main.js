/**
 * Server side code using the express framework running on a Node.js server.
 * 
 * Load the express framework and create an app.
 */
//const express = require('express');
//const app = express();
/** 
 * Host all files in the client folder as static resources.
 * That means: localhost:8080/someFileName.js corresponds to client/someFileName.js.
 */
//app.use(express.static('client'));

/**
 * Allow express to understand json serialization.
 */
/*app.use(express.json());


const mysql = require('mysql2/promise');
const pool = mysql.createPool({
  host  : 'localhost',
  port: 3307,
  user  : 'application',
  password : 'password',
  database: 'sogyoadventures',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});*/



/**
 * Our code starts here.
 */
 
const recipes = [
    { 
        name: "Chocolate Chip Cookies",
        categories: {"COOKIES"},
        rating: 5,
        preptime: 28,
        ingredients: {["100","g","flour"], ["50","g","sugar"]},
        instructions: "Instructions go here",
    },

    { 
        name: "food",
        categories: {"BREAKFAST", "DINNER"},
        rating: 3,
        preptime: 50,
        ingredients: {["100","g","pasta"], ["5","number","tomatoes"]},
        instructions: "do something",
    },
]

/**
 * A route is like a method call. It has a name, some parameters and some return value.
 * 
 * Name: /api/attractions
 * Parameters: the request as made by the browser
 * Return value: the list of attractions defined above as JSON
 * 
 * In addition to this, it has a HTTP method: GET, POST, PUT, DELETE
 * 
 * Whenever we make a request to our server,
 * the Express framework will call one of the methods defined here.
 * These are just regular functions. You can edit, expand or rewrite the code here as needed.
 */
app.get("/api/browse", function (request, response) {
    console.log("Api call received for /browse");

	/*let sql = 'SELECT * FROM Attraction WHERE active = 1';
	pool.query(sql, function(err, results, fields) {
		if (err) {
			console.log("Error: ");
			console.log(err);
		}
		response.json(results);
	});*/


    response.json(recipes)
});

app.post("/api/placeorder", async function (request, response) {
    console.log("Api call received for /placeorder");
	let orders = request.body;
	//console.log(orders);
	
	await processOrders(orders);
	
	/**
     * Send the status code 200 back to the clients browser.
     * This means OK.
     */
    response.sendStatus(200);
});

app.get("/api/myorders", function (request, response) {
    console.log("Api call received for /myorders");

    response.sendStatus(200);
});

app.get("/api/admin/edit", function (request, response) {
    console.log("Api call received for /admin/edit");

    response.sendStatus(200);
});

async function processOrders(orders) {
	sqlCreateCustomer = "INSERT INTO Customer (name, phonenumber, email, street, housenumber, city, postcode) VALUES ('Jantje', 0123456789, 'jantje@hotmail.com', 'Dorpsstraat', 17, 'Het Gat', '5678 AB')";
	async function createCustomer() {
			response = await pool.query(sqlCreateCustomer);
			[{insertId}] = await response;
			return insertId;
	};
	
	//sqlGetCustomer = "SELECT customer_id  FROM Customer WHERE name='Jantje'";
	/*async function getCustomer() {
			await createCustomer();
			[[{customer}]] = await pool.query(sqlGetCustomer);
			console.log("customer: " + customer);
			return customer;
	};*/
	
	customer_id = await createCustomer();
	
	for (order of orders) {
		parkname = order["parkname"];
		ticketsOrdered = order["total"];
		totalPrice = order["totalPrice"];
		
		sqlGetEventID = "SELECT event_id FROM Attraction WHERE name='" + parkname + "'";
		async function getEventID() {
			[[{event_id}]] = await pool.query(sqlGetEventID);
			return event_id;
		};
		event_id = await getEventID();
		
		sqlGetAvailableTickets = "SELECT available FROM Attraction WHERE event_id=" + event_id;
		async function getAvailableTickets() {
			[[{available}]] = await pool.query(sqlGetAvailableTickets);
			return available;
		};
		availableTickets = await getAvailableTickets();
				
		if (availableTickets < ticketsOrdered) {
			console.log("Not enough tickets available. Order for " + parkname + " cancelled.");
			//window.alert("Not enough tickets available. Order for " + parkname + " cancelled."); werkt niet
			continue;
		}
		
		sqlCreateOrder = "INSERT INTO Orders (customer_id, totalprice) VALUES (" + customer_id + ", " + totalPrice + ")";
		async function createOrder() {
			response = await pool.query(sqlCreateOrder);
			[{insertId}] = await response;
			return insertId;
		};
		order_id = await createOrder();
		
		sqlCreateTicket = "INSERT INTO Ticket (customer_id, event_id, order_id) VALUES ("+ customer_id + ", " + event_id + ", " + order_id + ")";
		async function createTicket() {
			response = await pool.query(sqlCreateTicket);
			[{insertId}] = await response;
			return insertId;
		};
		for (i = 1; i <= ticketsOrdered; i++) {
			console.log("ticket ordered with ID: " + await createTicket())
		}
		
		sqlReduceAvailableTickets = "UPDATE Attraction SET numberticketssold=numberticketssold + " + ticketsOrdered + ", available=available - " + ticketsOrdered + " WHERE event_id=" + event_id;
		async function ReduceAvailableTickets() {
			response = await pool.query(sqlReduceAvailableTickets);
		};
		ReduceAvailableTickets()
		
		console.log(ticketsOrdered + " tickets ordered for " + parkname + " with a total price of " + totalPrice);
		
		//return message to orderplaced.html about cancelled orders
	}
}

/**
 * Make our webserver available on port 8000.
 * Visit localhost:8000 in any browser to see your site!
 */
app.listen(8000, () => console.log('Example app listening on port 8000!'));