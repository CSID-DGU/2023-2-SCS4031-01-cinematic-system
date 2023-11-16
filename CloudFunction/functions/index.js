const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);

exports.helloWorld = functions.database.ref(
    "CareReceiver_list/abcd/ActivityData/응급").onUpdate((evt) => {
  const payload = {
    notification: {
      title: "VISITOR ALERT",
      body: "Someone is standing infront of your door",
      badge: "1",
      sound: "default",
    },
  };

  return admin.database().ref("fcm-token").once("value").then((allToken) => {
    if (allToken.val() && evt.after.val() == "1") {
      console.log("token available");
      const token = Object.keys(allToken.val());
      return admin.messaging().sendToDevice(token, payload);
    } else {
      console.log("No token available");
    }
  });
});
