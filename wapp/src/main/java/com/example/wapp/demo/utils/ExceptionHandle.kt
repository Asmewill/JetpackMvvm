package com.example.wapp.demo.utils

import android.util.MalformedJsonException
import com.example.wapp.demo.bean.exception.AppException
import com.example.wapp.demo.bean.exception.Error
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

/**
 * Created by jsxiaoshui on 2021-10-13
 */
object ExceptionHandle {

    fun handleException(e:Throwable):AppException{
        var exception:AppException = AppException(Error.UNKNOW,e)
        e?.let {
            when(it){
                is HttpException->{
                    exception=AppException(Error.NET_ERROR,e)
                }
                is JsonParseException,is JSONException ,is ParseException,is MalformedJsonException->{
                   exception=AppException(Error.PARSE_ERROR,e)
                }
                is ConnectException->{
                    exception= AppException(Error.NET_ERROR,e)
                }
                is SSLException->{
                    exception= AppException(Error.SSL_ERROR,e)
                }
                is TimeoutException,is SocketTimeoutException->{
                    exception=AppException(Error.TIME_OUT,e)
                }
                is UnknownHostException->{
                    exception=AppException(Error.TIME_OUT,e)
                }
            }
        }
        return  exception
    }
}