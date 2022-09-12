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
    private static String TAG = "Enc_Hook";

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {


        // Xposed模块自检测
        if (loadPackageParam.packageName.equals("taisuii.cn")) {
            XposedHelpers.findAndHookMethod("taisuii.cn.MainActivity", loadPackageParam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }

        String packager = loadPackageParam.packageName;

        boolean filter = true;
        EnConfig enConfig = new EnConfig();

        if (enConfig.getScope().equals("package")) filter = packager.equals(enConfig.getPackageName());

        if (enConfig.isHook() && filter && enConfig.isEncHook()) {

            taisui.EncLog("Hook enc target: " + packager);

            XposedBridge.hookAllMethods(MessageDigest.class, "update", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

                    taisui.EncLog("===================== java.security.MessageDigest =====================");
                    MessageDigest mes = (MessageDigest) param.thisObject;
                    String type = mes.getAlgorithm();
                    taisui.logParams(type + " update Arg", param.args, TAG);

                }
            });

            XposedBridge.hookAllMethods(MessageDigest.class, "digest", new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    MessageDigest mes = (MessageDigest) param.thisObject;
                    String type = mes.getAlgorithm();
                    taisui.logParams(type + " digest Arg", param.args, TAG);
                    taisui.logResult(type + " digest return", param.getResult(), TAG);
                    taisui.EncLog("===================== java.security.MessageDigest =====================");

                }
            });


            XposedBridge.hookAllConstructors(SecretKeySpec.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Exception {
                    taisui.EncLog("============================== javax.crypto.spec.SecretKeySpec ============================");
                    SecretKeySpec sec = (SecretKeySpec) param.thisObject;
                    String algorithm = sec.getAlgorithm();

                    byte[] secret = (byte[]) param.args[0];
                    taisui.EncLog(algorithm + " key: " + new String(secret));
                    taisui.EncLog(algorithm + " SecretKeySpecB64: " + Base64.encodeToString(secret, 0) + "\n");
                }
            });


            XposedBridge.hookAllMethods(Cipher.class, "update", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Cipher cipher = (Cipher) param.thisObject;
                    String type = cipher.getAlgorithm();
                    taisui.logParams(type + " update Arg", param.args, TAG);
                }
            });

            XposedBridge.hookAllMethods(Cipher.class, "doFinal", new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Exception {
                    Cipher cipher = (Cipher) param.thisObject;
                    String type = cipher.getAlgorithm();
                    try {
                        String Iv_ = new String(cipher.getIV());
                        if (!Iv_.equals("") || Iv_ != null) taisui.EncLog(type + " IV: " + Iv_);
                    } catch (Exception e) {
                        taisui.EncLog(type + "getIv err: " + e.toString());
                    }
                    taisui.logParams(type + " doFinal Arg", param.args, TAG);
                    taisui.logResult(type + " doFinal return", param.getResult(), TAG);
                    taisui.EncLog("============================== javax.crypto.Cipher ============================");
                }
            });


            XposedBridge.hookAllMethods(Mac.class, "update", new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Mac mac = (Mac) param.thisObject;
                    String type = mac.getAlgorithm();
                    taisui.logParams(type + " update Arg", param.args, TAG);
                }
            });

            XposedBridge.hookAllMethods(Mac.class, "doFinal", new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Mac mac = (Mac) param.thisObject;
                    String type = mac.getAlgorithm();
                    taisui.logParams(type + " doFinal Arg", param.args, TAG);
                    ;
                    taisui.logResult(type + " doFinal return", param.getResult(), TAG);
                    taisui.EncLog("============================== javax.crypto.Mac ============================");

                }
            });


            // RSA密钥部分
            XposedBridge.hookAllConstructors(X509EncodedKeySpec.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    taisui.EncLog("============================== java.security.spec.X509EncodedKeySpec ============================");
                    taisui.EncLog("X509EncodedKeySpec arg num: " + param.args.length);
                    for (int i = 0; i < param.args.length; i++) {
                        taisui.EncLog("RSA X509EncodedKeySpec: " + Base64.encodeToString((byte[]) param.args[0], 0));
                    }
                }
            });

            XposedBridge.hookAllConstructors(PKCS8EncodedKeySpec.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    taisui.EncLog("============================== java.security.spec.PKCS8EncodedKeySpec ============================");
                    taisui.EncLog("PKCS8EncodedKeySpec arg num: " + param.args.length);
                    for (int i = 0; i < param.args.length; i++) {
                        taisui.EncLog("RSA PKCS8EncodedKeySpec: " + Base64.encodeToString((byte[]) param.args[0], 0));
                    }
                }
            });

            XposedBridge.hookAllConstructors(RSAPublicKeySpec.class, new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    taisui.EncLog("============================== java.security.spec.RSAPublicKeySpec ============================");
                    taisui.EncLog("RSA PublicKeySpec arg num: " + param.args.length);
                    for (int i = 0; i < param.args.length; i++) {
                        taisui.EncLog("RSA RSAPublicKeySpec toString: " + param.args[i]);
                    }
                }
            });



        }
    }

}


