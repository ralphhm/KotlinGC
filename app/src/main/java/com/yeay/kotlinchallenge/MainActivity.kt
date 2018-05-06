package com.yeay.kotlinchallenge

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import de.rhm.kotlingc.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    lateinit var prefs: SharedPreferences

    private val prefListenerImpl = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> log("prefs changed: interface implementation called") }

    // will not be called after GC
    private val prefFun: (SharedPreferences, String) -> Unit = { _, _ -> log("prefs changed: function called")}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        prefs.registerOnSharedPreferenceChangeListener(prefListenerImpl)
        prefs.registerOnSharedPreferenceChangeListener(prefFun)

        button_prefs.setOnClickListener {
            log("changing pref")
            prefs.edit().putBoolean("somePref", !prefs.getBoolean("somePref", false)).apply()
        }
        button_gc.setOnClickListener {
            log("running GC")
            Runtime.getRuntime().gc()
        }
    }

    override fun onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(prefFun)
        prefs.unregisterOnSharedPreferenceChangeListener(prefListenerImpl)
        super.onDestroy()
    }

    private fun log(message: String) {
        text_log?.apply { text = "$text$message\n" }
    }
}