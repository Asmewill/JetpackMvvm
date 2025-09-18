package com.example.oapp.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oapp.R
import com.example.oapp.base.BaseVmDbFragment
import com.example.oapp.bean.ToDoBean
import com.example.oapp.constant.Constant
import com.example.oapp.databinding.FragmentAddTodoBinding
import com.example.oapp.utils.CommonUtil
import com.example.oapp.utils.SettingUtil
import com.example.oapp.viewmodel.AddToDoViewModel
import com.example.oapp.viewmodel.EventViewModel
import java.util.Calendar


/**
 * Created by jsxiaoshui on 2021/7/28
 */
class AddToDoFragment : BaseVmDbFragment<AddToDoViewModel, FragmentAddTodoBinding>() {
    private var priority: Int = 0
    private var bean: ToDoBean.DatasBean? = null
    private var editType: Int = 0    //1，2,3分别表示新增，编辑，查看
    private val calendar by lazy {
        Calendar.getInstance()
    }

    companion object {
        fun getInstance(editType: Int, bean: ToDoBean.DatasBean?): AddToDoFragment {
            val instance = AddToDoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.EDIT_TYPE, editType)
            bundle.putSerializable(Constant.ITEM_BENA, bean)
            instance.arguments = bundle
            return instance
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_add_todo
    }

    override fun createViewModel(): AddToDoViewModel {
        return ViewModelProvider(this).get(AddToDoViewModel::class.java)
    }

    override fun initView() {
        editType = arguments?.getInt(Constant.EDIT_TYPE, 0)!!
        if (arguments?.getSerializable(Constant.ITEM_BENA) != null) {
            bean = arguments?.getSerializable(Constant.ITEM_BENA) as ToDoBean.DatasBean
        }

        mDataBind.tvDate.text = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(
            Calendar.MONDAY
        ) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH)
        mDataBind.llDate.setOnClickListener {
            val selectDate = mDataBind.tvDate.text
            val dialog = DatePickerDialog(
                activity as Context,
                { view, year, month, dayOfMonth ->
                    mDataBind.tvDate.setText(year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString())
                },
                selectDate.split("-")[0].toInt(),
                selectDate.split("-")[1].toInt() - 1,
                selectDate.split("-")[2].toInt()
            )
            dialog.show()
        }
        mDataBind.btnSave.setOnClickListener {
            val title = mDataBind.etTitle.text.toString()
            val content = mDataBind.etContent.text.toString()
            if (TextUtils.isEmpty(title)) {
                CommonUtil.showToast("请输入标题")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(content)) {
                CommonUtil.showToast("请输入详情内容")
                return@setOnClickListener
            }
            when (editType) {
                1 -> {
                    mViewModel.saveTodoPlane(
                        0,
                        title,
                        content,
                        priority.toString(),
                        mDataBind.tvDate.text.toString()
                    )

                }

                2 -> {
                    bean?.let {
                        mViewModel.editTodoPlane(
                            it.id,
                            0,
                            title,
                            content,
                            priority.toString(),
                            mDataBind.tvDate.text.toString()
                        )
                    }
                }
            }
        }
        mDataBind.rgPriority.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb0 -> {
                    priority = 0
                }

                R.id.rb1 -> {
                    priority = 1
                }
            }
        }
        when (editType) {
            2 -> {
                bean?.let {
                    mDataBind.etTitle.setText(it.title)
                    mDataBind.etContent.setText(it.content)
                    it.priority?.let {
                        when (it) {
                            0 -> {
                                mDataBind.rb0.isChecked = true
                            }

                            1 -> {
                                mDataBind.rb1.isChecked = true
                            }
                        }
                    }
                }
            }

            3 -> {
                mDataBind.etTitle.isEnabled = false
                mDataBind.etContent.isEnabled = false
                mDataBind.rb0.isClickable = false
                mDataBind.rb1.isClickable = false
                bean?.let {
                    mDataBind.etContent.setText(it?.content)
                    mDataBind.etTitle.setText(it?.title)


                }
                mDataBind.llDate.setOnClickListener(null)
                mDataBind.btnSave.visibility = View.GONE
            }
        }
    }

    override fun initData() {
        //设置主题色值
        val mThemeColor = if (!SettingUtil.getIsNightMode()) {
            SettingUtil.getColor()
        } else {
            resources.getColor(R.color.colorPrimary)
        }
        mDataBind.btnSave.setBackgroundColor(mThemeColor)

    }

    override fun createObserver() {
        mViewModel.addTodoLiveData.observe(this, Observer {
            if (it.dataBean?.errorCode == 0) {
                CommonUtil.showToast("新增成功")
                EventViewModel.freshLiveData.value = 1
                activity?.finish()
            } else {
                if (!TextUtils.isEmpty(it.dataBean?.errorMsg)) {
                    CommonUtil.showToast(it.dataBean?.errorMsg!!)
                }
            }

        })
        mViewModel.editTodoLiveData.observe(this, Observer {
            if (it.dataBean?.errorCode == 0) {
                CommonUtil.showToast("编辑成功")
                EventViewModel.freshLiveData.value = 1
                activity?.finish()
            } else {
                if (!TextUtils.isEmpty(it.dataBean?.errorMsg)) {
                    CommonUtil.showToast(it.dataBean?.errorMsg!!)
                }
            }
        })
    }
}