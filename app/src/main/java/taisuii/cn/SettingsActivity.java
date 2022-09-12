package taisuii.cn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.*;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Setting");
        context = getApplicationContext();
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
        CharSequence[] charSequences;
        Drawable[] icon;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            ListPreference listPreference = findPreference("package");
            ContextWrapper contextWrapper = new ContextWrapper(context);

            int i = 0;

            for (PackageInfo packageInfo : contextWrapper.getPackageManager().getInstalledPackages(0)) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    i++;
                }
            }

            charSequences = new CharSequence[i];
            icon = new Drawable[i];

            i = 0;
            for (PackageInfo packageInfo : contextWrapper.getPackageManager().getInstalledPackages(0)) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    charSequences[i] = packageInfo.applicationInfo.loadLabel(contextWrapper.getPackageManager()).toString() + "\n" + packageInfo.packageName;
                    icon[i] = packageInfo.applicationInfo.loadIcon(contextWrapper.getPackageManager());
                    i++;
                }
            }

            listPreference.setEntries(charSequences);
            listPreference.setEntryValues(charSequences);

//                    appBean.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//                    appBean.packageName = packageInfo.packageName;
//                    appBean.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
            preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    ContextWrapper contextWrapper = new ContextWrapper(context);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contextWrapper.getBaseContext());
                    String json = Util.GetJson(pref);
                    Util.SetConf(json);
                    MainActivity.setContext("刷新成功");
                    MainActivity.setText();

                    if (s.equals("package")) {
                        ListPreference listPreference = findPreference(s);
                        String packagename = (String) listPreference.getEntry();
                        int index = listPreference.findIndexOfValue(packagename);
                        listPreference.setIcon(icon[index]);
                        listPreference.setDialogIcon(icon[index]);
                        listPreference.setDialogTitle(packagename.split("\n")[0]);
                    }

                }
            };
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        }

    }

}
