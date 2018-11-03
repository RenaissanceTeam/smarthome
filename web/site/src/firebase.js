import firebase from 'firebase'
var config = {
			apiKey: "AIzaSyAMPO-acgVmKxnB0sBfv7tzpxo5G-yuhfY",
			authDomain: "smarthome-47922.firebaseapp.com",
			databaseURL: "https://smarthome-47922.firebaseio.com",
			projectId: "smarthome-47922",
			storageBucket: "smarthome-47922.appspot.com",
			messagingSenderId: "700180913950"
			}

var myFirebase = firebase.initializeApp(config)
export default myFirebase
