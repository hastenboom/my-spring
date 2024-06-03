package com.student.core.factory

/**
 * @author Student
 */
interface BeanFactory
{
    fun getBean(beanName: String, type: Class<*>? = null): Any?
}