package com.example.wapp.demo.ext

import android.os.Build.VERSION_CODES.N
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.wapp.R
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.ui.fragment.*
import com.example.wapp.demo.widget.DefineLoadMoreView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
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
                    return MessageFragment()
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

fun SwipeRecyclerView.initFooter(loadMoreListener:SwipeRecyclerView.LoadMoreListener):DefineLoadMoreView{
     val footView=DefineLoadMoreView(MyApp.instance)
    //设置尾部点击
    footView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footView.onLoading()
        loadMoreListener.onLoadMore()
    })
     this.addFooterView(footView)
     this.setLoadMoreView(footView)
     this.setLoadMoreListener(loadMoreListener)
    return  footView
}

fun RecyclerView.initFloatBtn(floatBtn:FloatingActionButton){
    addOnScrollListener(object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatBtn.visibility = View.INVISIBLE
            }
        }
    })
    floatBtn.setOnClickListener {
        val layoutManager=layoutManager as LinearLayoutManager
        if(layoutManager.findLastVisibleItemPosition()>=40){
            scrollToPosition(0)//没有滚动动画，快速滚动到顶部
        }else{
            smoothScrollToPosition(0) //有滚动动画快速返回到顶部
        }
    }
}

fun SwipeRefreshLayout.init(onRefresh:()->Unit){
    this.setOnRefreshListener {
        onRefresh.invoke()
    }
}

fun Toolbar.initClose(
    titleStr:String="",
    backImg:Int= R.drawable.ic_back,
    onBack:(toolbar:Toolbar)->Unit
):Toolbar{
    title=titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener {
       onBack.invoke(this)
    }
    return this
}

fun String.toHtml(flag:Int=Html.FROM_HTML_MODE_LEGACY):Spanned{
  return if(android.os.Build.VERSION.SDK_INT>android.os.Build.VERSION_CODES.N){
        Html.fromHtml(this,flag)
    }else{
        Html.fromHtml(this)
    }
}

//普通RecycleView的绑定
fun  RecyclerView.init(
    layoutManager:RecyclerView.LayoutManager,
    bindAdapter:RecyclerView.Adapter<*>,
    isScroll:Boolean=false
):RecyclerView{
    this.layoutManager=layoutManager
    this.setHasFixedSize(true)
    this.adapter=bindAdapter
    this.isNestedScrollingEnabled=isScroll
    return this
}
fun BaseQuickAdapter<*,*>.setAdapterAnimation(mode:Int){
     if(mode==0){
       this.animationEnable=false
     }else{
         this.animationEnable=true
         this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode-1])
     }
}


/**
 * 将对象转为JSON字符串
 */

fun Any?.toJson():String{
    return Gson().toJson(this)
}


