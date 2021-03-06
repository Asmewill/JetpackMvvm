package com.example.wapp.demo.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ConvertUtils
import com.example.oapp.base.BaseViewModel
import com.example.oapp.base.BaseVmDbActivity
import com.example.wapp.R
import com.example.wapp.databinding.ActivitySplashBinding
import com.example.wapp.demo.adapter.SplashBannerAdapter
import com.example.wapp.demo.bean.SplashBean
import com.example.wapp.demo.ext.gone
import com.example.wapp.demo.ext.visible
import com.zhpan.bannerview.BannerViewPager


/**
 * Created by jsxiaoshui on 2021/8/17
 */
class SplashActivity:BaseVmDbActivity<BaseViewModel,ActivitySplashBinding>() {

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
        mDataBind.click=ProxyClick()
        mViewPager=findViewById<BannerViewPager<SplashBean,SplashBannerAdapter.BannerViewHolder>>(R.id.banner_view)
        mDataBind.welcomeImage.gone()
        mViewPager?.apply {
            this.adapter=bannerAdapter
            this.setLifecycleRegistry(lifecycle)
            this.setIndicatorMargin(0,0,0,ConvertUtils.dp2px(28f))
            this.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if(position==0||position==1){
                        mDataBind.welcomeJoin.gone()
                    }else{
                        mDataBind.welcomeJoin.visible()
                    }
                }
            })
        }.create(getSplashBeanList())
    }

    override fun createObserver() {

    }

    override fun initData() {

    }
    inner class ProxyClick{
        fun toMain(){
            val intent=Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun getSplashBeanList():MutableList<SplashBean>{
        val mesList= mutableListOf("?????????\n?????????????????????????????????", "?????????\nTA????????????????????????", "?????????\n???????????????????????????????????????")
        val drawableList=getDrawableList()
        val beanList= mutableListOf<SplashBean>()
        for(index in mesList.indices){
            beanList.add(SplashBean(drawableList[index], mesList[index]))
        }
        return beanList
    }

    /***
     * ??????drawableId??????
     */
    private fun getDrawableList():MutableList<Int> {
        val mDrawableList= mutableListOf<Int>()
        for (i in 0..2) {
            val drawable = resources.getIdentifier("guide$i", "drawable", packageName)
            mDrawableList.add(drawable)
        }
        return mDrawableList
    }

}