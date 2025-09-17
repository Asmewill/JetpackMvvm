package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.data.model.bean.SystemResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentSystemBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.TreeViewModel
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class SystemArrFragment : BaseFragment<TreeViewModel, FragmentSystemBinding>() {

    lateinit var data: SystemResponse

    var index = 0

    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun layoutId() = R.layout.fragment_system

    override fun initView(savedInstanceState: Bundle?)  {
        arguments?.let {
            data = (it.getSerializable("data") as SystemResponse?)!!
            index = it.getInt("index")
        }
        mDatabind.toolbar.initClose(data.name) {
            nav().navigateUp()
        }
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let {  mDatabind.viewpagerLinear.setBackgroundColor(it) }
        //设置栏目标题居左显示
        (mDatabind.magicIndicator.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.LEFT

    }

    override fun lazyLoadData() {
        data.children.forEach {
            fragments.add(SystemChildFragment.newInstance(it.id))
        }
        //初始化viewpager2
         mDatabind.viewPager.init(this, fragments)
        //初始化 magic_indicator
        mDatabind.magicIndicator.bindViewPager2(  mDatabind.viewPager, data.children.map { it.name })

        mDatabind.viewPager.offscreenPageLimit = fragments.size

        mDatabind.viewPager.postDelayed({
            mDatabind.viewPager.currentItem = index
        },100)

    }

    override fun createObserver() {

    }

}