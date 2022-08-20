package taisuii.cn;

import android.util.Base64;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Hook implements IXposedHookLoadPackage {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam){


        // Xposed模块自检测
        if (loadPackageParam.packageName.equals("taisuii.cn")) {
            XposedHelpers.findAndHookMethod("taisuii.cn.MainActivity", loadPackageParam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }

        String packager = loadPackageParam.packageName;
        String Hook_package = "encrypt.taisuii.cn";
        String Hook_scope = "all";
        boolean Hook_hook = true;
        boolean Hook_enc = true;
        boolean Self_hook = true;
        boolean filter = true;

        try {
            String json = Util.GetConf();
            JSONObject jsonObject = new JSONObject(json);
            Hook_package = jsonObject.getString("package");
            Hook_hook = jsonObject.getBoolean("hook");
            Hook_enc = jsonObject.getBoolean("enc");
            Hook_scope = jsonObject.getString("scope");
            Self_hook = jsonObject.getBoolean("self_hook");
        } catch (Exception e) {
            taisui.log("JSON Exception:" + e);
        }


        if (Hook_scope.equals("package")) filter = packager.equals(Hook_package);

        if (Hook_hook && filter) {

            try {
                //enc状态
                if (Hook_enc) {
                    taisui.log("Hook enc target: " + packager);

                    XposedBridge.hookAllMethods(MessageDigest.class, "update", new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

                            taisui.log("===================== java.security.MessageDigest =====================");
                            MessageDigest mes = (MessageDigest) param.thisObject;
                            String type = mes.getAlgorithm();
                            taisui.logParams(type + " update Arg", param.args);

                        }
                    });

                    XposedBridge.hookAllMethods(MessageDigest.class, "digest", new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            MessageDigest mes = (MessageDigest) param.thisObject;
                            String type = mes.getAlgorithm();
                            taisui.logParams(type + " digest Arg", param.args);
                            taisui.logResult(type + " digest return", param.getResult());
                            taisui.log("===================== java.security.MessageDigest =====================");

                        }
                    });


                    XposedBridge.hookAllConstructors(SecretKeySpec.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Exception {
                            taisui.log("============================== javax.crypto.spec.SecretKeySpec ============================");
                            SecretKeySpec sec = (SecretKeySpec) param.thisObject;
                            String algorithm = sec.getAlgorithm();

                            byte[] secret = (byte[]) param.args[0];
                            taisui.log(algorithm + " key: " + new String(secret));
                            taisui.log(algorithm + " SecretKeySpecB64: " + Base64.encodeToString(secret, 0) + "\n");
                        }
                    });


                    XposedBridge.hookAllMethods(Cipher.class, "update", new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            Cipher cipher = (Cipher) param.thisObject;
                            String type = cipher.getAlgorithm();
                            taisui.logParams(type + " update Arg", param.args);
                        }
                    });

                    XposedBridge.hookAllMethods(Cipher.class, "doFinal", new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Exception {
                            Cipher cipher = (Cipher) param.thisObject;
                            String type = cipher.getAlgorithm();
                            try {
                                String Iv_ = new String(cipher.getIV());
                                if (!Iv_.equals("") || Iv_ != null) taisui.log(type + " IV: " + Iv_);
                            } catch (Exception e) {
                                taisui.log(type + "getIv err: " + e.toString());
                            }
                            taisui.logParams(type + " doFinal Arg", param.args);
                            taisui.logResult(type + " doFinal return", param.getResult());
                            taisui.log("============================== javax.crypto.Cipher ============================");
                        }
                    });


                    XposedBridge.hookAllMethods(Mac.class, "update", new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            Mac mac = (Mac) param.thisObject;
                            String type = mac.getAlgorithm();
                            taisui.logParams(type + " update Arg", param.args);
                        }
                    });

                    XposedBridge.hookAllMethods(Mac.class, "doFinal", new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            Mac mac = (Mac) param.thisObject;
                            String type = mac.getAlgorithm();
                            taisui.logParams(type + " doFinal Arg", param.args);
                            taisui.logResult(type + " doFinal return", param.getResult());
                            taisui.log("============================== javax.crypto.Mac ============================");

                        }
                    });


                    // RSA密钥部分
                    XposedBridge.hookAllConstructors(X509EncodedKeySpec.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            taisui.log("============================== java.security.spec.X509EncodedKeySpec ============================");
                            taisui.log("X509EncodedKeySpec arg num: " + param.args.length);
                            for (int i = 0; i < param.args.length; i++) {
                                taisui.log("RSA X509EncodedKeySpec: " + Base64.encodeToString((byte[]) param.args[0], 0));
                            }
                        }
                    });

                    XposedBridge.hookAllConstructors(PKCS8EncodedKeySpec.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            taisui.log("============================== java.security.spec.PKCS8EncodedKeySpec ============================");
                            taisui.log("PKCS8EncodedKeySpec arg num: " + param.args.length);
                            for (int i = 0; i < param.args.length; i++) {
                                taisui.log("RSA PKCS8EncodedKeySpec: " + Base64.encodeToString((byte[]) param.args[0], 0));
                            }
                        }
                    });

                    XposedBridge.hookAllConstructors(RSAPublicKeySpec.class, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            taisui.log("============================== java.security.spec.RSAPublicKeySpec ============================");
                            taisui.log("RSA PublicKeySpec arg num: " + param.args.length);
                            for (int i = 0; i < param.args.length; i++) {
                                taisui.log("RSA RSAPublicKeySpec toString: " + param.args[i]);
                            }
                        }
                    });

                }
            }catch (Exception e){
                taisui.log(e.toString());
            }

            try {
                // 自设 Hook
                if (Self_hook) {
                    taisui.log("Hook Self target: " + packager);
                    String self = Util.Self();

                    JSONArray jsonArray = new JSONArray(self);

                    int run_num = jsonArray.length();

                    for (int i = 0; i < run_num; i++) {

                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        int retype = 0;
                        int type = 0;
                        JSONArray jsonArray2 = new JSONArray("[]");
                        JSONArray jsonArray3 = new JSONArray("[]");
                        String clas = "";
                        String method = "";

                        try {
                            type = jsonObject2.getInt("type");
                            clas = jsonObject2.getString("classname");
                            method = jsonObject2.getString("method");
                            jsonArray2 = new JSONArray(jsonObject2.getJSONArray("arglist").toString());
                            jsonArray3 = new JSONArray(jsonObject2.getJSONArray("staticlist").toString());
                            retype = jsonObject2.getInt("returntype");
                        } catch (Exception e) {
                            taisui.log("self hook get config hava a Exception: " + e);
                        }

                        if (type == 0) {

                            String key;
                            int typ;
                            for (int j = 0; j < jsonArray3.length(); j++) {
                                JSONObject jsonObject3 = jsonArray3.getJSONObject(i);
                                Class<?> clazz = XposedHelpers.findClass(clas, loadPackageParam.classLoader);
                                key = jsonObject3.getString("key");
                                typ = jsonObject3.getInt("type");
                                taisui.log(clas + " setStatic: " + key);
                                if (typ == 0)
                                    XposedHelpers.setStaticObjectField(clazz, key, jsonObject3.getString("value"));
                                if (typ == 1)
                                    XposedHelpers.setStaticIntField(clazz, key, jsonObject3.getInt("value"));
                                if (typ == 2)
                                    XposedHelpers.setStaticBooleanField(clazz, key, jsonObject3.getBoolean("value"));
                            }

                        } else if (type == 1) {

                            Class<?> clazz = XposedHelpers.findClass(clas, loadPackageParam.classLoader);
                            JSONArray finalJsonArray = jsonArray2;
                            String finalClas = clas;
                            XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    taisui.log("===================== Constructor " + finalClas + " =====================");
                                    taisui.log("Constructor arg num:" + param.args.length);
                                    int typ;
                                    for (int j = 0; j < param.args.length; j++) {
                                        for (int k = 0; k < finalJsonArray.length(); k++) {
                                            JSONObject jsonObject3 = finalJsonArray.getJSONObject(k);
                                            if (jsonObject3.getInt("n") == j) {
                                                typ = jsonObject3.getInt("type");
                                                if (typ == 0)
                                                    param.args[j] = jsonObject3.getString("value");
                                                if (typ == 1)
                                                    param.args[j] = jsonObject3.getInt("value");
                                                if (typ == 2)
                                                    param.args[j] = jsonObject3.getBoolean("value");
                                            }
                                        }

                                    }
                                    taisui.logParams("Constructor arg", param.args);
                                }

                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    taisui.log("===================== Constructor " + finalClas + " =====================");
                                }
                            });


                        } else if (type == 2) {

                            Class<?> clazz = XposedHelpers.findClass(clas, loadPackageParam.classLoader);
                            int finalRetype1 = retype;
                            JSONArray finalJsonArray1 = jsonArray2;
                            String finalClas1 = clas;
                            String finalMethod = method;
                            XposedBridge.hookAllMethods(clazz, method, new XC_MethodHook() {
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    taisui.log("===================== " + finalClas1 + " =====================");
                                    taisui.log(finalMethod + " arg num:" + param.args.length);
                                    int typ;
                                    for (int j = 0; j < param.args.length; j++) {

                                        for (int k = 0; k < finalJsonArray1.length(); k++) {
                                            JSONObject jsonObject3 = finalJsonArray1.getJSONObject(k);
                                            if (jsonObject3.getInt("n") == j) {
                                                typ = jsonObject3.getInt("type");
                                                if (typ == 0)
                                                    param.args[j] = jsonObject3.getString("value");
                                                if (typ == 1)
                                                    param.args[j] = jsonObject3.getInt("value");
                                                if (typ == 2)
                                                    param.args[j] = jsonObject3.getBoolean("value");
                                            }
                                        }
                                    }
                                    taisui.logParams(finalMethod + " arg", param.args);
                                }

                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    if (finalRetype1 == 1) param.setResult(jsonObject2.getString("returnvalue"));
                                    if (finalRetype1 == 2) param.setResult(jsonObject2.getInt("returnvalue"));
                                    if (finalRetype1 == 3) param.setResult(jsonObject2.getBoolean("returnvalue"));
                                    taisui.logResult(finalMethod + " reuturn", param.getResult());
                                    taisui.log("===================== " + finalClas1 + " =====================");
                                }
                            });


                        }
                    }

                }
            }catch (Exception e){
                taisui.log(e.toString());
            }


        }

    }

}


