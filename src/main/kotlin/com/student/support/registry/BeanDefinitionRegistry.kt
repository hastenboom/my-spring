package com.student.support.registry

import com.student.core.factory.BeanDefinition

/**
 * @author Student
 */
interface BeanDefinitionRegistry
{
    var beanDefinitionRegistryResolver: BeanDefinitionRegistryResolver

    fun registerBeanDefinition(beanDefinition: BeanDefinition)

    fun registerBeanDefinition(className: String)
}