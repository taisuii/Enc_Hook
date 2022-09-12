package Trace;

import android.util.Base64;
import android.util.Log;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import taisuii.cn.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Hook implements IXposedHookLoadPackage {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packager = loadPackageParam.packageName;

        boolean filter = true;
        EnConfig enConfig = new EnConfig();
        TraceConfig traceConfig = new TraceConfig();
        if (enConfig.getScope().equals("package")) filter = packager.equals(enConfig.getPackageName());

        if (enConfig.isHook() && filter && traceConfig.isPrintClass()) {

            taisui.TraceLog("Hook PrintClass:" + packager);

            XposedBridge.hookAllMethods(ClassLoader.class, "loadClass", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    Class<?> clazz = (Class<?>) param.getResult();

                    Boolean flag = false;


                    Method[] methods = clazz.getDeclaredMethods();

                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].toString().contains(traceConfig.getFilterClass())){
                            flag = true;
                        }
                    }


                    if (flag){
                        taisui.TraceLog("===================== " + clazz.getName() + " =====================");
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].toString().contains(traceConfig.getFilterClass())){
                                taisui.TraceLog(methods[i].toString());
                            }
                        }
                        taisui.TraceLog("===================== " + clazz.getName() + " =====================");
                    }

                }
            });


        }

        if (enConfig.isHook() && filter && traceConfig.isTraceMethod()) {
            taisui.TraceLog("Hook TraceMethod target: " + packager);
            Class<?> clazz = XposedHelpers.findClass(traceConfig.getTraceClass(), loadPackageParam.classLoader);
            XposedBridge.hookAllMethods(clazz, traceConfig.getTraceMethod(), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    taisui.TraceLog("===================== " + traceConfig.getTraceClass() + " =====================");
                    StackTraceElement[] Trace = new Throwable("Trace_taisui").getStackTrace();
                    for (int i = 0; i < Trace.length; i++) {
                        taisui.TraceLog(Trace[i].toString());
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    taisui.TraceLog("===================== " + traceConfig.getTraceClass() + " =====================");
                    super.afterHookedMethod(param);
                }
            });

        }


    }

}


