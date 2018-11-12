package com.example.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AccessibilityUtils {
    public static final String TAG = "AccessibilityUtils";
    //借箭（点击）
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean performClick(AccessibilityService context, String resourceId) {

        Log.i("mService","点击执行");
        AccessibilityNodeInfo nodeInfo = context.getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        targetNode = findNodeInfosById(nodeInfo,"com.whatsapp:id/" + resourceId);
        if (targetNode != null && targetNode.isClickable()) {
            targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG, "performClick() called with: context = [" + context + "], resourceId = [" + resourceId + "]");
            return true;
        }
        return false;
    }


    //调用兵力（通过id查找）
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if(list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    //调用船只（通过文本查找）
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean performInputText(MyAccessibilityService service, String resourceId, String text) {
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        targetNode = findNodeInfosById(nodeInfo,"com.whatsapp:id/" + resourceId);
        if (Build.VERSION.SDK_INT >= 21 && targetNode != null) {
            //android>=21 = 5.0时可以用ACTION_SET_TEXT
            Bundle arg = new Bundle();
            arg.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            targetNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arg);
            Log.d(TAG, "performInputText() called with: service = [" + service + "], resourceId = [" + resourceId + "], text = [" + text + "]");
            return true;
        }
        return false;

    }
}
