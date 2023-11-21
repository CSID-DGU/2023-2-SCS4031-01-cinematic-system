const functions = require("firebase-functions");

const admin = require("firebase-admin");
admin.initializeApp();

exports.checkEmergency =
functions.database.ref("/CareReceiver_list/abcd/ActivityData/emergency")
    .onUpdate((snapshot, context) => {
      const emergencyNewValue = snapshot.after.val();

      if (emergencyNewValue === "1") {
        // replace it with your app token
        const temptoken = "e6NlfINaQ9CLzkMgXpydlb:"+
          "APA91bH13xHzNWj-FkjnoHIk6zIoAdGvWwxbaJJrJ"+
          "_wKLd4UYIcZOow4MVA2jvN4DoBIvRGop5bJbciq73P9"+
          "j3l6iPMGIpskPvQkU5X2l8FUcwYfcujDkpWXo4hjy-WdEH-F1PMbTweb";

        const statusMessage = `응급 호출 버튼 이벤트 발생`;
        const message = {
          notification: {
            title: "!! 응급 상황 !!",
            body: statusMessage,
          },
          token: temptoken,
        };
        admin.messaging().send(message).then((response) => {
          console.log("Message sent successfully:", response);
          return snapshot.after.ref.set("0");
        })
            .catch((error) => {
              console.log("Error sending message: ", error);
            });
      }
    });

exports.checkFire =
functions.database.ref("/CareReceiver_list/abcd/ActivityData/fire")
    .onUpdate((snapshot, context) => {
      const fireNewValue = snapshot.after.val();

      if (fireNewValue === "1") {
        // replace it with your app token
        const temptoken = "e6NlfINaQ9CLzkMgXpydlb:"+
          "APA91bH13xHzNWj-FkjnoHIk6zIoAdGvWwxbaJJrJ"+
          "_wKLd4UYIcZOow4MVA2jvN4DoBIvRGop5bJbciq73P9"+
          "j3l6iPMGIpskPvQkU5X2l8FUcwYfcujDkpWXo4hjy-WdEH-F1PMbTweb";

        const statusMessage = `화재 감지 이벤트 발생`;
        const message = {
          notification: {
            title: "!! 화재 발생 !!",
            body: statusMessage,
          },
          token: temptoken,
        };
        admin.messaging().send(message).then((response) => {
          console.log("Message sent successfully:", response);
          return snapshot.after.ref.set("0");
        })
            .catch((error) => {
              console.log("Error sending message: ", error);
            });
      }
    });
