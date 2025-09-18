package com.example.wapp.demo.ui.fragment

import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSettingBinding
import com.example.wapp.demo.ext.initClose
import com.example.wapp.demo.http.NetworkApi
import com.example.wapp.demo.navigation.NavHostFragment
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.EventViewModel
import com.example.wapp.demo.viewmodel.SettingViewModel

/**
 * Created by jsxiaoshui on 2021-11-15
 */
class SettingFragment:BaseVmDbFragment<SettingViewModel,FragmentSettingBinding>(),View.OnClickListener{


    override fun layoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        mDataBind.toolbar.initClose(titleStr = "设置",onBack={
            NavHostFragment.findNavController(this).navigateUp()
        })
        mDataBind.llIsShowTop.setOnClickListener(this)
        mDataBind.llClearCache.setOnClickListener(this)
        mDataBind.llExitLogin.setOnClickListener(this)
        mDataBind.llListAnimation.setOnClickListener(this)
        mDataBind.llThemeColor.setOnClickListener(this)
        mDataBind.llVersion.setOnClickListener(this)
        mDataBind.llAuthor.setOnClickListener(this)
        mDataBind.llOriginCode.setOnClickListener(this)
        mDataBind.llCopyright.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun createObserver() {
        mViewModel.loginOutHxLiveData.observe(mActivity, androidx.lifecycle.Observer {
            if(it.isSuccess){
              ToastUtils.showShort("环信退出成功")
            }else{
              ToastUtils.showShort("环信退出失败："+it.errorMsg)
            }
        })
    }

    override fun onClick(v: View) {
       when(v.id){
           R.id.ll_isShowTop->{
               ToastUtils.showLong("是否显示置顶文章")
           }
           R.id.ll_clear_cache->{
               ToastUtils.showLong("清除缓存")
           }
           R.id.ll_exit_login->{
               activity?.let {
                   MaterialDialog(it)
                       .cancelable(true)
                       .lifecycleOwner(viewLifecycleOwner)
                       .show {
                          this.title(text = "退出")
                          this.message(text = "确定退出登录吗?")
                          this.positiveButton(text = "确定") {
                              //清除cookie
                              NetworkApi.instance.cookieJar.clear()
                              CacheUtil.setUser(null)
                              EventViewModel.userInfoLiveData.value = null
                              mViewModel.loginOutHx(true)
                              NavHostFragment.findNavController(this@SettingFragment).navigateUp()
                          }
                          this.negativeButton(text = "取消") {

                          }
                       }
               }
           }
           R.id.ll_list_animation->{
               ToastUtils.showLong("列表动画")
           }
           R.id.ll_theme_color->{
               ToastUtils.showLong("主题颜色")
           }
           R.id.ll_version->{
               ToastUtils.showLong("版本")
           }
           R.id.ll_author->{
               ToastUtils.showLong("作者")
           }
           R.id.ll_origin_code->{
               ToastUtils.showLong("源代码")
           }
           R.id.ll_copyright->{
               ToastUtils.showLong("版权声明")
           }
       }
    }
}