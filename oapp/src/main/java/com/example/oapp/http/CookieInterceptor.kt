package com.example.oapp.http

import com.example.oapp.MyApp
import com.example.oapp.constant.HttpConstant
import com.example.oapp.utils.SharedPreUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by jsxiaoshui on 2021/7/9
 * 保存cookie
 */
class CookieInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request()
        val response=chain.proceed(request)
        val requestUrl=chain.request().url().toString()
        if((requestUrl.contains(HttpConstant.SAVE_USER_LOGIN_KEY)
          ||requestUrl.contains(HttpConstant.SAVE_USER_REGISTER_KEY))
            &&!response.headers(HttpConstant.SET_COOKIE_KEY).isEmpty()){
            val cookieList=response.headers(HttpConstant.SET_COOKIE_KEY)
            val cookieStr= filterListCookie(cookieList)
            SharedPreUtil.saveString(MyApp.instance,HttpConstant.COOKIE_KEY,cookieStr)
        }
        return  response
    }


    private fun  filterListCookie(list:List<String>): String {
        val hashSet=HashSet<String>()////去除重复
        list.forEach {
            it.split(";").forEach {
                hashSet.add(it)
            }
        }
        val cookieStr=StringBuffer()
        hashSet.forEach {
            if(it.contains("=")){
                cookieStr.append(it)
                cookieStr.append(";")//添加;
            }
        }
        return cookieStr.toString()
    }
}



