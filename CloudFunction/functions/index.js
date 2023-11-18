const functions = require("firebase-functions");

const admin = require("firebase-admin");
admin.initializeApp();


exports.checkflag =
functions.database.ref("/CareReceiver_list/abcd/ActivityData/응급")
    .onUpdate((snapshot, context) => {
      // replace it with your app token
      const temptoken = "e6NlfINaQ9CLzkMgXpydlb:"+
      "APA91bH13xHzNWj-FkjnoHIk6zIoAdGvWwxbaJJrJ"+
      "_wKLd4UYIcZOow4MVA2jvN4DoBIvRGop5bJbciq73P9"+
      "j3l6iPMGIpskPvQkU5X2l8FUcwYfcujDkpWXo4hjy-WdEH-F1PMbTweb";

      const flag = snapshot.after.val();
      const statusMessage = `Message from the clouds as ${flag}`;
      const message = {
        notification: {
          title: "cfunction",
          body: statusMessage,
        },
        token: temptoken,
      };
      admin.messaging().send(message).then((response) => {
        console.log("Message sent successfully:", response);
        return response;
      })
          .catch((error) => {
            console.log("Error sending message: ", error);
          });
    });
