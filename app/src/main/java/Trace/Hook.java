package Trace;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import dalvik.system.DexFile;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import taisuii.cn.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Objects;

public class Hook implements IXposedHookLoadPackage {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packager = loadPackageParam.packageName;

        boolean filter = true;
        EnConfig enConfig = new EnConfig();
        TraceConfig traceConfig = new TraceConfig();
        if (enConfig.getScope().equals("package")) filter = packager.equals(enConfig.getPackageName());

        if (enConfig.isHook() && filter && traceConfig.isPrintClass()) {

            taisui.TraceLog("Hook PrintClass:" + packager);


            XposedHelpers.findAndHookMethod(Application.class, "attach",
                    Context.class,
                    new XC_MethodHook() {

                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            ClassLoader mLoader = ((Context) param.args[0]).getClassLoader();

                            try {
                                Field pathListField = Objects.requireNonNull(mLoader.getClass().getSuperclass()).getDeclaredField("pathList");
                                pathListField.setAccessible(true);
                                Object dexPathList = pathListField.get(mLoader);
                                Field dexElementsField = dexPathList.getClass().getDeclaredField("dexElements");
                                dexElementsField.setAccessible(true);
                                Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);
                                for (int i = 0; i < dexElements.length; i++) {
                                    Field dexFileField = dexElements[i].getClass().getDeclaredField("dexFile");
                                    dexFileField.setAccessible(true);
                                    DexFile dexFile = (DexFile) dexFileField.get(dexElements[i]);
                                    Enumeration<String> enumeration = dexFile.entries();
                                    while (enumeration.hasMoreElements()) {//遍历
                                        String className = enumeration.nextElement();

                                        taisui.TraceLog(className);

                                        Class clazz = XposedHelpers.findClass(className, mLoader);
                                        Boolean flag = false;

                                        Method[] methods = clazz.getDeclaredMethods();

                                        for (int j = 0; j < methods.length; j++) {

                                            if (methods[i].toString().contains(traceConfig.getFilterClass())) {
                                                flag = true;
                                            }
                                        }

                                        if (flag) {
                                            taisui.TraceLog("===================== " + clazz.getName() + " =====================");
                                            for (int j = 0; j < methods.length; j++) {
                                                if (methods[i].toString().contains(traceConfig.getFilterClass())) {
                                                    taisui.TraceLog(methods[i].toString());
                                                }
                                            }
                                            taisui.TraceLog("===================== " + clazz.getName() + " =====================");
                                        }

                                    }
                                }

                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
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


