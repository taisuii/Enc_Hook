package taisuii.cn;

import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;

import java.util.HashMap;

public class taisui {
    private static final String TAG = "Enc_Hook";
    public static final String JUST = "Just";
    public static void log(String args) {
        if (args != null) Log.d(TAG, args);
    }

    public static void JustLog(String args) {
        if (args != null) Log.d(JUST, args);
    }
    public static void logParams(String before, Object[] params) {
        // 不同的类型的输出方式不一样
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object args = params[i];

                if (args.getClass() == byte[].class) {
                    Log.d(TAG, before + "[" + i + "] Byte[] raw: " + new String((byte[]) args));
                    Log.d(TAG, before + "[" + i + "] Byte[] Hex: " + Util.byteToHexString((byte[]) args));
                    Log.d(TAG, before + "[" + i + "] Byte[] Base64: " + Base64.encodeToString((byte[]) args, 0));
                } else if (args.getClass() == Integer.class) {
                    Log.d(TAG, before + "[" + i + "] Integer raw: " + args);
                } else if (args.getClass() == String.class) {
                    Log.d(TAG, before + "[" + i + "] String raw: " + args);
                } else if (args.getClass() == StringBuilder.class) {
                    Log.d(TAG, before + "[" + i + "] StringBuilder raw: " + args.toString());
                } else if(args.getClass() == HashMap.class){
                    Log.d(TAG, before + "[" + i + "] HashMap raw: " + new JSONObject((HashMap) args).toString());
                }else if(args.getClass() == JSONObject.class){
                    Log.d(TAG, before + "[" + i + "] JSONObject raw: " + args.toString());
                }else{
                    Log.d(TAG, before + "[" + i + "] unknown " + args.getClass().toString());
                    try {
                        Log.d(TAG, before + "[" + i + "] unknown raw: " + args.toString());
                    } catch (Exception e) {
                        Log.d(TAG, " [" + i + "] unknown err: " + e);
                    }

                }
            }
        }
    }

    public static void logResult(String before, Object result) {
        if (result != null) {
            if (result.getClass() == byte[].class) {
                Log.d(TAG, before + " Byte[] raw: " + new String((byte[]) result));
                Log.d(TAG, before + " Byte[] Hex: " + Util.byteToHexString((byte[]) result));
                Log.d(TAG, before + " Byte[] Base64: " + Base64.encodeToString((byte[]) result, 0));
            } else if (result.getClass() == Integer.class) {
                Log.d(TAG, before + " Integer raw: " + result.toString());
            } else if (result.getClass() == String.class) {
                Log.d(TAG, before + " String raw: " + result);
            } else if (result.getClass() == StringBuilder.class) {
                Log.d(TAG, before + " StringBuilder: " + result.toString());
            } else if (result.getClass() == HashMap.class) {
                Log.d(TAG, before + " HashMap: " + new JSONObject((HashMap) result).toString());
            } else if (result.getClass() == JSONObject.class) {
                Log.d(TAG, before + " JSONObject: " + result.toString());
            } else {
                Log.d(TAG, before + " unknown " + result.getClass().toString());
                try {
                    Log.d(TAG, before + " unknown raw: " + result.toString());
                } catch (Exception e) {
                    Log.d(TAG, " unknown err: " + e);
                }
            }


        }
    }
}
