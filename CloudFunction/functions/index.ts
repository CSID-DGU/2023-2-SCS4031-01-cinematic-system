import {database} from "firebase-admin";
import DataSnapshot = database.DataSnapshot;
import {Change, EventContext, } from "firebase-functions";
import { ParamsOf } from "firebase-functions/lib/common/params";

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

// 응급상황 발생 트리거
exports.checkEmergency = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/emergency")
    .onUpdate((snapshot : Change<DataSnapshot>, context : EventContext<ParamsOf<string>>) => {
        const emergencyValue = snapshot.after.val();
        const userId = context.params.userId;
        console.log("userId: ", userId);

        // emergencyValue가 1이면 응급상황 발생
        if (emergencyValue === "1") {
            const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
            const guardianDataRef = admin.database().ref("/Guardian_list/");

            // 피보호자에게 푸시 알림을 보냄
            careReceiverDataRef.once("value").then((careReceiverSnapshot : DataSnapshot) => {
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
                        admin.messaging().send(message).then((response : string) => {
                            console.log("Message sent successfully:", response, "token: ", token);
                        })
                            .catch((error : string) => {
                                console.log("Error sending message: ", error);
                            });
                    }
                }
            });

            setTimeout(() => {
                // 15초 이내에 응답이 없으면 응급상황 발생으로 판단하고 보호자에게 푸시 알림을 보냄
                careReceiverDataRef.child("ActivityData").child("emergency").once("value").then((emergencySnapshot : DataSnapshot) => {
                    const emergencyValue = emergencySnapshot.val();
                    console.log("emergencyValue: ", emergencyValue);

                    if (emergencyValue === "1") {

                        // 피보호자의 보호자를 찾아서 푸시 알림을 보냄
                        guardianDataRef.once("value").then((guardianListSnapshot : DataSnapshot) => {
                            guardianListSnapshot.forEach((guardianSnapshot : DataSnapshot) => {
                                const guardianData = guardianSnapshot.val();
                                const carereceiverId = guardianData.CareReceiverID;

                                if (carereceiverId === userId && guardianData.deviceToken) {
                                    const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

                                    // Read the data
                                    userNameRef.once("value").then((userNameSnapshot : DataSnapshot) => {
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
                                                admin.messaging().send(message).then((response : string) => {
                                                    console.log("Message sent successfully:", response, "token: ", token);
                                                })
                                                    .catch((error : string) => {
                                                        console.log("Error sending message: ", error);
                                                    });
                                            }
                                        }
                                    });
                                }
                            });
                        });

                        // 응급상황 기록
                        careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot : DataSnapshot) => {
                            const latestEvent = latestEventSnapshot.val();
                            const keys = Object.keys(latestEvent);
                            const numChildren = Object.keys(latestEvent).length;
                            console.log("numChildren: ", numChildren);
                            // // 먼저 생성된 기록을 삭제하고 새로운 기록을 추가
                            if (numChildren > 4) {
                                keys.sort();
                                const oldestKey = keys[0];
                                careReceiverDataRef.child("ActivityData").child("latestEvent").child(oldestKey).remove();
                            }
                        });
                        const emergencyTimeStamp = Date.now();
                        const latestEvent = admin.database().ref(`/CareReceiver_list/${userId}/ActivityData/latestEvent`);
                        latestEvent.push(
                            {
                                time: emergencyTimeStamp,
                                type: "emergency"
                            }
                        );
                    }
                });
            }, 15000);
        }
    });

// 화재상황 발생 시 트리거
exports.checkFire = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/fire")
    .onUpdate((snapshot : Change<DataSnapshot>, context : EventContext<ParamsOf<string>>) => {
        const fireValue = snapshot.after.val();
        const userId = context.params.userId;
        console.log("userId: ", userId);

        if (fireValue === "1") {
            const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
            const guardianDataRef = admin.database().ref("/Guardian_list/");

            // 피보호자에게 푸시 알림을 보냄
            careReceiverDataRef.once("value").then((careReceiverSnapshot : DataSnapshot) => {
                const careReceiverData = careReceiverSnapshot.val();
                const tokenObject = careReceiverData.deviceToken;
                console.log("tokenObject: ", tokenObject);

                for (const key in tokenObject) {
                    if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                        const token = tokenObject[key];
                        const message = {
                            notification: {
                                title: "화재 상황 발생",
                                body: "화재 감자기에서 화재를 감지했습니다",
                            },
                            token: token,
                        };
                        admin.messaging().send(message).then((response : string) => {
                            console.log("Message sent successfully:", response, "token: ", token);
                        })
                            .catch((error : string) => {
                                console.log("Error sending message: ", error);
                            });
                    }
                }
            });

            // 피보호자의 보호자를 찾아서 푸시 알림을 보냄
            guardianDataRef.once("value").then((guardianListSnapshot : DataSnapshot) => {

                guardianListSnapshot.forEach((guardianSnapshot : DataSnapshot) => {
                    const guardianData = guardianSnapshot.val();
                    const carereceiverId = guardianData.CareReceiverID;

                    if (carereceiverId === userId && guardianData.deviceToken) {
                        // eslint-disable-next-line max-len
                        const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

                        userNameRef.once("value").then((userNameSnapshot : DataSnapshot) => {
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
                                    admin.messaging().send(message).then((response : string) => {
                                        // eslint-disable-next-line max-len
                                        console.log("Message sent successfully:", response, "token: ", token);
                                    })
                                        .catch((error : string) => {
                                                console.log("Error sending message: ", error);
                                            }
                                        );
                                }
                            }
                        });
                    }
                });
            });
            // 화재상황 기록
            careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot : DataSnapshot) => {
                const latestEvent = latestEventSnapshot.val();
                const keys = Object.keys(latestEvent);
                const numChildren = Object.keys(latestEvent).length;
                console.log("numChildren: ", numChildren);
                // 먼저 생성된 기록을 삭제하고 새로운 기록을 추가
                if (numChildren > 4) {
                    keys.sort();
                    const oldestKey = keys[0];
                    careReceiverDataRef.child("ActivityData").child("latestEvent").child(oldestKey).remove();
                }
            });
            const fireTimeStamp = Date.now();
            const latestEvent = admin.database().ref(`/CareReceiver_list/${userId}/ActivityData/latestEvent`);
            latestEvent.push(
                {
                    time: fireTimeStamp,
                    type: "fire"
                }
            );

            // 화재상황 발생 시 피보호자의 화재상황 발생 여부를 0으로 초기화
            careReceiverDataRef.child("ActivityData").child("fire").set("0");
        }
    });

// 도어 센서 문 열림 감지 시 트리거
exports.checkOuting = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/door/checkouting")
    .onUpdate((snapshot : Change<DataSnapshot>, context : EventContext<ParamsOf<string>>) => {
        const doorValue = snapshot.after.val();
        const userId = context.params.userId;
        console.log("userId: ", userId);

        if (doorValue === "1") {
            const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
            const guardianDataRef = admin.database().ref("/Guardian_list/");

            // 피보호자에게 푸시 알림을 보냄
            careReceiverDataRef.once("value").then((careReceiverSnapshot : DataSnapshot) => {
                const careReceiverData = careReceiverSnapshot.val();
                const tokenObject = careReceiverData.deviceToken;
                console.log("tokenObject: ", tokenObject);

                for (const key in tokenObject) {
                    if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                        const token = tokenObject[key];
                        const message = {
                            notification: {
                                title: "도어 센서가 문 열림을 감지했습니다",
                                body: "알림을 클릭하여 외출 상태를 변경해주세요",
                            },
                            token: token,
                        };
                        admin.messaging().send(message).then((response : string) => {
                            console.log("Message sent successfully:", response, "token: ", token);
                        })
                            .catch((error : string) => {
                                console.log("Error sending message: ", error);
                            });
                    }
                }
            });

            // 30분마다 외출 상태를 확인하고 외출 상태를 변경하지 않으면 푸시 알림을 보냄
            function checkOutingStatusEvery30min() {
                setTimeout(() => {
                    careReceiverDataRef.child("ActivityData").child("door").child("checkouting").once("value").then((outingSnapshot : DataSnapshot) => {
                        const outingValue = outingSnapshot.val();
                        console.log("outingValue: ", outingValue);

                        if (outingValue === "1") {
                            // 피보호자에게 푸시 알림을 보냄
                            careReceiverDataRef.once("value").then((careReceiverSnapshot : DataSnapshot) => {
                                const careReceiverData = careReceiverSnapshot.val();
                                const tokenObject = careReceiverData.deviceToken;
                                console.log("tokenObject: ", tokenObject);

                                for (const key in tokenObject) {
                                    if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                                        const token = tokenObject[key];
                                        const message = {
                                            notification: {
                                                title: "도어 센서가 문 열림을 감지했습니다",
                                                body: "알림을 클릭하여 외출 상태를 변경해주세요",
                                            },
                                            token: token,
                                        };
                                        admin.messaging().send(message).then((response : string) => {
                                            console.log("Message sent successfully:", response, "token: ", token);
                                        })
                                            .catch((error : string) => {
                                                console.log("Error sending message: ", error);
                                            });
                                    }
                                }
                            });
                            checkOutingStatusEvery30min();
                        }
                    });
                }, 1800000);
            }

            checkOutingStatusEvery30min();
        }
    });

// 도어 센서 문 열림 확인 시 트리거
exports.confirmOuting = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/door/outing")
    .onUpdate((snapshot : Change<DataSnapshot> , context : EventContext<ParamsOf<string>> ) => {
        const doorValue = snapshot.after.val();
        const userId = context.params.userId;
        console.log("userId: ", userId);

        if (doorValue === "1") {
            const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
            const guardianDataRef = admin.database().ref("/Guardian_list/");

            // 보호자에게 푸시 알림을 보냄
            guardianDataRef.once("value").then((guardianListSnapshot : DataSnapshot) => {
                guardianListSnapshot.forEach((guardianSnapshot : DataSnapshot) => {
                    const guardianData = guardianSnapshot.val();
                    const carereceiverId = guardianData.CareReceiverID;

                    if (carereceiverId === userId && guardianData.deviceToken) {
                        const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);

                        userNameRef.once("value").then((userNameSnapshot : DataSnapshot) => {
                            const name = userNameSnapshot.val();

                            const tokenObject = guardianData.deviceToken;
                            console.log("tokenObject: ", tokenObject);

                            for (const key in tokenObject) {
                                if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                                    const token = tokenObject[key];
                                    const message = {
                                        notification: {
                                            title: "외출 상황 감지",
                                            body: `${name} 님이 외출하셨습니다`,
                                        },
                                        token: token,
                                    };
                                    admin.messaging().send(message).then((response : string) => {
                                        console.log("Message sent successfully:", response, "token: ", token);
                                    })
                                        .catch((error : string) => {
                                                console.log("Error sending message: ", error);
                                            }
                                        );
                                }
                            }
                        });
                    }
                });
            });

            // 외출상황 기록
            careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot : DataSnapshot) => {
                const latestEvent = latestEventSnapshot.val();
                const keys = Object.keys(latestEvent);
                const numChildren = Object.keys(latestEvent).length;
                console.log("numChildren: ", numChildren);
                // 먼저 생성된 기록을 삭제하고 새로운 기록을 추가
                if (numChildren > 4) {
                    keys.sort();
                    const oldestKey = keys[0];
                    careReceiverDataRef.child("ActivityData").child("latestEvent").child(oldestKey).remove();
                }
            });

            const outingTimeStamp = Date.now();
            const latestEvent = admin.database().ref(`/CareReceiver_list/${userId}/ActivityData/latestEvent`);
            latestEvent.push(
                {
                    time: outingTimeStamp,
                    type: "outing"
                }
            );
        }
    });

exports.checkActivities = functions.database.ref("CareReceiver_list/{userId}/ActivityData/activity/cnt")
  .onUpdate((snapshot : Change<DataSnapshot>, context : EventContext<ParamsOf<string>>) => {
    const cnt = snapshot.after.val();
  });
