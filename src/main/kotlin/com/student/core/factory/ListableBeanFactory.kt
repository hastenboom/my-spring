package com.student.core.factory

/**
 * @author Student
 */
interface ListableBeanFactory : BeanFactory
{

    fun getBeansByType(type: Class<*>): List<*>

    fun getBeansByInterface(interfaceType: Class<*>): List<*>

    fun getBeansBySuperClass(superClass: Class<*>): List<*>

}