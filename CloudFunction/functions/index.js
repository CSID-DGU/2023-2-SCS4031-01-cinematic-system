const functions = require("firebase-functions");

const admin = require("firebase-admin");
admin.initializeApp();

// eslint-disable-next-line max-len
exports.checkEmergency = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/emergency")
    .onUpdate((snapshot, context) => {
      const emergencyNewValue = snapshot.after.val();
      const userId = context.params.userId;
      console.log("userId: ", userId);

      if (emergencyNewValue === "1") {
        const guardianDataRef = admin.database().ref("/Guardian_list/");

        return guardianDataRef.once("value").then((guardianListSnapshot) => {
          const promises = []; // To hold all promises

          guardianListSnapshot.forEach((guardianSnapshot) => {
            const guardianData = guardianSnapshot.val();
            const carereceiverId = guardianData.CareReceiverID;

            if (carereceiverId === userId) {
              if (guardianData.deviceToken === undefined) {
                promises.push(snapshot.after.ref.set("0"));
                console.log("guardianData.deviceToken is undefined");
              } else {
                // eslint-disable-next-line max-len
                const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

                // Read the data
                userNameRef.once("value").then((userNameSnapshot) => {
                  const name = userNameSnapshot.val();

                  const token = guardianData.deviceToken;
                  console.log("token: ", token);

                  const statusMessage = "응급 상황 발생";
                  const message = {
                    notification: {
                      title: `${name} 님이 응급 버튼을 눌렀습니다.`,
                      body: statusMessage,
                    },
                    token: token,
                  };
                  promises.push(
                      admin.messaging().send(message).then((response) => {
                        console.log("Message sent successfully:", response);
                        return snapshot.after.ref.set("0");
                      })
                          .catch((error) => {
                            console.log("Error sending message: ", error);
                          }),
                  );
                });
              }
            }
          });
          return Promise.all(promises);
        });
      } else {
        return Promise.resolve();
      }
    });

// eslint-disable-next-line max-len
exports.checkFire = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/fire")
    .onUpdate((snapshot, context) => {
      const fireNewValue = snapshot.after.val();
      const userId = context.params.userId;
      console.log("userId: ", userId);

      if (fireNewValue === "1") {
        const guardianDataRef = admin.database().ref("/Guardian_list/");

        return guardianDataRef.once("value").then((guardianListSnapshot) => {
          const promises = []; // To hold all promises

          guardianListSnapshot.forEach((guardianSnapshot) => {
            const guardianData = guardianSnapshot.val();
            const carereceiverId = guardianData.CareReceiverID;

            if (carereceiverId === userId) {
              if (guardianData.deviceToken === undefined) {
                promises.push(snapshot.after.ref.set("0"));
                console.log("guardianData.deviceToken is undefined");
              } else {
                // eslint-disable-next-line max-len
                const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

                // Read the data
                userNameRef.once("value").then((userNameSnapshot) => {
                  const name = userNameSnapshot.val();

                  const token = guardianData.deviceToken;
                  console.log("token: ", token);

                  const statusMessage = "화재 상황 발생";
                  const message = {
                    notification: {
                      title: `${name} 님의 집에서 화재 발생을 감지했습니다.`,
                      body: statusMessage,
                    },
                    token: token,
                  };
                  promises.push(
                      admin.messaging().send(message).then((response) => {
                        console.log("Message sent successfully:", response);
                        return snapshot.after.ref.set("0");
                      })
                          .catch((error) => {
                            console.log("Error sending message: ", error);
                          }),
                  );
                });
              }
            }
          });
          return Promise.all(promises);
        });
      } else {
        return Promise.resolve();
      }
    });
