package com.gxnova.appgxnova.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestiona el token JWT y los datos básicos de sesión del usuario.
 */
public class SessionManager {

    private static final String PREF_NAME = "gxnova_session";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROL = "user_rol";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String token, int userId, String name, String rol) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_ROL, rol)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    /**
     * Devuelve el header "Bearer <token>" listo para usar en llamadas autenticadas.
     */
    public String getBearerToken() {
        String token = getToken();
        return token != null ? "Bearer " + token : null;
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public String getUserRol() {
        return prefs.getString(KEY_USER_ROL, "");
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
