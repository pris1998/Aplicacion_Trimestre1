package com.example.repasobd.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.repasobd.R;
//Esta clase sirve para poder añadir las preferencias
// desde el xml creado anteriormente.
public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
