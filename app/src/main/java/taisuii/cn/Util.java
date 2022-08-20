package taisuii.cn;

import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public static String PATH = "data/local/tmp/taisui.json";
    public static String SELF_PATH = "data/local/tmp/self_hook.json";

    //    public static String GetConf() {
//        String result = "";
//        File file = new File(PATH);
//        if (file.isDirectory()) {
//            Log.d("载入配置文件", "没有发现配置文件");
//        } else {
//            try {
//                FileInputStream f = new FileInputStream(PATH);
//                BufferedReader bis = new BufferedReader(new InputStreamReader(f));
//                while (true) {
//                    String line = bis.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    result = result + line;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                MainActivity.setContext(e.toString());
//                Log.d("载入配置文件", Log.getStackTraceString(e));
//            }
//        }
//        return result;
//    }
    public static String GetConf() {
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

    public static String Self() {
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

    public static void SetConf(String Json) {

        Runtime runtime = Runtime.getRuntime();
            try {
                if (new File(PATH).exists()&& new File(SELF_PATH).exists()) {
                    runtime.exec(new String[]{"sh", "-c", "echo \"" + Json.replace("\"", "\\\"") + "\" > " + PATH});
                    runtime.exec(new String[]{"sh", "-c", "chmod 777 " + PATH});
                    runtime.exec(new String[]{"sh", "-c", "chmod 777 " + SELF_PATH});
                } else {
                    MainActivity.setContext("申请ROOT权限");
                    runtime.exec(new String[]{"su", "-c",
                            "echo \"" + Json.replace("\"", "\\\"") + "\" > " + PATH + "\n"
                                    + "chmod 777 " + PATH + "\n"
                                    + "echo [] > " + SELF_PATH + "\n"
                                    + "chmod 777 " + SELF_PATH});
                }


            } catch (Exception e) {
                MainActivity.setContext(e.toString());
            }
    }

    public static final String GetJson(String arg1, String arg2) {
        String ret = "";
        if (arg1 == "") {
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

    public static final String GetJson(SharedPreferences pref) {

        String packager = pref.getString("package", "encrypt.taisuii.cn");
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
}
