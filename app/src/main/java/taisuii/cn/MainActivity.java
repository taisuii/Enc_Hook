package taisuii.cn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Context context;
    static TextView packager;
    static TextView tv_reply;
    static TextView tv_sync;
    static TextView tv_notifications;
    @SuppressLint("StaticFieldLeak")
    static EditText editText;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packager = findViewById(R.id.packagename);
        tv_reply = findViewById(R.id.tv_reply);
        tv_sync = findViewById(R.id.tv_sync);
        tv_notifications = findViewById(R.id.tv_notifications);
        editText = findViewById(R.id.log);

        setContext("软件加载成功 太岁又沐风博客: taisuii.cn");
        setContext("XP镜像: x.taisuii.cn");
        setContext("当前设备SDK：" + android.os.Build.VERSION.SDK_INT);

        context = getApplicationContext();

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        setText();

        if (!isModuleActive()) {
            Toast.makeText(this, "模块未启动", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "模块已启动", Toast.LENGTH_SHORT).show();
        }


    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void read(View view) {
        Util.SetConf(Util.GetJson(pref));
        setContext("刷新成功");
        setText();
//        setContext(Util.ReadLog("Enc_Hook"));
    }

    public void delete(View view) {
//        Util.ClearLog("");
        editText.setText("");
    }


    @SuppressLint("SetTextI18n")
    public static void setText() {
        String json = Util.EncConfig();
        String Self = Util.SelfConfig();
        setContext(json);
        setContext(Self);

        packager.setText("当前目标: " + Util.GetJson(json, "package"));
        tv_reply.setText("作用域: " + Util.GetJson(json, "scope"));
        tv_sync.setText("Hook: " + Util.GetJson(json, "hook"));
        tv_notifications.setText("Enc/Self_hook: " + Util.GetJson(json, "enc") + "/" + Util.GetJson(json, "self_hook"));
        setContext("当前目标: " + Util.GetJson(json, "package"));
        setContext("作用域: " + Util.GetJson(json, "scope"));
        setContext("Hook: " + Util.GetJson(json, "hook"));
        setContext("Enc: " + Util.GetJson(json, "enc"));
        setContext("Self_hook: " + Util.GetJson(json, "self_hook"));
        setContext("JustTrustMePlus: " + Util.GetJson(json, "JustTrustMePlus"));
    }


    public static void setContext(String arg) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setSelection(editText.getText().length(), editText.getText().length());
        editText.getText().append("[").append(dateFormat.format(date)).append("]").append(arg).append("\n");
    }


    private boolean isModuleActive() {
        return false;
    }

}
