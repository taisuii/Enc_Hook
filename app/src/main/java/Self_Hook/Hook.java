package Self_Hook;

import android.util.Base64;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import taisuii.cn.*;
import taisuii.cn.EnConfig;

public class Hook implements IXposedHookLoadPackage {
    private static final String Self = "Self_Hook";

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packager = loadPackageParam.packageName;

        boolean filter = true;
        EnConfig enConfig = new EnConfig();

        if (enConfig.getScope().equals("package")) filter = packager.equals(enConfig.getPackageName());

        if (enConfig.isHook() && filter && enConfig.isSelfHook()) {
            taisui.SelfLog("Hook Self target: " + packager);
            int run_num = SelfConfig.getNum();
            for (int i = 0; i < run_num; i++) {

                try {
                    SelfConfig selfConfig = new SelfConfig(i);

                    taisui.SelfLog("Class name :" + selfConfig.getClassName());

                    switch (selfConfig.getHookType()) {
                        case 0:
                            setField(selfConfig, loadPackageParam.classLoader);
                            break;
                        case 1:
                            HookConstructor(selfConfig, loadPackageParam.classLoader);
                            break;
                        case 2:
                            HookMethod(selfConfig, loadPackageParam.classLoader);
                            break;
                    }
                } catch (Exception e) {
                    taisui.SelfLog(e.toString());
                }


            }
        }
    }


    public static void setField(SelfConfig selfConfig, ClassLoader classLoader) throws Exception {

        for (int i = 0; i < selfConfig.getStaticListArray().length(); i++) {
            JSONObject jsonObject = selfConfig.getStaticListArray().getJSONObject(i);
            Class<?> clazz = XposedHelpers.findClass(selfConfig.getClassName(), classLoader);

            String key = jsonObject.getString("key");
            int typ = jsonObject.getInt("type");

            taisui.SelfLog(selfConfig.getClassName() + " setStatic: " + key);

            if (typ == 0)
                XposedHelpers.setStaticObjectField(clazz, key, jsonObject.getString("value"));
            if (typ == 1)
                XposedHelpers.setStaticIntField(clazz, key, jsonObject.getInt("value"));
            if (typ == 2)
                XposedHelpers.setStaticBooleanField(clazz, key, jsonObject.getBoolean("value"));
        }

    }

    public static void HookConstructor(SelfConfig selfConfig, ClassLoader classLoader) throws Exception{

        Class<?> clazz = XposedHelpers.findClass(selfConfig.getClassName(), classLoader);

        XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                taisui.SelfLog("===================== Constructor " + selfConfig.getClassName() + " =====================");
                taisui.SelfLog("Constructor arg num:" + param.args.length);
                for (int j = 0; j < param.args.length; j++) {
                    for (int k = 0; k < selfConfig.getArgListArray().length(); k++) {
                        JSONObject jsonObject = selfConfig.getArgListArray().getJSONObject(k);
                        if (jsonObject.getInt("n") == j) {
                            int typ = jsonObject.getInt("type");
                            if (typ == 0)
                                param.args[j] = jsonObject.getString("value");
                            if (typ == 1)
                                param.args[j] = jsonObject.getInt("value");
                            if (typ == 2)
                                param.args[j] = jsonObject.getBoolean("value");
                        }
                    }

                }
                taisui.logParams("Constructor arg", param.args, Self);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                taisui.SelfLog("===================== Constructor " + selfConfig.getClassName() + " =====================");
            }
        });

    }

    public static void HookMethod(SelfConfig selfConfig, ClassLoader classLoader) throws Exception{

        Class<?> clazz = XposedHelpers.findClass(selfConfig.getClassName(), classLoader);

        XposedBridge.hookAllMethods(clazz, selfConfig.getMethodName(), new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                taisui.SelfLog("===================== " + selfConfig.getClassName() + " =====================");
                taisui.SelfLog(selfConfig.getMethodName() + " arg num:" + param.args.length);
                int typ;
                for (int j = 0; j < param.args.length; j++) {

                    for (int k = 0; k < selfConfig.getArgListArray().length(); k++) {
                        JSONObject jsonObject3 = selfConfig.getArgListArray().getJSONObject(k);
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
                taisui.logParams(selfConfig.getMethodName() + " arg", param.args, Self);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (selfConfig.getReturnType() == 1) param.setResult(selfConfig.getReturnValue());
                if (selfConfig.getReturnType() == 2) param.setResult(selfConfig.getReturnValue());
                if (selfConfig.getReturnType() == 3) param.setResult(selfConfig.getReturnValue());
                taisui.logResult(selfConfig.getMethodName() + " reuturn", param.getResult(), Self);
                taisui.SelfLog("===================== " + selfConfig.getClassName() + " =====================");
            }
        });
    }
}


