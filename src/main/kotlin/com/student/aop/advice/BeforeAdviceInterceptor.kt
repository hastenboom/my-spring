package com.student.aop.advice

import com.student.aop.AspectExpressionPointcut
import com.student.aop.MethodInterceptor
import com.student.aop.MethodInvocation
import java.lang.reflect.Method

/**
 * TODO:
 * @author Student
 */
class BeforeAdviceInterceptor(
    val aspectMethod: Method,
    val pointcut: AspectExpressionPointcut,
    val aif: PrototypeAspectInstanceFactory
) : MethodInterceptor
{
    override fun invoke(invocation: MethodInvocation): Any?
    {
        //before
        aspectMethod.invoke(aif.getAspectInstance(), null)
        return invocation.proceed()
    }
}