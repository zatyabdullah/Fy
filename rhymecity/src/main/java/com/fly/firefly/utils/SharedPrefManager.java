package com.fly.firefly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SharedPrefManager {
    private static final String PREF_NAME = "AndroidHivePref";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String SELECTED = "SELECTED";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_INFO = "USER_INFO";

    public static final String TITLE = "TITLE";
    public static final String FLIGHT = "FLIGHT";
    public static final String COUNTRY = "COUNTRY";
    public static final String STATE = "STATE";

    public static final String ISLOGIN = "ISLOGIN";
    public static final String ISNEWSLETTER = "ISNEWSLETTER";
    public static final String PROMO_BANNER = "PM";
    public static final String DEFAULT_BANNER = "DB";
    public static final String USERNAME = "USERNAME";

    // public static final String SELECTED = "SELECTED";



    int PRIVATE_MODE = 0;
    Context _context;
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public SharedPrefManager(Context context) {
        this._context = context;
        _sharedPrefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        _prefsEditor = _sharedPrefs.edit();
    }

    /*Return Login Status*/
    public HashMap<String, String> getPromoBanner() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PROMO_BANNER, _sharedPrefs.getString(PROMO_BANNER, null));
        return init;
    }

    /*Return State*/
    public HashMap<String, String> getState() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(STATE, _sharedPrefs.getString(STATE, null));
        return init;
    }

    /*Return Username*/
    public HashMap<String, String> getUsername() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USERNAME, _sharedPrefs.getString(USERNAME, null));
        return init;
    }

    /*Return UserInfo*/
    public HashMap<String, String> getUserInfo() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USER_INFO, _sharedPrefs.getString(USER_INFO, null));
        return init;
    }


    /*Return Login Status*/
    public HashMap<String, String> getDefaultBanner() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(DEFAULT_BANNER, _sharedPrefs.getString(DEFAULT_BANNER, null));
        return init;
    }


    /*Return Login Status*/
    public HashMap<String, String> getLoginStatus() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(ISLOGIN, _sharedPrefs.getString(ISLOGIN, null));
        return init;
    }

    /*Return newsletter Status*/
    public HashMap<String, String> getNewsletterStatus() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(ISNEWSLETTER, _sharedPrefs.getString(ISNEWSLETTER, null));
        return init;
    }


    /*Return Signature Value*/
    public HashMap<String, String> getSignatureFromLocalStorage() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SIGNATURE, _sharedPrefs.getString(SIGNATURE, null));
        return init;
    }

    /*Return Selected Value*/
    public HashMap<String, String> getSelectedPopupSelection() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SELECTED, _sharedPrefs.getString(SELECTED, null));
        return init;
    }

    /*Return Selected Value*/
    public HashMap<String, String> getTitle() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(TITLE, _sharedPrefs.getString(TITLE, null));
        return init;
    }

    /*Return Country Value*/
    public HashMap<String, String> getCountry() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(COUNTRY, _sharedPrefs.getString(COUNTRY, null));
        return init;
    }

    /*Return Country Value*/
    public HashMap<String, String> getFlight() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(FLIGHT, _sharedPrefs.getString(FLIGHT, null));
        return init;
    }



    /*Return User_info*/
    public HashMap<String, String> getUserEmail() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USERNAME, _sharedPrefs.getString(USER_EMAIL, null));
        return init;
    }

    /*Set Username Value*/
    public void setUsername(String url) {
        _prefsEditor.putString(USERNAME, url);
        _prefsEditor.apply();
    }

    /*Set STATE value*/
    public void setState(String url) {
        _prefsEditor.putString(STATE, url);
        _prefsEditor.apply();
    }


    /*Set Username Value*/
    public void setUserInfo(String url) {
        _prefsEditor.putString(USER_INFO, url);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setBannerUrl(String url) {
        _prefsEditor.putString(DEFAULT_BANNER, url);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setPromoBannerUrl(String url) {
        _prefsEditor.putString(PROMO_BANNER, url);
        _prefsEditor.apply();
    }


    /*Set Signature Value*/
    public void setLoginStatus(String status) {
        _prefsEditor.putString(ISLOGIN, status);
        _prefsEditor.apply();
    }


    /*Set Signature Value*/
    public void setNewsletterStatus(String status) {
        _prefsEditor.putString(ISNEWSLETTER, status);
        _prefsEditor.apply();
    }
    /*Set Signature Value*/
    public void setSignatureToLocalStorage(String signature) {
        _prefsEditor.putString(SIGNATURE, signature);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setSelectedPopupSelection(String signature) {
        _prefsEditor.putString(SELECTED, signature);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setUserTitle(String title) {
        _prefsEditor.putString(TITLE, title);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setFlight(String flight) {
        _prefsEditor.putString(FLIGHT, flight);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setCountry(String country) {
        _prefsEditor.putString(COUNTRY, country);
        _prefsEditor.apply();
    }


    /*Set Userinfo Value*/
    public void setUserEmail(String url) {
        _prefsEditor.putString(USER_EMAIL, url);
        _prefsEditor.apply();
    }

    /*Clear Signature Value*/
    public void clearBannerURL() {
        // Clearing All URL
        _sharedPrefs.edit().remove(PROMO_BANNER).apply();
        _sharedPrefs.edit().remove(DEFAULT_BANNER).apply();

    }

    /*Clear Login Status*/
    public void clearLoginStatus() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(ISLOGIN).apply();
    }

    /*Clear Login Status*/
    public void clearNewsletterStatus() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(ISNEWSLETTER).apply();
    }

    /*Clear Signature Value*/
    public void clearSignatureFromLocalStorage() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(SIGNATURE).apply();
    }

    /*Clear Signature Value*/
    public void setUsername() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(USERNAME).apply();
    }

    public void setUserInfo() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(USER_INFO).apply();
    }

    /*Clear Selected Value*/
    public void clearSelectedPopupSelection() {
        // Clearing Selected
        _sharedPrefs.edit().remove(SELECTED).apply();
    }

    /*Clear Selected Value*/
    public void clearTitle() {
        // Clearing Selected
        _sharedPrefs.edit().remove(TITLE).apply();
    }

    /*Clear State Value*/
    public void clearState() {
        // Clearing Selected
        _sharedPrefs.edit().remove(STATE).apply();
    }

    /*Clear Flight Value*/
    public void clearFlight() {
        // Clearing Selected
        _sharedPrefs.edit().remove(FLIGHT).apply();
    }

    /*Clear Country Value*/
    public void clearCoutnry() {
        // Clearing Selected
        _sharedPrefs.edit().remove(COUNTRY).apply();
    }


    /*Clear UserInfo Value*/
    public void clearUserEmail() {
        // Clearing Selected
        _sharedPrefs.edit().remove(USER_EMAIL).apply();
    }
}