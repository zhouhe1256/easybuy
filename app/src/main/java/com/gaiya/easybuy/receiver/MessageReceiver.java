
package com.gaiya.easybuy.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.util.Logger;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.activity.LoginActivity;
import com.gaiya.easybuy.activity.MainActivity;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.model.PushModel;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import java.util.List;

/**
 * Created by dengt on 15-5-22.
 */
public class MessageReceiver extends BroadcastReceiver {

    private static int ids = 0;
    public static Activity baseActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid,
                        messageid, 90001);
                if (payload != null) {
                    String data = new String(payload);
                    Logger.d("GetuiSdkDemo", "Got Payload:" + data);// {"type":"1","url":"http://www.qq.com","content":"腾讯"}
                    // DialogUtil.showMessage(data);
                    try {
                        PushModel pushModel = JSONUtil.load(PushModel.class, data);
                        if (pushModel != null) {
                            handlePush(context, pushModel);
                        }
                    } catch (IllegalArgumentException e) {
                    }

                }
                break;
            case PushConsts.GET_CLIENTID:
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;
            default:
                break;
        }
    }

    private void handlePush(final Context context, final PushModel pushModel) {

        Intent intent;
        if (isRunning(context)) {

            if (!"3".equals(pushModel.getType())) {
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("push", pushModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                sendNotice(context, intent, pushModel);
            } else {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClass(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                sendNotice(context, intent, pushModel);
            }
        } else {

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("push", pushModel);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent).setTicker(pushModel.getContent()).
                    setContentTitle("柠檬豆").
                    setContentText(pushModel.getContent()).
                    setSmallIcon(R.drawable.ic_launcher).
                    setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND);
            Notification notification;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                notification = new Notification();
                notification.contentIntent = contentIntent;
                notification.tickerText = pushModel.getContent();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.defaults = Notification.DEFAULT_SOUND;
                notification.icon = R.drawable.ic_launcher;

            } else
                notification = builder.
                        build();
            mNotificationManager.notify(ids++, notification);
        }

    }

    public void sendNotice(Context context, Intent intent, PushModel pushModel) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, ids, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent).setTicker(pushModel.getContent()).
                setContentTitle("柠檬豆").
                setContentText(pushModel.getContent()).
                setSmallIcon(R.drawable.ic_launcher).
                setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);
        Notification notification;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification();
            notification.contentIntent = contentIntent;
            notification.tickerText = pushModel.getContent();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND;
            notification.icon = R.drawable.ic_launcher;
        } else
            notification = builder.
                    build();
        mNotificationManager.notify(ids++, notification);
    }

    public boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        System.out.println("packageName=" + packageName + ",topActivityClassName="
                + topActivityClassName);
        if (packageName != null && topActivityClassName != null
                && topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
    }

    private boolean isRunning(Context context) {
        // 判断应用是否在运行
        ActivityManager am = (ActivityManager) GApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = getPackageName(context);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Logger.i("isRunning",
                        info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="
                                + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context
                        .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

}
