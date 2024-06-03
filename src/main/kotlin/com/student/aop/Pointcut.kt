package com.student.aop

/**
 * @author Student
 */
interface Pointcut
{
    fun getMethodMatcher(): MethodMatcher
}