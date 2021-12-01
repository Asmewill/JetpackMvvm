package com.example.wapp.demo.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentSquareBinding
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.ext.toHtml
import com.example.wapp.demo.viewmodel.SquareViewModel
import com.example.wapp.demo.widget.ScaleTransitionPagerTitleView
import kotlinx.android.synthetic.main.fragment_project.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * Created by jsxiaoshui on 2021/8/20
 */
class SquareFragment:BaseVmDbFragment<SquareViewModel,FragmentSquareBinding>() {
    private val commonNavigator by lazy {
        CommonNavigator(MyApp.instance)
    }
    private val tabList= arrayListOf("广场","每日一问","体系","导航")

    private val fragmentList= arrayListOf<Fragment>()
    init {
        fragmentList.add(PlazaFragment.newInstance())
        fragmentList.add(AskFragment.newInstance())
        fragmentList.add(SystemFragment.newInstance())
        fragmentList.add(NavigationFragment.newInstance())
    }

    override fun layoutId(): Int {
       return R.layout.fragment_square
    }

    override fun initView() {
        include_viewpager_toolbar.inflateMenu(R.menu.todo_menu)
        include_viewpager_toolbar.setOnMenuItemClickListener {
             when(it.itemId){
                 R.id.todo_add->{
                     val items= arrayListOf<String>("分享文章", "公众号")
                     activity?.let { activity ->
                         MaterialDialog(activity, BottomSheet())
                             .lifecycleOwner(viewLifecycleOwner).show {
                                 cornerRadius(8f)
                                 setPeekHeight(ConvertUtils.dp2px((items.size * 50 + 36).toFloat()))
                                 listItems(items = items) { _, index, item ->
                                     when (index) {
                                         0 -> {
                                             ToastUtils.showShort("分享文章")
                                         }
                                         1 -> {
                                             nav().navigate(R.id.action_Main_to_WechatFragment)
                                         }
                                     }
                                 }
                             }
                     }
                   true
                 }
                 else -> {
                     true
                 }

             }
        }
        view_pager.isUserInputEnabled=true//设置是否禁止用户滑动页面
        view_pager.adapter=object: FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return  fragmentList.size
            }
            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }


        commonNavigator.adapter=object : CommonNavigatorAdapter(){
            override fun getCount(): Int {
                return  tabList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    //设置文本
                    text = tabList[index].toHtml()
                    //字体大小
                    textSize = 17f
                    //未选中颜色
                    normalColor = Color.WHITE
                    //选中颜色
                    selectedColor = Color.WHITE
                    this.setOnClickListener {
                        view_pager.currentItem=index
                    }
                }
            }
            override fun getIndicator(context: Context?): IPagerIndicator {
                return  LinePagerIndicator(context).apply {
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
        magic_indicator.navigator=commonNavigator
        view_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                magic_indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                magic_indicator.onPageSelected(position)
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                magic_indicator.onPageScrollStateChanged(state)
            }
        })

    }

    override fun initData() {

    }

    override fun createObserver() {

    }
}