# Smart home project
## Web part
I want to create a platform to add building plans and modules, so that every part of smart house will be depicted and easy to access. This should be done as a web app using React and Firebase.

#### Contribute
* Download npm ( https://www.npmjs.com/get-npm )
* Install scripts ( npm i create-react-app )

## Hardware & Software part
### Home
There should be a Raspberry PI (probably with Android Things) that will act like a server. It will be able to host Arduinos which should be able to perform some basic operations: give information about their services (things they can control e.g. sensors), operate the services and transfer that data to server (Raspberry) via some protocol (idk which one, probably MQTT). 
### User
On the other side, user should be able to interact with a home server (Raspberry) via Android app.

