const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

/** 응급상황 발생 Trigger
 *
 * 1. 피보호자 db를 찾아 응급상황 발생 여부를 확인
 * 2. 응급상황 발생 시 피보호자에게 푸시 알림을 보냄
 * 3. 15초 이내 응답이 없을 시 응급상황 발생으로 판단하고 보호자에게 푸시 알림을 보냄
 */
exports.checkEmergency = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/emergency")
    .onUpdate((snapshot, context) => {
      const emergencyNewValue = snapshot.after.val();
      const userId = context.params.userId;
      console.log("userId: ", userId);

      if (emergencyNewValue === "1") {
        const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
        const guardianDataRef = admin.database().ref("/Guardian_list/");

        // 피보호자에게 푸시 알림을 보냄
        careReceiverDataRef.once("value").then((careReceiverSnapshot) => {
          const careReceiverData = careReceiverSnapshot.val();
          const tokenObject = careReceiverData.deviceToken;
          console.log("tokenObject: ", tokenObject);

          for (const key in tokenObject) {
            if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
              const token = tokenObject[key];
              const message = {
                notification: {
                  title: "응급 상황 발생",
                  body: "응급 버튼을 누른 것이 아니라면 15초 내에 응답해주세요",
                },
                token: token,
              };
              admin.messaging().send(message).then((response) => {
                console.log("Message sent successfully:", response, "token: ", token);
              })
                  .catch((error) => {
                    console.log("Error sending message: ", error);
                  });
            }
          }
        });


        // 피보호자의 보호자를 찾아서 푸시 알림을 보냄
        return guardianDataRef.once("value").then((guardianListSnapshot) => {
          const promises = []; // To hold all promises

          guardianListSnapshot.forEach((guardianSnapshot) => {
            const guardianData = guardianSnapshot.val();
            const carereceiverId = guardianData.CareReceiverID;

            if (carereceiverId === userId && guardianData.deviceToken) {
              const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

              // Read the data
              userNameRef.once("value").then((userNameSnapshot) => {
                const name = userNameSnapshot.val();

                const tokenObject = guardianData.deviceToken;
                console.log("tokenObject: ", tokenObject);

                for (const key in tokenObject) {
                  if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                    const token = tokenObject[key];
                    const message = {
                      notification: {
                        title: "응급 상황 발생",
                        body: `${name} 님이 응급 버튼을 눌렀습니다`,
                      },
                      token: token,
                    };
                    promises.push(
                        admin.messaging().send(message).then((response) => {
                          console.log("Message sent successfully:", response, "token: ", token);
                        })
                            .catch((error) => {
                              console.log("Error sending message: ", error);
                            }),
                    );
                  }
                }
              });
            }
          });
          return Promise.all(promises);
        });
      } else {
        return Promise.resolve();
      }
    });

// 화재상황 발생 시 Trigger
// eslint-disable-next-line max-len
exports.checkFire = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/fire")
    .onUpdate((snapshot, context) => {
      const fireNewValue = snapshot.after.val();
      const userId = context.params.userId;
      console.log("userId: ", userId);

      if (fireNewValue === "1") {
        const guardianDataRef = admin.database().ref("/Guardian_list/");

        // 피보호자의 보호자를 찾아서 푸시 알림을 보냄
        return guardianDataRef.once("value").then((guardianListSnapshot) => {
          const promises = []; // To hold all promises

          guardianListSnapshot.forEach((guardianSnapshot) => {
            const guardianData = guardianSnapshot.val();
            const carereceiverId = guardianData.CareReceiverID;

            if (carereceiverId === userId && guardianData.deviceToken) {
              // eslint-disable-next-line max-len
              const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

              // Read the data
              userNameRef.once("value").then((userNameSnapshot) => {
                const name = userNameSnapshot.val();

                const tokenObject = guardianData.deviceToken;
                console.log("tokenObject: ", tokenObject);

                for (const key in tokenObject) {
                  if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                    const token = tokenObject[key];
                    const message = {
                      notification: {
                        title: "화재 상황 발생",
                        body: `${name} 님의 집에서 화재가 감지되었습니다`,
                      },
                      token: token,
                    };
                    promises.push(
                        admin.messaging().send(message).then((response) => {
                          // eslint-disable-next-line max-len
                          console.log("Message sent successfully:", response, "token: ", token);
                        })
                            .catch((error) => {
                              console.log("Error sending message: ", error);
                            }),
                    );
                  }
                }
              });
            }
          });
          return Promise.all(promises);
        });
      } else {
        return Promise.resolve();
      }
    });

// 문 열림 상황 발생 시 Trigger
// eslint-disable-next-line max-len
exports.checkOuting = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/door")
    .onUpdate((snapshot, context) => {

    });
