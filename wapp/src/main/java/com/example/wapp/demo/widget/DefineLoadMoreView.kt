package com.example.wapp.demo.widget

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.example.wapp.R

import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Created by jsxiaoshui on 2021-10-12
 * 自定义LoadMoreView
 */
class DefineLoadMoreView(context:Context):LinearLayout(context),SwipeRecyclerView.LoadMoreView, View.OnClickListener{
    val mProgressBar:ProgressBar
    private var mLoadMoreListener :SwipeRecyclerView.LoadMoreListener?=null

     fun setmLoadMoreListener(mLoadMoreListener :SwipeRecyclerView.LoadMoreListener){
         this.mLoadMoreListener=mLoadMoreListener
    }


    private val mTvMessage:TextView
    init {
        layoutParams=ViewGroup.LayoutParams(-1,-2)
        gravity=Gravity.CENTER
        visibility=View.GONE
        val minHeight=ConvertUtils.dp2px(36f)
        minimumHeight=minHeight
        View.inflate(context, R.layout.layout_fotter_loadmore,this)
        mProgressBar=findViewById(R.id.loading_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
           // mProgressBar.indeterminateTintList = SettingUtil.getOneColorStateList(context)
        }
        mTvMessage=findViewById(R.id.tv_message)
        setOnClickListener(this)
    }


    override fun onLoading() {
       visibility=View.VISIBLE
        mProgressBar.visibility=View.VISIBLE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "正在努力加载，请稍后"
    }

    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        if(!hasMore){
            visibility=View.VISIBLE
            if(dataEmpty){
                mProgressBar.visibility=View.GONE
                mTvMessage.visibility=View.VISIBLE
                mTvMessage.text="暂时没有数据"
            }else{
                mProgressBar.visibility=View.GONE
                mTvMessage.visibility=View.VISIBLE
                mTvMessage.text="没有更多数据啦"
            }
        }else{
            visibility=View.INVISIBLE
        }

    }

    override fun onWaitToLoadMore(loadMoreListener: SwipeRecyclerView.LoadMoreListener) {
        this.mLoadMoreListener=loadMoreListener
        visibility=View.VISIBLE
        mProgressBar.visibility=View.GONE
        mTvMessage.visibility=View.VISIBLE
        mTvMessage.text="点我加载更多"
    }

    /**
     * 加载出错了
     * 错误码和错误信息二选一
     */
    override fun onLoadError(errorCode: Int, errorMessage: String?) {
        visibility=View.VISIBLE
        mProgressBar.visibility=View.GONE
        mTvMessage.visibility=View.VISIBLE
        mTvMessage.text=errorMessage
    }

    override fun onClick(v: View?) {
         mLoadMoreListener?.let {
             if(!mTvMessage.text.equals("没有更多数据啦")&&mProgressBar.visibility!=View.VISIBLE){
                 it.onLoadMore()
             }
         }
    }
}