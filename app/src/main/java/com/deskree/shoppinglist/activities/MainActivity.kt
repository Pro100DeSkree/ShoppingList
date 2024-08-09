package com.deskree.shoppinglist.activities
// Відео про SplashScreen: https://www.youtube.com/watch?v=rIHArmoq9f8
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.billing.BillingManager
import com.deskree.shoppinglist.fragments.FragmentManager
import com.deskree.shoppinglist.databinding.ActivityMainBinding
import com.deskree.shoppinglist.dialogs.NewListDialog
import com.deskree.shoppinglist.fragments.NoteFragment
import com.deskree.shoppinglist.fragments.ShopListNamesFragment
import com.deskree.shoppinglist.settings.SettingsActivity
import com.deskree.shoppinglist.utils.TimeManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    private lateinit var pref: SharedPreferences
    private lateinit var defPref: SharedPreferences

    // ADS
    private lateinit var mAdView: AdView
    private var iAd: InterstitialAd? = null
    private var adShowTime = 0
    private var adShowTimeMin = 180
    private var adShowCounter = 3
    private var adShowCounterMax = 2 - 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ShoppingList)

        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defStartFrag = defPref.getString(getString(R.string.default_start_frag_key), "Lists")
        if (defStartFrag == "Notes") {
            currentMenuItemId = R.id.notes
        }

        setBottomNavListener()
        // Ініціалізація реклами
        // TODO: Ініціалізація реклами ВИМКНЕНА
//        if(!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) {
//            loadInterAd()
//            // Ініціалізація Банера
//            mAdView = binding.adView
//            mAdView.visibility = View.VISIBLE
//            MobileAds.initialize(this) {}
//            val adRequest = AdRequest.Builder().build()
//            mAdView.loadAd(adRequest)
//        }
    }

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(ad: LoadAdError) {
                    iAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (iAd != null && (adShowCounter >= adShowCounterMax || checkShowTimeAd() && !pref.getBoolean(
                BillingManager.REMOVE_ADS_KEY,
                false
            ))
        ) {
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            adShowTime = TimeManager.getCurrentTimeAd()
            adShowCounter = 0
            iAd?.show(this)
        } else {
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun checkShowTimeAd(): Boolean {
        val time = TimeManager.getCurrentTimeAd() - adShowTime
        return (time > adShowTimeMin)
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                    supportActionBar?.title = "Списки"
                }

                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }

                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                    supportActionBar?.title = "Нотатки"
                }

                R.id.settings -> {
//                    showInterAd(
//                        object: AdListener{
//                            override fun onFinish() {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//                            }
//                        }
//                    )
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
    }

    override fun onStartDi() {

    }

    override fun onClick(name: String) {
    }

    override fun onDismiss() {
    }

    interface AdListener {
        fun onFinish()
    }
}