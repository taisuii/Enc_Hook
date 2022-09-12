package taisuii.cn;

import org.json.JSONObject;

public class TraceConfig {
    private String TraceMethod = "md5";
    private String TraceClass = "encrypt.taisuii.cn.MD5";
    private boolean isTraceMethod = true;

    private String FilterClass = "taisuii";

    private boolean isPrintClass = true;
    public TraceConfig(){
        String json = Util.TraceConfig();
        try {
            JSONObject jsonObject = new JSONObject(json);
            isTraceMethod = jsonObject.getBoolean("isTraceMethod");
            TraceClass = jsonObject.getString("TraceClass");
            TraceMethod = jsonObject.getString("TraceMethod");

            isPrintClass = jsonObject.getBoolean("isPrintClass");
            FilterClass = jsonObject.getString("FilterClass");
        }catch (Exception e){
            taisui.TraceLog(e.toString());
        }
    }
    public String getTraceClass() {
        return TraceClass;
    }
    public String getTraceMethod() {
        return TraceMethod;
    }

    public boolean isTraceMethod() {
        return isTraceMethod;
    }

    public String getFilterClass() {
        return FilterClass;
    }

    public boolean isPrintClass() {
        return isPrintClass;
    }
}
