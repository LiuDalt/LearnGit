package com.example.accessibility.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AccessibilityUtils {
    public static final String TAG = "AcessibilityManager";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean performAction(AccessibilityService service, ActionInfo actionInfo){
        AccessibilityNodeInfo targetNode = null;
        targetNode = findNodeInfo(service, actionInfo);
        if(targetNode != null) {
            if(actionInfo.isDetectAndClick()){
                return performAction(service, actionInfo.mDetectActionInfo);
            }
        }
        if (targetNode != null && targetNode.isClickable() && actionInfo.isClick()) {
            return targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        if (targetNode != null && targetNode.isClickable() && actionInfo.isInput()) {
            return performInput(service, targetNode, actionInfo);
        }
        return false;
    }

    private static boolean performInput(AccessibilityService service, AccessibilityNodeInfo targetNode, ActionInfo actionInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //android>=21 = 5.0时可以用ACTION_SET_TEXT
            Bundle arg = new Bundle();
            arg.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, (String) actionInfo.mExtra);
            return targetNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arg);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Bundle arguments = new Bundle();
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT, AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE);
            arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
//            if (isFromStart) {
//                node.performAction(AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, arguments);
//            } else {
//                node.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
//            }
            targetNode.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
            ClipboardManager cmb = (ClipboardManager) service.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText((String) actionInfo.mExtra);
            targetNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            return targetNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static AccessibilityNodeInfo findNodeInfo(AccessibilityService service, ActionInfo actionInfo) {
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        String resPrefix = WhatsAppConstant.RES_PREFIX;
        if(!TextUtils.isEmpty(actionInfo.mResStr)){
            return findNodeInfosById(nodeInfo,resPrefix + actionInfo.mResStr);
        }
        if(!TextUtils.isEmpty(actionInfo.mText)){
            return findNodeInfosByText(nodeInfo, actionInfo.mText);
        }
        return findNodeInfosByParent(nodeInfo, actionInfo);
    }

    private static AccessibilityNodeInfo findNodeInfosByParent(AccessibilityNodeInfo nodeInfo, ActionInfo actionInfo) {
        if(!TextUtils.isEmpty(actionInfo.mDescription)){
            return findNodeInfosByDescription(nodeInfo, actionInfo.mDescription);
        }
        if(actionInfo.mNodeIndex != null){
            return findNodeInfosByIndex(nodeInfo, actionInfo.mNodeIndex);
        }
        return null;
    }

    //通过id查找
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if(list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    //（通过文本查找）
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    //（通过父布局查找 index）
    public static AccessibilityNodeInfo findNodeInfosByIndex(AccessibilityNodeInfo rootNodeInfo, int[] indexs) {
        AccessibilityNodeInfo nodeInfo = rootNodeInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rootNodeInfo != null) {
            for(Integer index : indexs){
                nodeInfo = findNodeInfosByIndex(nodeInfo, index);
            }
            return nodeInfo;
        }
        return null;
    }

    //（通过父布局查找 index）
    public static AccessibilityNodeInfo findNodeInfosByIndex(AccessibilityNodeInfo nodeInfo, int index) {
        if(index < 0){
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            if(childCount <= index){
                return null;
            }
            return nodeInfo.getChild(index);
        }
        return null;
    }

    //（通过父布局查找 description）
    public static AccessibilityNodeInfo findNodeInfosByDescription(AccessibilityNodeInfo nodeInfo, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            for(int i = 0; i < childCount; i++){
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if(description.equals(child.getContentDescription())){
                    return child;
                }else if(child.getChildCount() > 0){
                    AccessibilityNodeInfo node = findNodeInfosByDescription(child, description);
                    if(node != null){
                        return node;
                    }
                }

            }
        }
        return null;
    }
}
