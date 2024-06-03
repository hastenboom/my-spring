package com.student.aop

/**
 * @author Student
 */
interface MethodInterceptor : Interceptor
{
    @Throws(Throwable::class)
    fun invoke(invocation: MethodInvocation): Any?

}