package com.example.wapp.demo.ui.fragment


import com.example.oapp.base.BaseVmDbFragment
import com.example.wapp.R
import com.example.wapp.databinding.FragmentShareAriticleBinding
import com.example.wapp.demo.viewmodel.ArticleViewModel


/**
 * Data :2023/2/13
 * Time:16:07
 * Author:shuij
 *
 */
class AddArticleFragment:BaseVmDbFragment<ArticleViewModel, FragmentShareAriticleBinding>(){


    override fun layoutId(): Int {
       return R.layout.fragment_share_ariticle
    }

    override fun initView() {
        mDataBind.vm=mViewModel
//        toolbar.run {
//            initClose(titleStr = "分享文章",onBack = {
//                nav().navigateUp()
//            })
//            this.inflateMenu(R.menu.share_menu)
//            setOnMenuItemClickListener {
//                when(it.itemId){
//                    R.id.share_guize->{
//                        activity?.let {activity->
//                            MaterialDialog(activity,BottomSheet()).lifecycleOwner().show {
//                                this.title(text="温馨提示")
//                                this.customView(R.layout.customview,
//                                    scrollable = true,
//                                    horizontalPadding = true
//                                )
//                                this.positiveButton(text="知道了")
//                                this.cornerRadius(literalDp = 16f)
//                                this.getActionButton(WhichButton.POSITIVE).updateTextColor(
//                                    SettingUtil.getColor(activity)
//                                )
//                            }
//                        }
//                    }
//                }
//                return@setOnMenuItemClickListener true
//            }
//        }

    }
    override fun initData() {

    }
    override fun createObserver() {
    }
}