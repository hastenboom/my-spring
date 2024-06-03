package com.student.aop.advice

/**
 * @author Student
 */
interface AspectInstanceFactory
{
    /**
     * Create an instance of this factory's aspect.
     * @return the aspect instance (never `null`)
     */
    fun getAspectInstance(): Any
}