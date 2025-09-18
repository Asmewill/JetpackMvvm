package com.example.wapp.demo.ui.fragment

import android.content.Context
import android.graphics.Color
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentWechatBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.ext.toHtml
import com.example.wapp.demo.viewmodel.WechatViewModel
import com.example.wapp.demo.widget.ScaleTransitionPagerTitleView
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.example.wapp.demo.loadcallback.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class WechatFragment : BaseVmDbFragment<WechatViewModel, FragmentWechatBinding>() {
    private val commonNavigator by lazy {
        CommonNavigator(MyApp.instance)
    }
    private var fragments = arrayListOf<Fragment>()
    private var mDataList = arrayListOf<String>()

    override fun layoutId(): Int {
        return R.layout.fragment_wechat
    }

    override fun initView() {
        //注册LoadingService
        loadService = LoadSir.getDefault().register(mDataBind.llContentWechat) {
            loadService.showCallback(LoadingCallback::class.java)
            mViewModel.getPublicTitle()
        }

        mDataBind.viewPager.isUserInputEnabled = true//设置是否禁止用户滑动页面
        mDataBind.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    //设置文本
                    text = mDataList[index].toHtml()
                    //字体大小
                    textSize = 17f
                    //未选中颜色
                    normalColor = Color.WHITE
                    //选中颜色
                    selectedColor = Color.WHITE
                    this.setOnClickListener {
                        mDataBind.viewPager.currentItem = index
                    }
                }
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    //线条的宽高度
                    lineHeight = UIUtil.dip2px(MyApp.instance, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(MyApp.instance, 30.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(MyApp.instance, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    //线条的颜色
                    setColors(Color.WHITE)
                }
            }
        }
        mDataBind.magicIndicator.navigator = commonNavigator
        mDataBind.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                mDataBind.magicIndicator.onPageScrolled(
                    position,
                    positionOffset,
                    positionOffsetPixels
                )
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mDataBind.magicIndicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                mDataBind.magicIndicator.onPageScrollStateChanged(state)
            }
        })

    }

    override fun initData() {

    }

    override fun lazyLoad() {
        super.lazyLoad()
        loadService.showCallback(LoadingCallback::class.java)
        mViewModel.getPublicTitle()

    }

    override fun createObserver() {
        mViewModel.publicLiveData.observe(mActivity, Observer {
            if (it.isSuccess) {
                mDataList.clear()
                fragments.clear()
                it.listData.forEach { classifyResponse ->
                    mDataList.add(classifyResponse.name)
                    fragments.add(WechatChildFragment.newInstance(classifyResponse.id))
                }
                mDataBind.magicIndicator.navigator.notifyDataSetChanged()
                mDataBind.viewPager.adapter?.notifyDataSetChanged()
                mDataBind.viewPager.offscreenPageLimit = fragments.size
                loadService.showCallback(SuccessCallback::class.java)
            } else {
                loadService.showCallback(ErrorCallback::class.java)
            }
        })
    }

}