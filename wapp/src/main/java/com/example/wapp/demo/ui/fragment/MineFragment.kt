package com.example.wapp.demo.ui.fragment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentMineBinding
import com.example.wapp.demo.utils.CacheUtil
import com.example.wapp.demo.viewmodel.EventViewModel
import com.example.wapp.demo.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by jsxiaoshui on 2021/8/20
 *
 */
class MineFragment:BaseVmDbFragment<MineViewModel,FragmentMineBinding>() {

    override fun layoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        mDataBind.vm = mViewModel
        mDataBind.click = MyClick()
        if(CacheUtil.isLogin()){
            mViewModel.name.set(CacheUtil.getUser()?.username)
        }else{
            mViewModel.name.set("请先登录~")
        }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        if(CacheUtil.isLogin()){
            mViewModel.getPointAndRank()
        }

    }
    override fun initData() {
        me_swipe.setOnRefreshListener {
            me_swipe.isRefreshing=true
            mViewModel.getPointAndRank()
        }
    }

    override fun createObserver() {
        EventViewModel.userInfoLiveData.observeInFragment(this, Observer {
            if(it!=null){
                mViewModel.name.set(it.username)
                mViewModel.info.set("id:${it.id}　排名：--")
                mViewModel.getPointAndRank()
            }else{
                mViewModel.name.set("请先登录~")
                mViewModel.info.set("id:--　排名:--")
            }
        })
        mViewModel.pointLiveData.observe(mActivity, Observer {
            me_swipe.isRefreshing=false
            if(it.isSuccess){
                mViewModel.info.set("id:${it.listData[0].userId}　排名：${it.listData[0].rank}")
                mViewModel.point.set(it.listData[0].coinCount)
            }else{
                mViewModel.info.set("id:--　排名:--")
            }
        })
    }

    inner class MyClick(){
        fun myPonit(){
            if(CacheUtil.isLogin()){
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_MyPointFragment)
            }else{
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_LoginFragment)
            }
        }
        fun myCollect(){
            if(CacheUtil.isLogin()){
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_CollectFragment)
            }else{
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_LoginFragment)
            }
        }
        fun myArticle(){
            if(CacheUtil.isLogin()){
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_ArticleFragment)
            }else{
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_LoginFragment)
            }
        }
        fun goTodo(){
            if(CacheUtil.isLogin()){
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_ToDoFragment)
            }else{
                NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_LoginFragment)
            }
        }
        fun openSoureWebSite(){

        }
        fun joinUs(){
            joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
        }
        fun someDemo(){
            NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_DemoFragment)
        }
        fun systemSettings(){
            NavHostFragment.findNavController(this@MineFragment).navigate(R.id.action_Main_to_SettingFragment)
        }
    }

    /**
     * 加入qq聊天群
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
            false
        }
    }
}