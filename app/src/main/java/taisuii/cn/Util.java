package taisuii.cn;

import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Util {

    public static String PATH = "data/local/tmp/taisui.json";
    public static String SELF_PATH = "data/local/tmp/self_hook.json";

    public static String TRACE_PATH = "data/local/tmp/Trace.json";

    public static String TraceConfig() {
        String result = "";
        Process process;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(new String[]{"sh", "-c", "cat " + TRACE_PATH});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
        return result;
    }

    public static String SelfConfig() {
        String result = "";
        Process process;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(new String[]{"sh", "-c", "cat " + SELF_PATH});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
        return result;
    }

    public static String EncConfig() {
        String result = "";
        Process process;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(new String[]{"sh", "-c", "cat " + PATH});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
        return result;
    }

    public static void SetConf(String Json) {

        Runtime runtime = Runtime.getRuntime();
        try {
            if (new File(PATH).exists() && new File(SELF_PATH).exists() && new File(TRACE_PATH).exists()) {
                runtime.exec(new String[]{"sh", "-c", "echo \"" + Json.replace("\"", "\\\"") + "\" > " + PATH});
                runtime.exec(new String[]{"sh", "-c", "chmod 777 " + PATH});
                runtime.exec(new String[]{"sh", "-c", "chmod 777 " + SELF_PATH});
                runtime.exec(new String[]{"sh", "-c", "chmod 777 " + TRACE_PATH});
            } else {
                MainActivity.setContext("申请ROOT权限");
                runtime.exec(new String[]{"su", "-c",
                        "echo \"" + Json.replace("\"", "\\\"") + "\" > " + PATH + "\n"
                                + "chmod 777 " + PATH + "\n"
                                + "echo [] > " + SELF_PATH + "\n"
                                + "chmod 777 " + SELF_PATH + "\n"
                                + "echo {} > " + TRACE_PATH + "\n"
                                + "chmod 777 " + TRACE_PATH + "\n"
                });
            }


        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
    }

    public static String GetJson(String arg1, String arg2) {
        String ret = "";
        if (arg1.equals("")) {
            MainActivity.setContext("软件尚未配置，无法读取数据");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(arg1);
                ret = jsonObject.getString(arg2);
            } catch (Exception e) {
                MainActivity.setContext(e.toString());
            }
        }
        return ret;
    }

    public static String GetJson(SharedPreferences pref) {
        String packager = pref.getString("package", "加密测试\nencrypt.taisuii.cn").split("\n")[1].replace("\n", "").replace(" ", "");
        String scope = pref.getString("scope", "all");
        boolean Hook = pref.getBoolean("Hook", true);
        boolean Enc_Hook = pref.getBoolean("Enc_Hook", true);
        boolean JustTrustMe = pref.getBoolean("JustTrustMePlus", true);
        boolean self_hook = pref.getBoolean("Self_Hook", false);
        JSONObject json = new JSONObject();
        try {
            json.put("package", packager);
            json.put("scope", scope);
            json.put("hook", Hook);
            json.put("enc", Enc_Hook);
            json.put("self_hook", self_hook);
            json.put("JustTrustMePlus", JustTrustMe);

        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
        return json.toString();
    }

    public static void AddLog(String tag, String args) {
        Runtime runtime = Runtime.getRuntime();
        try {
            if (new File("data/local/tmp/" + tag + ".log").exists()) {
                runtime.exec(new String[]{"sh", "-c", "echo \"" + args.replace("\"", "\\\"") + "\" >> data/local/tmp/" + tag + ".log"});
            } else {
                runtime.exec(new String[]{"su", "-c", " echo QQ27788854 > data/local/tmp/" + tag + ".log\nchmod 777 data/local/tmp/" + tag + ".log"});
            }

        } catch (Exception e) {
            Log.d(tag, "AddLog: " + e);
        }
    }


    public static void ClearLog(String tag) {
        Runtime runtime = Runtime.getRuntime();
        try {
            if (Objects.equals(tag, "")) {
                runtime.exec(new String[]{"sh", "-c", " echo QQ27788854 > data/local/tmp/Enc_Hook.log"});
                runtime.exec(new String[]{"sh", "-c", " echo QQ27788854 > data/local/tmp/Just.log"});
                runtime.exec(new String[]{"sh", "-c", " echo QQ27788854 > data/local/tmp/Self_Hook.log"});
                runtime.exec(new String[]{"sh", "-c", " echo QQ27788854 > data/local/tmp/Trace.log"});
            } else {
                runtime.exec(" echo QQ27788854 > " + tag + ".log");
            }

        } catch (Exception e) {
            Log.d(tag, "AddLog: " + e);
        }
    }

    public static String ReadLog(String tag) {
        String result = "";
        Process process;
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(new String[]{"sh", "-c", "cat data/local/tmp/" + tag + ".log"});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            MainActivity.setContext(e.toString());
        }
        return result;
    }

    public static String byteToHexString(byte[] by) {
        StringBuffer SB = new StringBuffer();
        for (byte k : by) {
            int j = k;
            if (k < 0) {
                j = k + 256;
            }
            if (j < 16) {
                SB.append("0");
            }
            SB.append(Integer.toHexString(j));
        }
        return SB.toString();
    }


}
