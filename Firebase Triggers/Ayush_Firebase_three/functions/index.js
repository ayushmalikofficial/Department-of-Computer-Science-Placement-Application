const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.updateAttendance = functions.firestore
    .document('placement_events/Attendance')
    .onUpdate((change, context) => {
      // Get an object representing the document
      // e.g. {'name': 'Marie', 'age': 66}
      const newValue = change.after.data();

      // ...or the previous value before this update
      const previousValue = change.before.data();

      // access a particular field as you would any JS property
      var company ;
	
      // perform desired operations ...
		const stat= newValue.Enabled;
		var input_content;
		if(stat)
		{		
			company=newValue.Company;
			 input_content="Marking of attendance for "+company+" is now available.";
		}
		else
		{
				company=previousValue.Company;
				input_content="Attendance for "+company+" is completed.";
		}
	
	console.log("Function Called");
   // access a particular field as you would any JS property
   //   const name = newValue.name;
   const notificationContent = {
				notification: {
					title: "Attendance ("+company+")",
					body: input_content ,
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
	
	
	