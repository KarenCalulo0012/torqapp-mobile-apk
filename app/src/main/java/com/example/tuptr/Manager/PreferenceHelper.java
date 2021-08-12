package com.example.tuptr.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final String INTRO = "intro";
    private final String NAME = "username";
    private final String ROLEID = "roles_id";
    private final String GUARDNAME = "guard_username";
    private final String VGUARDNAME = "guard_username";
    private final String TITLE = "title";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
    public boolean getIsLogin() {
        return app_prefs.getBoolean(INTRO, false);
    }

    public void putName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginorout);
        edit.commit();
    }
    public String getName() {
        return app_prefs.getString(NAME, "");
    }

    public void putRoleid(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ROLEID, loginorout);
        edit.commit();
    }
    public String getRoleid() {
        return app_prefs.getString(ROLEID, "");
    }

    public void putGuardid(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(GUARDNAME, loginorout);
        edit.commit();
    }
    public void putvGuardid(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(VGUARDNAME, loginorout);
        edit.commit();
    }
    public String getGuardid() {

        return app_prefs.getString(NAME, "");
    }

    public String getvGuardid() {
        return app_prefs.getString(NAME, "");
    }

    public String getTitle() {
        return app_prefs.getString(TITLE, "");
    }

}
