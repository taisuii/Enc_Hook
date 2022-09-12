package taisuii.cn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelfConfig {
    private int HookType;
    private String ClassName;
    private String MethodName;

    private int ReturnType;
    private Object ReturnValue;

    private JSONArray ArgListArray;

    public int getHookType() {
        return HookType;
    }

    public String getClassName() {
        return ClassName;
    }

    public String getMethodName() {
        return MethodName;
    }

    public int getReturnType() {
        return ReturnType;
    }

    public Object getReturnValue() {
        return ReturnValue;
    }

    public JSONArray getArgListArray() {
        return ArgListArray;
    }

    public JSONArray getStaticListArray() {
        return StaticListArray;
    }

    private JSONArray StaticListArray;

    public SelfConfig(int index) throws JSONException {
        String json = Util.SelfConfig();
        JSONArray jsonArray = new JSONArray(json);

        JSONObject jsonObject = jsonArray.getJSONObject(index);

        HookType = jsonObject.getInt("type");
        ClassName = jsonObject.getString("classname");
        MethodName = jsonObject.getString("method");

        ReturnType = jsonObject.getInt("returntype");

        switch (ReturnType) {
            case 1:
                ReturnValue = jsonObject.getString("returnvalue");
                break;
            case 2:
                ReturnValue = jsonObject.getInt("returnvalue");
                break;
            case 3:
                ReturnValue = jsonObject.getBoolean("returnvalue");
                break;
        }

        ArgListArray = jsonObject.getJSONArray("arglist");
        StaticListArray = jsonObject.getJSONArray("staticlist");

    }


    public static int getNum() {
        int num = 0;
        try {
            String json = Util.SelfConfig();
            JSONArray jsonArray = new JSONArray(json);
            num = jsonArray.length();
        } catch (Exception e) {
            taisui.EncLog(e);
        }
        return num;
    }

}
