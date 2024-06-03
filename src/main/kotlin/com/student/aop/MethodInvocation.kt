package com.student.aop

import java.lang.reflect.Method

/**
 * 这个是创建调用链的
 * @author Student
 */
interface MethodInvocation : JoinPoint
{

    /**
     * 获得执行链中目标方法的实参
     * @return
     */
    fun getArguments(): Array<Any>

    /**
     * 修改执行链中目标方法的实参
     * ProxyMethodInvocation 中的功能，这里直接放在 MethodInvocation 中了，允许修改实参
     */
    fun setArguments(args: Array<Any>)

    /**
     * 获得执行链中的目标方法
     * @return
     */
    fun getMethod(): Method

}