package com.example.wapp.demo.adapter

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wapp.R
import com.example.wapp.demo.bean.AriticleResponse
import com.example.wapp.demo.widget.CollectView

/**
 * Created by jsxiaoshui on 2021-10-11
 */
class AriticleAdapter(data: MutableList<AriticleResponse>) :
    BaseDelegateMultiAdapter<AriticleResponse, BaseViewHolder>(data) {
    private var showTag = false

    constructor(data: MutableList<AriticleResponse>, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        //第一步---设置ItemType
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<AriticleResponse>() {
            override fun getItemType(data: List<AriticleResponse>, position: Int): Int {
                if (TextUtils.isEmpty(data.get(position).envelopePic)) {
                    return 1
                } else {
                    return 2
                }
            }
        })
        //第二步 绑定item类型
        getMultiTypeDelegate()?.addItemType(1, R.layout.item_ariticle)
        getMultiTypeDelegate()?.addItemType(2, R.layout.item_project)

    }

    override fun convert(holder: BaseViewHolder, item: AriticleResponse) {
        when (holder.itemViewType) {
            1 -> {//文章
                val item_home_author = holder.getView<TextView>(R.id.item_home_author)
                val item_home_top = holder.getView<TextView>(R.id.item_home_top)
                val item_home_new = holder.getView<TextView>(R.id.item_home_new)
                val item_home_type1 = holder.getView<TextView>(R.id.item_home_type1)
                val item_home_date = holder.getView<TextView>(R.id.item_home_date)
                val item_home_content = holder.getView<TextView>(R.id.item_home_content)
                val item_home_type2 = holder.getView<TextView>(R.id.item_home_type2)
                val item_home_collect = holder.getView<CollectView>(R.id.item_home_collect)
                if (!TextUtils.isEmpty(item.author)) {
                    item_home_author.text = item.author
                } else {
                    item_home_author.text = item.shareUser
                }
                if (item.isTop) {
                    item_home_top.visibility = View.VISIBLE
                    item_home_new.visibility = View.VISIBLE
                } else {
                    item_home_top.visibility = View.GONE
                    item_home_new.visibility = View.GONE
                }
                if (item.tags.isNotEmpty()) {
                    item_home_type1.visibility = View.VISIBLE
                    item_home_type1.text = item.tags[0].name
                } else {
                    item_home_type1.visibility = View.GONE
                }
                item_home_date.text = item.niceDate
                item_home_content.text = Html.fromHtml(item.title)
                item_home_type2.text = item.superChapterName


            }
            2 -> {
                val item_project_author = holder.getView<TextView>(R.id.item_project_author)
                val item_project_top = holder.getView<TextView>(R.id.item_project_top)
                val item_project_new = holder.getView<TextView>(R.id.item_project_new)
                val item_project_type1 = holder.getView<TextView>(R.id.item_project_type1)
                val item_project_imageview = holder.getView<ImageView>(R.id.item_project_imageview)
                val item_project_title = holder.getView<TextView>(R.id.item_project_title)
                val item_project_content = holder.getView<TextView>(R.id.item_project_content)
                val item_project_type = holder.getView<TextView>(R.id.item_project_type)
                val item_project_date=holder.getView<TextView>(R.id.item_project_date)
                val item_project_collect = holder.getView<CollectView>(R.id.item_project_collect)

                if (!TextUtils.isEmpty(item.author)) {
                    item_project_author.text = item.author
                } else {
                    item_project_author.text = item.shareUser
                }

                if (item.isTop) {
                    item_project_top.visibility = View.VISIBLE
                    item_project_new.visibility = View.VISIBLE
                } else {
                    item_project_top.visibility = View.GONE
                    item_project_new.visibility = View.GONE
                }
                if (item.tags.isNotEmpty()) {
                    item_project_type1.text = item.tags[0].name
                } else {
                    item_project_type1.visibility = View.GONE
                }

                if (item.envelopePic.isNotEmpty()) {
                    Glide.with(context).load(item.envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(item_project_imageview)
                }
                item_project_title.text = item.title
                item_project_content.text=item.desc
                item_project_type.text=item.superChapterName
                item_project_date.text=item.niceDate
            }
        }
    }
}