package com.example.oapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.oapp.R
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbActivity
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.ActivitySettingBinding
import com.example.oapp.event.ThemeEvent
import com.example.oapp.ext.showSnackMsg
import com.example.oapp.utils.CacheDataUtil
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.EventViewModel

/**
 * Created by jsxiaoshui on 2021/7/27
 */
@Route(path = Constant.PagePath.SETTING)
class SettingActivity : BaseVmDbActivity<BaseViewModel, ActivitySettingBinding>(),
    View.OnClickListener, ColorChooserDialog.ColorCallback {
    override fun createViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        mDataBind.toolbar?.title = "设置"
        setSupportActionBar(mDataBind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //设置主题颜色
        initColor()
        mDataBind.ivThemeColor.setBackgroundColor(mThemeColor)
        mDataBind.cbNophoto.isChecked = SettingUtil.getIsNoPhotoMode()
        mDataBind.cbShowTop.isChecked = SettingUtil.getIsShowTopArticle()
        mDataBind.cbNavColor.isChecked = SettingUtil.getNavBar()

        mDataBind.llThemeColor.setOnClickListener(this)
        mDataBind.llClearCache.setOnClickListener(this)
        mDataBind.llVersion.setOnClickListener(this)
        mDataBind.llUpdateLog.setOnClickListener(this)
        mDataBind.llOriginCode.setOnClickListener(this)
        mDataBind.llCopyright.setOnClickListener(this)
    }


    override fun initData() {
     mDataBind.cbNophoto.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    SettingUtil.putNoPhotoMode(true)
                    EventViewModel.noPhotoLiveData.value = 1
                }

                else -> {
                    SettingUtil.putNoPhotoMode(false)
                    EventViewModel.noPhotoLiveData.value = 2
                }
            }
        }
        mDataBind.cbShowTop.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    SettingUtil.putShowTopArticle(true)
                    EventViewModel.showTopArticleLiveData.value = 1
                }

                else -> {
                    SettingUtil.putShowTopArticle(false)
                    EventViewModel.showTopArticleLiveData.value = 2
                }
            }
        }
       mDataBind.cbNavColor.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    SettingUtil.setNavBar(true)
                }

                else -> {
                    SettingUtil.setNavBar(false)
                }
            }
        }
    }

    override fun createObserver() {

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var temp: String? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_theme_color -> {
                ColorChooserDialog.Builder(this, R.string.choose_theme_color).show()
            }

            R.id.ll_clear_cache -> {
                temp!!.length;
                CacheDataUtil.clearAllCache(this)
                showSnackMsg(getString(R.string.clear_cache_successfully))
                mDataBind.tvCacheSize.setText(CacheDataUtil.getTotalCacheSize(this))
            }

            R.id.ll_version -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.changelog_url))
                    )
                )
            }

            R.id.ll_update_log -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.source_code_url))
                    )
                )
            }

            R.id.ll_origin_code -> {
                AlertDialog.Builder(this).setTitle("版权声明")
                    .setMessage(R.string.copyright_content).setCancelable(true).show()
            }

            R.id.ll_copyright -> {
                AlertDialog.Builder(this).setTitle("版权声明")
                    .setMessage(R.string.copyright_content).setCancelable(true).show()
            }
        }
    }

    override fun onColorSelection(p0: ColorChooserDialog, selectedColor: Int) {
        SettingUtil.setColor(selectedColor)
        initColor()
        mDataBind.ivThemeColor.setBackgroundColor(selectedColor)
        //通知其他界面更新
        EventViewModel.themeColorLiveData.value = ThemeEvent(selectedColor)

    }


    override fun onColorChooserDismissed(p0: ColorChooserDialog) {

    }
}