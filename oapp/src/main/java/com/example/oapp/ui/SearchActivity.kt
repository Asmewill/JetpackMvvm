package com.example.oapp.ui

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.oapp.R
import com.example.oapp.adapter.SearchHistoryAdapter
import com.example.oapp.base.BaseActivity
import com.example.oapp.bean.HotBean
import com.example.oapp.bean.HttpResult
import com.example.oapp.bean.SearchHistoryBean
import com.example.oapp.constant.Constant
import com.example.oapp.ext.applySchdules
import com.example.oapp.http.ApiCallback
import com.example.oapp.http.HttpRetrofit
import com.example.oapp.http.OObserver
import com.example.oapp.utils.CommonUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal


/**
 * Created by jsxiaoshui on 2021/7/7
 */
@Route(path = Constant.PagePath.SEARCH)
class SearchActivity:BaseActivity() {
    lateinit var toolbar: Toolbar
    lateinit var hot_search_flow_layout: TagFlowLayout
    lateinit var rv_history_search: RecyclerView
    lateinit var search_history_clear_all_tv: TextView

    private val hotList: ArrayList<HotBean> by lazy {
        ArrayList<HotBean>()
    }
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter()
    }

    override fun attachLayoutRes(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        toolbar=findViewById<Toolbar>(R.id.toolbar)
        hot_search_flow_layout=findViewById<TagFlowLayout>(R.id.hot_search_flow_layout)
        rv_history_search=findViewById<RecyclerView>(R.id.rv_history_search)
        search_history_clear_all_tv=findViewById<TextView>(R.id.search_history_clear_all_tv)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        hot_search_flow_layout.setOnTagClickListener { p0, p1, p2 ->
            ARouter.getInstance().build(Constant.PagePath.COMMON)
                .withString(Constant.PAGE_TYPE, Constant.Type.SEARCH_TYPE_KEY)
                .withString(Constant.SEARCH_KEY, hotList[p1].name)
                .navigation()
            true
        }
        rv_history_search?.let {
            it.layoutManager=LinearLayoutManager(this@SearchActivity)
            it.adapter=searchHistoryAdapter
            searchHistoryAdapter.bindToRecyclerView(it)
        }
        searchHistoryAdapter.setEmptyView(R.layout.search_empty_view)
        search_history_clear_all_tv.setOnClickListener {
            deleteAll()
        }
        searchHistoryAdapter.setOnItemClickListener { _, _, position ->
             val item:SearchHistoryBean= searchHistoryAdapter.data[position]
            val intent = Intent(this@SearchActivity, CommonActivity::class.java)
            intent.putExtra(Constant.PAGE_TYPE, Constant.Type.SEARCH_TYPE_KEY)
            intent.putExtra(Constant.SEARCH_KEY, item.key)
            startActivity(intent)

        }
    }

    override fun initData() {
        getHotkey()
        queryHistoryKey()//查询数据库历史记录

    }

    private fun getHotkey() {
        HttpRetrofit.apiService.getHotkey().applySchdules().subscribe(OObserver(object:ApiCallback<HttpResult<List<HotBean>>>{
            override fun onSuccess(t: HttpResult<List<HotBean>>) {
                t.data?.let { hotList.addAll(it) }
                hot_search_flow_layout.adapter=object:TagAdapter<HotBean>(t.data){
                    override fun getView(p0: FlowLayout?, p1: Int, p2: HotBean?): View {
                        val textView:TextView= View.inflate(this@SearchActivity,R.layout.flow_layout_tv,null) as TextView
                        textView.text= t.data!!.get(p1).name
                        textView.setTextColor(CommonUtil.randomColor())
                        return textView
                    }
                }
            }
            override fun onFailture(t: Throwable) {

            }
        }))

    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchView:androidx.appcompat.widget.SearchView= menu?.findItem(R.id.action_search)?.actionView as androidx.appcompat.widget.SearchView
        searchView.maxWidth= Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint="搜索更多干货..."
        searchView.isSubmitButtonEnabled=true
        searchView.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(!TextUtils.isEmpty(p0)){
                    saveSearchKey(p0!!)
                    val intent=Intent(this@SearchActivity,CommonActivity::class.java)
                    intent.putExtra(Constant.PAGE_TYPE,Constant.Type.SEARCH_TYPE_KEY)
                    intent.putExtra(Constant.SEARCH_KEY,p0)
                    startActivity(intent)
                }
                return  false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        //使用反射获取mGoButton对象,并赋值
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun saveSearchKey(key:String){
        doAsync {
            val historyBean=SearchHistoryBean()
            historyBean.key=key.trim()
            val beanList=LitePal.where("key=\'"+key.trim()+"\'").find(SearchHistoryBean::class.java)
            if(beanList.size==0){
                historyBean.save()
            }else{
                LitePal.delete(SearchHistoryBean::class.java,beanList.get(0).id)
                historyBean.save()
            }
            uiThread {
                queryHistoryKey()
            }
        }


    }

    /***
     * 删除单个关键字
     */
    fun deleleHotKey(key:String){
        doAsync {
            val beanList=LitePal.where("key=\'"+key.trim()+"\'").find(SearchHistoryBean::class.java)
            if(beanList.size>0){
                LitePal.delete(SearchHistoryBean::class.java,beanList.get(0).id)
            }
            queryHistoryKey()
        }
    }

    private fun queryHistoryKey() {
        doAsync {
            val beanList=LitePal.findAll(SearchHistoryBean::class.java)
            beanList.reverse()
            uiThread {
                searchHistoryAdapter.setNewData(beanList)
            }
        }
    }

    fun deleteAll(){
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            queryHistoryKey()
        }
    }
}