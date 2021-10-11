package com.example.wapp.demo.ext

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.wapp.demo.ui.fragment.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Created by jsxiaoshui on 2021/8/18
 */
fun View.gone(){
    this.visibility=View.GONE
}
fun View.visible(){
    this.visibility=View.VISIBLE
}
fun ViewPager2.initMain(fragment:Fragment):ViewPager2{
    this.isUserInputEnabled=false
    this.offscreenPageLimit=5
    this.adapter=object :FragmentStateAdapter(fragment){
        override fun getItemCount(): Int {
            return 5
        }
        override fun createFragment(position: Int): Fragment {
            when(position){
                0->{
                    return HomeFragment()
                }
                1->{
                    return ProjectFragment()
                }
                2->{
                    return SquareFragment()
                }
                3->{
                    return WechatFragment()
                }
                4->{
                    return MineFragment()
                }
                else->{
                    return HomeFragment()
                }
            }
        }
    }
    this.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){

    })
   return this
}
fun BottomNavigationViewEx.init(itemSelectAction:(Int)->Unit):BottomNavigationViewEx{
    this.enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    this.setTextSize(12f);
    this.setOnNavigationItemSelectedListener {
        itemSelectAction.invoke(it.itemId)
        true
    }
    return this
}

fun Toolbar.init(titleStr:String): Toolbar {
    this.title=titleStr
    return this
}
fun Fragment.nav():NavController{
    return NavHostFragment.findNavController(this)
}

//recycleView的扩展函数
fun SwipeRecyclerView.init(
    layoutmanager:RecyclerView.LayoutManager,
    bindAdapter:RecyclerView.Adapter<*>,
    isScroll: Boolean=true
):SwipeRecyclerView{
    layoutManager=layoutmanager
    adapter=bindAdapter
    setHasFixedSize(true)
    isNestedScrollingEnabled=isScroll
    return  this
}