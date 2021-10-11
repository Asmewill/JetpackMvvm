package com.example.wapp.demo.ext

import java.lang.reflect.ParameterizedType

/**
 * Created by jsxiaoshui on 2021/8/18
 */
/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
fun <VM> getVmClazz(obj:Any):VM{
    return  (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}