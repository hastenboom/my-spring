package com.student.aop.test

import com.student.aop.Interceptor
import com.student.aop.MethodInterceptor
import com.student.aop.MethodInvocation
import java.lang.reflect.Method

/**
 * @author Student
 */

class MyInvocation(
    private val targetObj: Any,
    private val method: Method,
    private val args: Array<Any>,
    private val methodInterceptorList: List<MethodInterceptor>
) : MethodInvocation
{


    private var count = 1
    override fun proceed(): Any?
    {
        if (count > methodInterceptorList.size)
        {
            return method.invoke(targetObj, *args)
        }
        val methodInterceptor = methodInterceptorList[count - 1]
        count += 1
        return methodInterceptor.invoke(this)
    }


    override fun getArguments(): Array<Any>
    {
        TODO("Not yet implemented")
    }

    override fun setArguments(args: Array<Any>)
    {
        TODO("Not yet implemented")
    }

    override fun getMethod(): Method
    {
        TODO("Not yet implemented")
    }
}
