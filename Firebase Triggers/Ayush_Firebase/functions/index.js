const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.createNotification = functions.firestore
    .document('announce/{userId}')
    .onCreate((snap, context) => {
   // Get an object representing the documen
   // e.g. {'name': 'Marie', 'age': 66}
   const newValue = snap.data();
   console.log("Function Called");
   // access a particular field as you would any JS property
   //   const name = newValue.name;
   const input_title=newValue.title;
   const input_content=newValue.content_input;
   const input_type_of_announcement=newValue.type_of_announcement;
   const notificationContent = {
				notification: {
					title: input_title+" ("+input_type_of_announcement+")",
					body: input_content,
					icon: "default"
				}
			};
   //const tokenId=newValue.tokenID;
   const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };
	
   console.log("Notification Created but Not Sent");
   
   return admin.messaging().sendToTopic("/topics/all",notificationContent, options).then(result => {
				console.log("Notification sent!");
				//admin.firestore().collection("notifications").doc(userEmail).collection("userNotifications").doc(notificationId).delete();
			});
      
    });

	

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
