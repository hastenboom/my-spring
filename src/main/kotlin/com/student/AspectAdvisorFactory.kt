package com.student

import com.student.aop.Advisor

/**
 * @author Student
 */
interface AspectAdvisorFactory
{
    /**
     * 是否是切面类 @Aspect
     * @param clazz
     * @return
     */
    fun isAspect(clazz: Class<*>): Boolean

    /**
     * 解析 @Aspect 切面类中的所有切面
     * @param clazz
     * @return
     */
    fun getAdvisors(clazz: Class<*>): List<Advisor>

}