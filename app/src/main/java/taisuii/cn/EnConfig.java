package taisuii.cn;

import org.json.JSONObject;

public class EnConfig {

    private String PackageName = "encrypt.taisuii.cn";
    private String Scope = "all";
    private boolean EncHook = true;
    private boolean SelfHook = true;
    private boolean Hook = true;

    private boolean JustTrustMePlus = true;




    public EnConfig() {
        String json = Util.EncConfig();
        try {
            JSONObject jsonObject = new JSONObject(json);
            PackageName = jsonObject.getString("package");
            Scope = jsonObject.getString("scope");
            JustTrustMePlus = jsonObject.getBoolean("JustTrustMePlus");
            Hook = jsonObject.getBoolean("hook");
            EncHook = jsonObject.getBoolean("enc");
            SelfHook = jsonObject.getBoolean("self_hook");
        } catch (Exception e) {
            taisui.EncLog(e);
        }

    }

    public boolean isJustTrustMePlus() {
        return JustTrustMePlus;
    }
    public String getPackageName() {
        return PackageName;
    }

    public String getScope() {
        return Scope;
    }

    public boolean isEncHook() {
        return EncHook;
    }

    public boolean isSelfHook() {
        return SelfHook;
    }

    public boolean isHook() {
        return Hook;
    }
}
