package com.deskree.shoppinglist.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.billing.BillingManager

class SettingsFragment: PreferenceFragmentCompat() {
    private lateinit var removeAsdPref: Preference
    private lateinit var bManager: BillingManager
    private lateinit var defPref: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        defPref = PreferenceManager.getDefaultSharedPreferences(requireActivity())
//        init()

    }

//    private fun init(){
//        bManager = BillingManager(activity as AppCompatActivity)
//        removeAsdPref = findPreference("remove_ads_key")!!
//        if(defPref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)){
//            removeAsdPref.isEnabled = false
//            }else {
//            removeAsdPref.setOnPreferenceClickListener {
//                bManager.startConnection()
//                true
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        bManager.closeConnection()
//        super.onDestroy()
//    }
}