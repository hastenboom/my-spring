package com.student.aop

/**
 * @author Student
 */
interface Advisor
{
    fun getPointcut(): Pointcut


    fun getAdvice(): Advice
}