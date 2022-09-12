package taisuii.cn;

import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;

import java.util.HashMap;

public class taisui {

    public static void EncLog(Object args) {
        if (args != null) {
            log_d("Enc_Hook", args.toString());
        }
    }

    public static void JustLog(String args) {
        if (args != null) {
            log_d("Just", args);
        }
    }

    public static void SelfLog(String args) {
        if (args != null) {
            log_d("Self_Hook", args);
        }
        ;
    }

    public static void TraceLog(String args) {

        if (args != null) {
            log_d("Trace", args);
        }
    }

    public static void logParams(String before, Object[] params, String tag) {

        // 不同的类型的输出方式不一样
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object args = params[i];

                if (args.getClass() == byte[].class) {
                    log_d(tag, before + "[" + i + "] Byte[] raw: " + new String((byte[]) args));
                    log_d(tag, before + "[" + i + "] Byte[] Hex: " + Util.byteToHexString((byte[]) args));
                    log_d(tag, before + "[" + i + "] Byte[] Base64: " + Base64.encodeToString((byte[]) args, 0));
                } else if (args.getClass() == Integer.class) {
                    log_d(tag, before + "[" + i + "] Integer raw: " + args);
                } else if (args.getClass() == String.class) {
                    log_d(tag, before + "[" + i + "] String raw: " + args);
                } else if (args.getClass() == StringBuilder.class) {
                    log_d(tag, before + "[" + i + "] StringBuilder raw: " + args.toString());
                } else if (args.getClass() == HashMap.class) {
                    log_d(tag, before + "[" + i + "] HashMap raw: " + new JSONObject((HashMap) args).toString());
                } else if (args.getClass() == JSONObject.class) {
                    log_d(tag, before + "[" + i + "] JSONObject raw: " + args.toString());
                } else {
                    log_d(tag, before + "[" + i + "] unknown " + args.getClass());
                    try {
                        log_d(tag, before + "[" + i + "] unknown raw: " + args.toString());
                    } catch (Exception e) {
                        log_d(tag, " [" + i + "] unknown err: " + e);
                    }

                }
            }
        }
    }

    public static void logResult(String before, Object result, String tag) {

        if (result != null) {
            if (result.getClass() == byte[].class) {
                log_d(tag, before + " Byte[] raw: " + new String((byte[]) result));
                log_d(tag, before + " Byte[] Hex: " + Util.byteToHexString((byte[]) result));
                log_d(tag, before + " Byte[] Base64: " + Base64.encodeToString((byte[]) result, 0));
            } else if (result.getClass() == Integer.class) {
                log_d(tag, before + " Integer raw: " + result.toString());
            } else if (result.getClass() == String.class) {
                log_d(tag, before + " String raw: " + result);
            } else if (result.getClass() == StringBuilder.class) {
                log_d(tag, before + " StringBuilder: " + result.toString());
            } else if (result.getClass() == HashMap.class) {
                log_d(tag, before + " HashMap: " + new JSONObject((HashMap) result).toString());
            } else if (result.getClass() == JSONObject.class) {
                log_d(tag, before + " JSONObject: " + result.toString());
            } else {
                log_d(tag, before + " unknown " + result.getClass().toString());
                try {
                    log_d(tag, before + " unknown raw: " + result.toString());
                } catch (Exception e) {
                    log_d(tag, " unknown err: " + e);
                }
            }
        }
    }

    public static void log_d(String tag, String data) {
        Log.d(tag, data);
        Util.AddLog(tag, data);
    }
}
