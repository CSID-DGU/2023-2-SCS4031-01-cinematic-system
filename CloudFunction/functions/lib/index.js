"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
// 응급상황 발생 트리거
exports.checkEmergency = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/emergency")
    .onUpdate((snapshot, context) => {
    const emergencyValue = snapshot.after.val();
    const userId = context.params.userId;
    console.log("userId: ", userId);
    // emergencyValue가 1이면 응급상황 발생
    if (emergencyValue === "1") {
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
        // DB의 ActivityData에서 1초에 한번씩 타이머 업데이트
        let emergencyTimer = 15;
        const interval = setInterval(() => {
            emergencyTimer--;
            careReceiverDataRef.child("ActivityData").child("emergencyTimer").set(emergencyTimer.toString());
            // emergencyValue가 0이면 타이머를 멈춤
            careReceiverDataRef.child("ActivityData").child("emergency").once("value").then((emergencySnapshot) => {
                const emergencyValue = emergencySnapshot.val();
                if (emergencyValue === "0") {
                    clearInterval(interval);
                    careReceiverDataRef.child("ActivityData").child("emergencyTimer").remove();
                }
            });
            // timer가 0이면 타이머를 멈춤
            if (emergencyTimer === 0) {
                clearInterval(interval);
                executeAfter15sec();
                careReceiverDataRef.child("ActivityData").child("emergencyTimer").remove();
            }
        }, 1000);
        // 15초가 지나면 응급상황 발생으로 판단하고 보호자에게 푸시 알림을 보냄
        function executeAfter15sec() {
            careReceiverDataRef.child("ActivityData").child("emergency").once("value").then((emergencySnapshot) => {
                const emergencyValue = emergencySnapshot.val();
                console.log("emergencyValue: ", emergencyValue);
                if (emergencyValue === "1") {
                    // 피보호자의 보호자를 찾아서 푸시 알림을 보냄
                    guardianDataRef.once("value").then((guardianListSnapshot) => {
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
                                            admin.messaging().send(message).then((response) => {
                                                console.log("Message sent successfully:", response, "token: ", token);
                                            })
                                                .catch((error) => {
                                                console.log("Error sending message: ", error);
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    });
                    // 응급상황 기록
                    careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot) => {
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
                    latestEvent.push({
                        time: emergencyTimeStamp,
                        type: "emergency"
                    });
                }
            });
        }
    }
});
// 화재상황 발생 시 트리거
exports.checkFire = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/fire")
    .onUpdate((snapshot, context) => {
    const fireValue = snapshot.after.val();
    const userId = context.params.userId;
    console.log("userId: ", userId);
    if (fireValue === "1") {
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
                            title: "화재 상황 발생",
                            body: "화재 감자기에서 화재를 감지했습니다",
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
        guardianDataRef.once("value").then((guardianListSnapshot) => {
            guardianListSnapshot.forEach((guardianSnapshot) => {
                const guardianData = guardianSnapshot.val();
                const carereceiverId = guardianData.CareReceiverID;
                if (carereceiverId === userId && guardianData.deviceToken) {
                    // eslint-disable-next-line max-len
                    const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);
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
                                admin.messaging().send(message).then((response) => {
                                    // eslint-disable-next-line max-len
                                    console.log("Message sent successfully:", response, "token: ", token);
                                })
                                    .catch((error) => {
                                    console.log("Error sending message: ", error);
                                });
                            }
                        }
                    });
                }
            });
        });
        // 화재상황 기록
        careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot) => {
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
        latestEvent.push({
            time: fireTimeStamp,
            type: "fire"
        });
        // 화재상황 발생 시 피보호자의 화재상황 발생 여부를 0으로 초기화
        careReceiverDataRef.child("ActivityData").child("fire").set("0");
    }
});
// 도어 센서 문 열림 감지 시 트리거
exports.checkOuting = functions.database.ref("/CareReceiver_list/{userId}/ActivityData/door/checkouting")
    .onUpdate((snapshot, context) => {
    const doorValue = snapshot.after.val();
    const userId = context.params.userId;
    console.log("userId: ", userId);
    if (doorValue === "1") {
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
                            title: "도어 센서가 문 열림을 감지했습니다",
                            body: "알림을 클릭하여 외출 상태를 변경해주세요",
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
        // 30분마다 외출 상태를 확인하고 외출 상태를 변경하지 않으면 푸시 알림을 보냄
        function checkOutingStatusEvery30min() {
            setTimeout(() => {
                careReceiverDataRef.child("ActivityData").child("door").child("checkouting").once("value").then((outingSnapshot) => {
                    const outingValue = outingSnapshot.val();
                    console.log("outingValue: ", outingValue);
                    if (outingValue === "1") {
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
                                            title: "도어 센서가 문 열림을 감지했습니다",
                                            body: "알림을 클릭하여 외출 상태를 변경해주세요",
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
    .onUpdate((snapshot, context) => {
    const doorValue = snapshot.after.val();
    const userId = context.params.userId;
    console.log("userId: ", userId);
    if (doorValue === "1") {
        const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
        const guardianDataRef = admin.database().ref("/Guardian_list/");
        // 보호자에게 푸시 알림을 보냄
        guardianDataRef.once("value").then((guardianListSnapshot) => {
            guardianListSnapshot.forEach((guardianSnapshot) => {
                const guardianData = guardianSnapshot.val();
                const carereceiverId = guardianData.CareReceiverID;
                if (carereceiverId === userId && guardianData.deviceToken) {
                    const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);
                    userNameRef.once("value").then((userNameSnapshot) => {
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
                                admin.messaging().send(message).then((response) => {
                                    console.log("Message sent successfully:", response, "token: ", token);
                                })
                                    .catch((error) => {
                                    console.log("Error sending message: ", error);
                                });
                            }
                        }
                    });
                }
            });
        });
        // activity/time 삭제
        careReceiverDataRef.child("ActivityData").child("activity").child("time").remove();
        // 외출상황 기록
        careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot) => {
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
        latestEvent.push({
            time: outingTimeStamp,
            type: "outing"
        });
    }
});
// 활동량 감지 시 트리거
exports.checkActivities = functions.database.ref("CareReceiver_list/{userId}/ActivityData/activity/cnt")
    .onUpdate((snapshot, context) => {
    const cnt = parseInt(snapshot.after.val());
    const userId = context.params.userId;
    console.log("userId :", userId);
    //서울 시간 조회
    const date = new Date();
    const SEOUL_TIME_DIFF = 9;
    let hour = new Date(date).getHours() + SEOUL_TIME_DIFF;
    if (hour >= 24)
        hour -= 24;
    console.log("hour: ", hour);
    // 데이터베이스 참조
    const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/${userId}`);
    const guardianDataRef = admin.database().ref("/Guardian_list/");
    // cnt_list 업데이트
    careReceiverDataRef.child("ActivityData").child("activity").child("cnt_list").child(hour.toString()).once("value").then((cntSnapshot) => {
        let cntValue = parseInt(cntSnapshot.val());
        console.log("cntValue: ", cntValue);
        // cnt_list가 없는 경우 0으로 초기화
        if (cntValue === null) {
            cntValue = 0;
        }
        // door/outing === 0 인 경우에만 cnt_list 업데이트
        careReceiverDataRef.child("ActivityData").child("door").child("outing").once("value").then((outingSnapshot) => {
            const outingValue = outingSnapshot.val();
            console.log("outingValue: ", outingValue);
            if (outingValue === "0") {
                cntValue += cnt;
                careReceiverDataRef.child("ActivityData").child("activity").child("cnt_list").child(hour.toString()).set(cntValue.toString());
            }
        });
    });
    // time 업데이트
    careReceiverDataRef.child("ActivityData").child("activity").child("time").once("value").then((timeSnapshot) => {
        let timeValue = parseInt(timeSnapshot.val());
        const currentTime = Date.now();
        careReceiverDataRef.child("ActivityData").child("activity").child("time").set(currentTime.toString());
    });
});
// 주기적 활동량 감시
exports.onTracking = functions.pubsub.schedule("every 1 minutes").onRun((context) => {
    console.log("주기적 활동량 감시가 ", Date.now(), "에 실행되었습니다.");
    const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/`);
    const guardianDataRef = admin.database().ref("/Guardian_list/");
    // 활동량 미감지 코드 매핑
    let ACTIVITY_CODE;
    (function (ACTIVITY_CODE) {
        ACTIVITY_CODE[ACTIVITY_CODE["activityDetected"] = 0] = "activityDetected";
        ACTIVITY_CODE[ACTIVITY_CODE["noActivitiesDetectedLastEightHours"] = 1] = "noActivitiesDetectedLastEightHours";
        ACTIVITY_CODE[ACTIVITY_CODE["noActivitiesDetectedLastTwelveHours"] = 2] = "noActivitiesDetectedLastTwelveHours";
        ACTIVITY_CODE[ACTIVITY_CODE["noActivitiesDetectedLastTwentyFourHours"] = 3] = "noActivitiesDetectedLastTwentyFourHours";
        ACTIVITY_CODE[ACTIVITY_CODE["outOfBound"] = 4] = "outOfBound";
    })(ACTIVITY_CODE || (ACTIVITY_CODE = {}));
    // 모든 피보호자에 대해 활동량을 확인
    careReceiverDataRef.once("value").then((careReceiverListSnapshot) => {
        careReceiverListSnapshot.forEach((careReceiverSnapshot) => {
            const careReceiverData = careReceiverSnapshot.val();
            const userId = careReceiverSnapshot.key;
            // 피보호자의 활동량을 확인
            careReceiverDataRef.child(`${userId}/ActivityData/activity`).once("value").then((activitySnapshot) => {
                const activityData = activitySnapshot.val();
                const time = activityData.time;
                const currentTime = Date.now();
                const timeDiffHours = Math.floor((currentTime - time) / 1000 / 60 / 60);
                // ACTIVITY_CODE 필드가 없는 경우 0으로 초기화
                if (activityData.ACTIVITY_CODE === undefined) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.activityDetected);
                }
                // 8시간 이상 활동량이 감지되지 않았을 때
                if (timeDiffHours >= 8 && timeDiffHours < 12 && activityData.ACTIVITY_CODE !== 1) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.noActivitiesDetectedLastEightHours);
                }
                // 12시간 이상 활동량이 감지되지 않았을 때
                if (timeDiffHours >= 12 && timeDiffHours < 24 && activityData.ACTIVITY_CODE !== 2) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.noActivitiesDetectedLastTwelveHours);
                    // 피보호자의 보호자에게 푸시 알림을 보냄 && 활동량 미감지 기록
                    sendPushNotificationToGuardian(ACTIVITY_CODE.noActivitiesDetectedLastTwelveHours);
                    updateLatestEvent(ACTIVITY_CODE.noActivitiesDetectedLastTwelveHours);
                }
                // 24시간 이상 활동량이 감지되지 않았을 때
                if (timeDiffHours >= 24 && timeDiffHours < 30 && activityData.ACTIVITY_CODE !== 3) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.noActivitiesDetectedLastTwentyFourHours);
                    // 피보호자의 보호자에게 푸시 알림을 보냄 && 활동량 미감지 기록
                    sendPushNotificationToGuardian(ACTIVITY_CODE.noActivitiesDetectedLastTwentyFourHours);
                    updateLatestEvent(ACTIVITY_CODE.noActivitiesDetectedLastTwentyFourHours);
                }
                // 30시간 이상 활동량이 감지되지 않았을 때
                if (timeDiffHours >= 30 && activityData.ACTIVITY_CODE !== 4) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.outOfBound);
                }
                // 다시 활동을 감지했을 때 ACTIVITY_CODE를 0으로 초기화
                if (timeDiffHours < 8 && activityData.ACTIVITY_CODE !== 0) {
                    careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("ACTIVITY_CODE").set(ACTIVITY_CODE.activityDetected);
                }
            });
            // 피보호자의 보호자를 찾아서 푸시 알림을 보내는 함수
            function sendPushNotificationToGuardian(cases) {
                let HOUR = 0;
                cases === ACTIVITY_CODE.noActivitiesDetectedLastTwelveHours ? HOUR = 12 : HOUR = 24;
                guardianDataRef.once("value").then((guardianListSnapshot) => {
                    guardianListSnapshot.forEach((guardianSnapshot) => {
                        const guardianData = guardianSnapshot.val();
                        const carereceiverId = guardianData.CareReceiverID;
                        if (carereceiverId === userId && guardianData.deviceToken) {
                            const userNameRef = admin.database().ref(`/CareReceiver_list/${userId}/name`);
                            userNameRef.once("value").then((userNameSnapshot) => {
                                const name = userNameSnapshot.val();
                                const tokenObject = guardianData.deviceToken;
                                console.log("tokenObject: ", tokenObject);
                                for (const key in tokenObject) {
                                    if (Object.prototype.hasOwnProperty.call(tokenObject, key)) {
                                        const token = tokenObject[key];
                                        const message = {
                                            notification: {
                                                title: "활동 미감지 알림",
                                                body: `${name} 님의 활동이 ${HOUR} 시간 동안 감지되지 않습니다`,
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
                        }
                    });
                });
            }
            // latestEvent 업데이트하는 함수
            function updateLatestEvent(cases) {
                let type = "";
                cases === ACTIVITY_CODE.noActivitiesDetectedLastTwelveHours ? type = "no_movement_detected_1" : type = "no_movement_detected_2";
                careReceiverDataRef.child("ActivityData").child("latestEvent").once("value").then((latestEventSnapshot) => {
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
                const timeStamp = Date.now();
                const latestEvent = admin.database().ref(`/CareReceiver_list/${userId}/ActivityData/latestEvent`);
                latestEvent.push({
                    time: timeStamp,
                    type: type
                });
            }
        });
    });
});
// 매 자정마다 피보호자의 활동량을 초기화 및 하루 활동 로그를 저장
exports.onResetActivity = functions.pubsub.schedule("0 3 * * *").onRun((context) => {
    console.log("활동 로그 초기화가 ", Date.now(), "에 실행되었습니다.");
    const careReceiverDataRef = admin.database().ref(`/CareReceiver_list/`);
    const guardianDataRef = admin.database().ref("/Guardian_list/");
    // 모든 피보호자에 대해 활동량을 초기화
    careReceiverDataRef.once("value").then((careReceiverListSnapshot) => {
        careReceiverListSnapshot.forEach((careReceiverSnapshot) => {
            const careReceiverData = careReceiverSnapshot.val();
            const userId = careReceiverSnapshot.key;
            // 피보호자의 cnt_list를 초기화
            for (let i = 0; i < 24; i++) {
                careReceiverDataRef.child(`${userId}/ActivityData/activity`).child("cnt_list").child(i.toString()).set("0");
            }
            // 피보호자의 활동량 로그를 저장
            careReceiverDataRef.child(`${userId}/ActivityData/activity`).once("value").then((activitySnapshot) => {
                const activityData = activitySnapshot.val();
                const date = new Date().getDate() - 1;
                const cnt_list = activityData.cnt_list;
                admin.database().ref(`/CareReceiver_list/${userId}/ActivityData/activityLog`).child(date.toString()).set(cnt_list);
            });
        });
    });
});
