package me.hgj.jetpackmvvm.demo.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.tencent.bugly.beta.Beta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.util.StatusBarUtil
import me.hgj.jetpackmvvm.demo.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.MainViewModel
import me.hgj.jetpackmvvm.network.manager.NetState

/**
 * 项目主页Activity
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L
    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        testCoroutineInActivity()
        //进入首页检查更新
        Beta.checkUpgrade(false, true)

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
//                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment) {
//                    //如果当前界面不是主页，那么直接调用返回即可
//                    nav.navigateUp()
//                } else
//            }
//            }){
//            //是主页
//            if (System.currentTimeMillis() - exitTime > 2000) {
//                ToastUtils.showShort("再按一次退出程序")
//                exitTime = System.currentTimeMillis()
//            } else {
//                finish()
//            }
//        }
        appViewModel.appColor.value?.let {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0) }
    }

    fun testCoroutineInActivity() {
        CoroutineScope(Dispatchers.IO).launch() {
            println("Test001:查看运行的线程1："+Thread.currentThread().name)
            val start= System.currentTimeMillis()
            val result1=async {//运行在主线程，非异步
                println("Test001:查看运行的线程2："+Thread.currentThread().name)
                delay(1000)
                "123"
            }
            val result2=async(Dispatchers.IO) { //异步
                println("Test001:查看运行的线程3："+Thread.currentThread().name)
                delay(2000)
                "456"
            }
            val result3=result1.await()+result2.await();
            println("Test001:并发耗时："+ (System.currentTimeMillis() - start)+"||result3:"+result3)
            withContext(Dispatchers.IO){
                println("Test001:查看运行的线程4："+ Thread.currentThread().name)
                delay(1000)
            }
            println("Test001:查看运行的线程5："+ Thread.currentThread().name)
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observeInActivity(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "我特么怎么断网了!", Toast.LENGTH_SHORT).show()
        }
    }

}
