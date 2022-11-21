package com.example.wapp.demo.ui

import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SPUtils
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbActivity
import com.example.wapp.R
import com.example.wapp.databinding.ActivitySplashBinding
import com.example.wapp.demo.adapter.SplashBannerAdapter
import com.example.wapp.demo.bean.SplashBean
import com.example.wapp.demo.constant.Constant
import com.example.wapp.demo.ext.gone
import com.example.wapp.demo.ext.visible
import com.zhpan.bannerview.BannerViewPager



/**
 * Created by jsxiaoshui on 2021/8/17
 */
class SplashActivity : BaseVmDbActivity<BaseViewModel, ActivitySplashBinding>() {

    private val bannerAdapter by lazy {
        SplashBannerAdapter()
    }
    private lateinit var mViewPager: BannerViewPager<SplashBean, SplashBannerAdapter.BannerViewHolder>
    override fun createViewModel(): BaseViewModel {
        return ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    //https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
    override fun initView() {
        mDataBind.click = ProxyClick()
        mViewPager =
            findViewById<BannerViewPager<SplashBean, SplashBannerAdapter.BannerViewHolder>>(R.id.banner_view)
        var is_first_enter: Boolean =
            SPUtils.getInstance().getBoolean(Constant.IS_FIRST_ENTER, true)
        if (is_first_enter) {
            mDataBind.welcomeImage.gone()
            mViewPager?.apply {
                this.adapter = bannerAdapter
                this.setLifecycleRegistry(lifecycle)
                this.setIndicatorMargin(0, 0, 0, ConvertUtils.dp2px(28f))
                this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == 0 || position == 1) {
                            mDataBind.welcomeJoin.gone()
                        } else {
                            mDataBind.welcomeJoin.visible()
                        }
                    }
                })
            }.create(getSplashBeanList())
            SPUtils.getInstance().put(Constant.IS_FIRST_ENTER, false)
        } else {
            mDataBind.welcomeImage.visibility = View.VISIBLE
            mDataBind.welcomeJoin.visibility=View.GONE
            Handler().postDelayed({
                ProxyClick().toMain()
            }, 1000)

        }
    }

    override fun createObserver() {

    }

    override fun initData() {

    }

    inner class ProxyClick {
        fun toMain() {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getSplashBeanList(): MutableList<SplashBean> {
        val mesList = mutableListOf("在这里\n你可以听到周围人的心声", "在这里\nTA会在下一秒遇见你", "在这里\n不再错过可以改变你一生的人")
        val drawableList = getDrawableList()
        val beanList = mutableListOf<SplashBean>()
        for (index in mesList.indices) {
            beanList.add(SplashBean(drawableList[index], mesList[index]))
        }
        return beanList
    }

    /***
     * 获取drawableId列表
     */
    private fun getDrawableList(): MutableList<Int> {
        val mDrawableList = mutableListOf<Int>()
        for (i in 0..2) {
            val drawable = resources.getIdentifier("guide$i", "drawable", packageName)
            mDrawableList.add(drawable)
        }
        return mDrawableList
    }

}