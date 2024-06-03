package com.student.support.registry

/**
 * @author Student
 */
interface SingletonBeanRegistry
{

    fun getSingleton(beanName: String, allowEarlyReference: Boolean): Any?

    fun containSingleton(beanName: String): Boolean

    fun registerBean(beanName: String, instance: Any): Unit


    fun addSingleton(beanName: String, singletonObject: Any): Unit

}