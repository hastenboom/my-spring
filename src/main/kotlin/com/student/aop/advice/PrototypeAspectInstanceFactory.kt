package com.student.aop.advice

/**
 * @author Student
 */
class PrototypeAspectInstanceFactory(private val clazz: Class<*>) : AspectInstanceFactory
{
    override fun getAspectInstance(): Any
    {
        return clazz.newInstance()
    }
}