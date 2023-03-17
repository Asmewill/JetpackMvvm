package com.example.oapp.ui

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.base.BaseActivity
import com.example.oapp.bean.ToDoBean
import com.example.oapp.constant.Constant
import com.example.oapp.ui.fragment.AboutUsFragment
import com.example.oapp.ui.fragment.AddToDoFragment
import com.example.oapp.ui.fragment.CollectFragment
import com.example.oapp.ui.fragment.SearchListFragment
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by jsxiaoshui on 2021/7/8
 */
@Route(path = Constant.PagePath.COMMON)
class CommonActivity :BaseActivity() {
    //kotlin中使用Arouter注解传递参数
    @Autowired
    @JvmField var page_type: String? = null

    private var fragment: Fragment?=null
    //kotlin中使用Arouter注解传递参数
    @Autowired
    @JvmField  var search_key: String?=""
    @Autowired
    @JvmField var item_bean:ToDoBean.DatasBean?=null
    private var type=""
    private var title=""

    override fun attachLayoutRes(): Int {
        return R.layout.activity_common
    }
    override fun initView() {
        ARouter.getInstance().inject(this)// Start auto inject.
    }
    override fun initData() {
        when(page_type){
            Constant.Type.SEARCH_TYPE_KEY->{
                toolbar.title=search_key
                fragment= SearchListFragment.getInstance(search_key)
            }
            Constant.Type.COLLECT_TYPE_KEY->{
                toolbar.title="收藏"
                fragment=CollectFragment.getInstance()
            }
            Constant.Type.ABOUT_US_TYPE_KEY->{
                toolbar.title="关于我们"
                fragment=AboutUsFragment.getInstance()
            }
            Constant.Type.ADD_TODO_TYPE_KEY->{
                toolbar.title="新增"
                fragment=AddToDoFragment.getInstance(1,null)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY->{
                toolbar.title="编辑"
                fragment=AddToDoFragment.getInstance(2,
                    item_bean
                )
            }
            Constant.Type.SEE_TODO_TYPE_KEY->{
                toolbar.title="查看"
                fragment=AddToDoFragment.getInstance(3,
                    item_bean)
            }
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.common_frame_layout,it).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
           android.R.id.home->{
               finish()
           }
        }
        return super.onOptionsItemSelected(item)
    }
}
