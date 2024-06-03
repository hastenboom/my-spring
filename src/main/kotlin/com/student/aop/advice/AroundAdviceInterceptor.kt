package com.student.aop.advice

import com.student.aop.AspectExpressionPointcut
import com.student.aop.MethodInterceptor
import com.student.aop.MethodInvocation
import java.lang.reflect.Method

/**
 * @author Student
 */
class AroundAdviceInterceptor(
    val aspectMethod: Method,
    val pointcut: AspectExpressionPointcut,
    val aif: PrototypeAspectInstanceFactory
) : MethodInterceptor
{
    override fun invoke(invocation: MethodInvocation): Any?
    {
        //before
        val methodBeingProxied = invocation.getMethod()

        aspectMethod.invoke(aif.getAspectInstance(), methodBeingProxied)

        return invocation.proceed()
    }
}