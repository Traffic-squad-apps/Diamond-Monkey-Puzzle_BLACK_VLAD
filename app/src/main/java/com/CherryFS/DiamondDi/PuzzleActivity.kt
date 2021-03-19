package com.CherryFS.DiamondDi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_puzzle.*

private var valueCallBack: ValueCallback<Array<Uri>>? = null
var faw3 = true

class PuzzleActivity : AppCompatActivity() {
    private lateinit var mainMeWeb: WebView
    var num = 0

    companion object{
        var TAG = "PuzzleActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        mainMeWeb = WebView(this)

        var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        puz.addView(mainMeWeb, layoutParams)

        var mainMeWeb = mainMeWeb
        mainMeWeb.settings.javaScriptEnabled = true
        mainMeWeb.settings.allowFileAccess = true
        mainMeWeb.settings.mixedContentMode = 0
        mainMeWeb.settings.setAppCacheEnabled(true)
        mainMeWeb.settings.allowFileAccessFromFileURLs = true
        mainMeWeb.settings.javaScriptCanOpenWindowsAutomatically = true
        mainMeWeb.settings.useWideViewPort = true
        mainMeWeb.settings.domStorageEnabled = true
        mainMeWeb.settings.databaseEnabled = true

        //Todo: Запуск вебвью
        var fullLink = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString("full_Link" ,"null")
        Log.e(TAG,"Start load: "+fullLink)
        mainMeWeb.loadUrl(fullLink.toString())

        mainMeWeb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("onPageFinished", ": $url")
                if (view!!.title!!.contains("404 Not Found")) {
                    Lo().ww("404 Not Found")
                    errorFun()
                }

                num++
                if (num == 2) {
                    Lo().bb("2: "+url)
                    Log.e("LINK-2", ""+url)
                }else if(num == 3){
                    Lo().bb("3: "+url)
                    Log.e("LINK-3", ""+url)
                }else if(num == 4){
                    Lo().bb("4: "+url)
                    Log.e("LINK-4", ""+url)
                }
                CookieManager.getInstance().flush()
            }
        }
        mainMeWeb.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(web: WebView?, v: ValueCallback<Array<Uri>>?, sad: FileChooserParams? ): Boolean {
                if (valueCallBack != null) {
                    valueCallBack!!.onReceiveValue(null)
                }
                return faw3
            }
        }
    }


    fun errorFun(){
        getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("404_Error", "true").apply()
        var i = Intent(this, BoardOptionsActivity::class.java)
        startActivity(i)
        finish()
    }


    override fun onBackPressed() {
        when (faw3) {
            mainMeWeb.canGoBack() -> mainMeWeb.goBack()
            else -> null
        }
    }

}