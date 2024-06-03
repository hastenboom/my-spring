package com.student.aop

/**
 * @author Student
 */
interface JoinPoint
{
    @Throws(Throwable::class)
    fun proceed(): Any?
}