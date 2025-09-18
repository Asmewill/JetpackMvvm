package com.example.wapp.demo.ui.fragment

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentAddContactBinding
import com.example.wapp.demo.adapter.AddContackAdapter
import com.example.wapp.demo.ext.nav
import com.example.wapp.demo.viewmodel.ConversationViewModel

/**
 * Created by jsxiaoshui on 2021-11-25
 */
class AddContactFragment:BaseVmDbFragment<ConversationViewModel,FragmentAddContactBinding>() {

    private val conversationViewModel:ConversationViewModel by viewModels()
    var dataList= mutableListOf<String>()

    val addContackAdapter by lazy {
        AddContackAdapter(conversationViewModel)
    }
    override fun layoutId(): Int {
       return R.layout.fragment_add_contact
    }
    override fun initView() {
       mDataBind.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                   mDataBind.ivClear.visibility = View.VISIBLE

                } else {
                    mDataBind.ivClear.visibility = View.INVISIBLE
                    addContackAdapter.setList(mutableListOf())
                }
            }
        })

        mDataBind.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val search: String = mDataBind.etSearch.text.toString().trim()
                if (!TextUtils.isEmpty(search)) {
                    dataList.clear()
                    dataList.add(search)
                    addContackAdapter.setList(dataList)
                    ToastUtils.showShort("searchText:$search")
                }
                KeyboardUtils.hideSoftInput(mActivity)
                return@OnEditorActionListener true
            }
            false
        })

        mDataBind.ivClear.setOnClickListener(View.OnClickListener {
             mDataBind.etSearch.text.clear()
            addContackAdapter.setList(mutableListOf())
        })

       mDataBind.tvCancel.setOnClickListener(View.OnClickListener {
            KeyboardUtils.hideSoftInput(mActivity)
             nav().navigateUp()
          }
        )
        //recycleview设置
       mDataBind.rvList.layoutManager=LinearLayoutManager(mActivity)
       mDataBind.rvList.adapter=addContackAdapter

    }

    override fun initData() {

    }

    override fun createObserver() {
        conversationViewModel.addContactLiveData.observe(mActivity, Observer {
            if(it.isSuccess){
                ToastUtils.showShort("已发送好友邀请")
            }else{
                ToastUtils.showShort("发送好友邀请失败")
            }
        })
    }
}