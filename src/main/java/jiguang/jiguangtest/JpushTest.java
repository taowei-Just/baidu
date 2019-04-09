package jiguang.jiguangtest;

import baidu.utils.Out;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM;
import static android.provider.ContactsContract.CommonDataKinds.Organization.TITLE;
import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;


public class JpushTest {

    public static String masterSecret = "789a8b55dfc994438bd454b3";
    public static String appKey = "dae9063656df96bf56cf63d2";
    private final PushClient jpushClient;

    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, TITLE, null))
                .build();
    }

    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("alias1"))
                .setNotification(Notification.alert(ALERT))
                .build();
    }

    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }

    public void testSendWithSMS() {

        try {
//            SMS sms = SMS.newBuilder()
//                    .setDelayTime(1000)
//                    .setContent("11111")
//                    .build();
//            PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "test sms", sms, "alias1");
            PushResult result = jpushClient.sendPush(buildPushObject_all_alias_alert());
            Out.d("Got result - " + result);
        } catch (APIConnectionException e) {
            Out.d("Connection error. Should retry later. ");
        } catch (APIRequestException e) {
            Out.d("Error response from JPush server. Should review and fix it. ");
            Out.d("HTTP Status: " + e.getStatus());
            Out.d("Error Code: " + e.getErrorCode());
            Out.d("Error Message: " + e.getErrorMessage());
        }
    }

    public JpushTest() {
        jpushClient = new PushClient(masterSecret, appKey);


    }

    public static void main(String[] args) {
        new JpushTest().start();
    }

    private void start() {
        PushPayload build = getPushPayload("message:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
        try {
            PushResult result = jpushClient.sendPush(build);
            Out.d("Got result - " + result);
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            Out.d("Connection error, should retry later");

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            Out.d("Should review the error, and fix the request");
            Out.d("HTTP Status: " + e.getStatus());
            Out.d("Error Code: " + e.getErrorCode());
            Out.d("Error Message: " + e.getErrorMessage());
        }
    }

    public static PushPayload getPushPayload(String message) {
        return (new PushPayload.Builder())
                    .setPlatform(Platform.all())
                    .setAudience(Audience.all())
                    .setNotification(Notification.alert(message))
                    .build();
    }
}
