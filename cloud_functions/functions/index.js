var app = require('express')();
const functions = require('firebase-functions');
const admin = require('firebase-admin');
var bodyParser = require('body-parser');
var serviceAccount = require('../serviceAccountKey.json');

admin.initializeApp({ credential: admin.credential.cert(serviceAccount) });
app.use(bodyParser.json());


exports.sendFcm = functions.https.onRequest((request, response) => {
 	let title = request.body['title'];
 	let body = request.body['body'];
 	let priority = request.body['priority'];
 	let type = request.body['type']
 	if (!priority) {
 		console.log('no priority, so use default="normal"');
 		priority = "normal";
 	}
 	let tokens = request.body['tokens'];
 	
 	if (isNull(title, 'title')) return getInvalidError(title, 'title', response);
 	if (isNull(body, 'body')) return getInvalidError(body, 'body', response);
 	if (isNull(tokens, 'tokens')) return getInvalidError(tokens, 'tokens', response);
 	if (isNull(type, 'type')) return getInvalidError(type, 'type', response);

 	let payload = createPayload(type, title, body);
 	let options = { priority: priority };

    admin.messaging().sendToDevice(tokens, payload, options)
	    .then((fcmResponse) => {
	    	console.log('Successfully sent message:', fcmResponse);
	    	response.send("Success")
	    })
	    .catch((error) => {
	    	console.log("error", error);
	    	response.status(404).send('Error sending message:', error);
	    });
});

function isNull(what, name) {
	if (what == null || what == undefined || what.length == 0) {
		console.log("no value provided for", name);
		return true;
	}
	console.log("value for", name, "=", what);
	return false;
}

function getInvalidError(what, name, response) {
	response.status(404).send("passed invalid value for " + name + "=" + what);
}

function createPayload(type, title, body) {
	if (type == "notification") {
	 	return {
	 		notification: {
	 			title: title,
	 			body: body
	 		}
	 	};
 	} 

 	return  {
 		data: {
 			title: title,
 			body: body
 		}
 	};
 	
}