package com.example.oapp.ui.fragment

import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.lifecycle.ViewModelProvider
import com.example.oapp.R
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.databinding.FragmentAboutBinding
import com.example.oapp.databinding.FragmentCollectBinding
import com.example.oapp.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_about.*
import org.jetbrains.anko.backgroundColor

/**
 * Created by jsxiaoshui on 2021/7/27
 */
class AboutUsFragment: BaseVmDbFragment<BaseViewModel, FragmentAboutBinding>() {
    private var versionStr: String?=null

    companion object{
        fun getInstance():AboutUsFragment{
            val instance=AboutUsFragment()
            return instance
        }
    }
    override fun layoutId(): Int {
        return R.layout.fragment_about
    }

    override fun createViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    override fun initView() {
        var text= Html.fromHtml(getString(R.string.about_content))
        about_content?.setText(text)
        about_content?.movementMethod= LinkMovementMethod.getInstance()//响应超链接
        versionStr=getString(R.string.app_name)+" V"+activity?.packageManager?.getPackageInfo(activity?.packageName!!,0)?.versionName
        about_version?.text=versionStr
        iv_logo.backgroundColor=SettingUtil.getColor()
    }

    override fun createObserver() {

    }

    override fun initData() {

    }
}