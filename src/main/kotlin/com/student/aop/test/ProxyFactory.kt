package com.student.aop.test

import com.student.aop.Advice
import com.student.aop.Advisor
import com.student.aop.MethodInterceptor
import java.lang.reflect.Proxy

/**
 * @author Student
 */
class ProxyFactory(
    val targetObj: Any,
    val interfaceList: List<Class<*>>?,
    val eligibleAdvisorList: List<Advisor>
)
{

    fun getProxy(): Any
    {
        // No matching advisor found, no proxy needed!
        if (eligibleAdvisorList.isEmpty())
        {
            return targetObj
        }
        else
        {
            return if (interfaceList == null || interfaceList.isEmpty())
            {
                createCglibProxy()
            }
            else
            {
                createJdkProxy()
            }

        }
    }

    private fun createJdkProxy(): Any
    {
        return Proxy.newProxyInstance(
            targetObj.javaClass.classLoader, interfaceList!!.toTypedArray()
        ) { proxy, method, args ->

            // collect the interceptors for the method
            val interceptorList = mutableListOf<Advice>()
            for (advisor in eligibleAdvisorList)
            {
                // TODO:pointcut, matcher的接口设计有误
                if (advisor.getPointcut().getMethodMatcher().matches(method, targetObj.javaClass))
                {
                    interceptorList.add(advisor.getAdvice())
                }
            }

            if (interceptorList.isNotEmpty())
            {
                @Suppress("UNCHECKED_CAST")
                val myInvocation = MyInvocation(targetObj, method, args, interceptorList as List<MethodInterceptor>)
                myInvocation.proceed()
            }
            //这个方法不需要被代理， interceptorList.isEmpty()
            else
            {
                method.invoke(targetObj, *args)
            }
        }

    }


    //TODO:
    fun createCglibProxy(): Any
    {
        return Object()
    }


}
