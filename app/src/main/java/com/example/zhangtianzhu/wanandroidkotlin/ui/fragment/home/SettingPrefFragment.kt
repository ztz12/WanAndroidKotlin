package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatDelegate
import android.view.View
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.LoadPhoto
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.LoadTopArticle
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.NightModelEvent
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.SettingActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.*
import com.example.zhangtianzhu.wanandroidkotlin.widget.IconPreference
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SettingPrefFragment :PreferenceFragment(),SharedPreferences.OnSharedPreferenceChangeListener{

    private var context:SettingActivity?=null

    private lateinit var colorView:IconPreference
    companion object {
        fun getInstance():SettingPrefFragment{
            return SettingPrefFragment()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_setting)
        setHasOptionsMenu(true)
        context = activity as SettingActivity
        colorView = findPreference("color") as IconPreference
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultText()
        findPreference("setting_night").setOnPreferenceChangeListener { preference, any ->
            if(ConfigureUtils.getIsNightMode()){
                ConfigureUtils.setIsNightMode(false)
                RxBus.default.post(NightModelEvent(false))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                activity.window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                activity.recreate()
            }else{
                ConfigureUtils.setIsNightMode(true)
                RxBus.default.post(NightModelEvent(true))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                activity.window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                activity.recreate()
            }
            true
        }

        findPreference("setting_NoPhoto").setOnPreferenceChangeListener { preference, any ->
            if(ConfigureUtils.getIsNoPhotoMode()){
                ConfigureUtils.setIsNoPhotoModel(false)
                RxBus.default.post(LoadPhoto(false))
            }else{
                ConfigureUtils.setIsNoPhotoModel(true)
                RxBus.default.post(LoadPhoto(true))
            }
            true
        }

        findPreference("setting_show_top").setOnPreferenceChangeListener { preference, any ->
            Observable.timer(100,TimeUnit.MILLISECONDS)
                    .compose(SchedulerUtils.toMain())
                    .subscribe {
                        RxBus.default.post(LoadTopArticle(true))
                    }
            true
        }

        findPreference("color").setOnPreferenceClickListener {
            ColorChooserDialog.Builder(context!!, R.string.choose_theme_color)
                    .backButton(R.string.back)
                    .cancelButton(R.string.cancel)
                    .doneButton(R.string.done)
                    .customButton(R.string.custom)
                    .presetsButton(R.string.back)
                    .allowUserColorInputAlpha(false)
                    .show()
            true
        }

        findPreference("clearCache").setOnPreferenceClickListener {
            CacheDataUtil.clearAllCache(context!!)
            DialogUtil.showSnackBar(context!!,getString(R.string.clear_success))
            setDefaultText()
            true
        }

       try {
           val version = "当前版本:"+context?.packageManager?.getPackageInfo(context?.packageName,0)?.versionName
           findPreference("version").summary = version
       }catch (e:Exception){
           e.printStackTrace()
       }
    }

    private fun setDefaultText(){
        try {
            findPreference("clearCache").summary = CacheDataUtil.getTotalCacheSize(context!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        p1 ?: return

        if(p1=="color"){
            colorView.setView()
        }
    }
}