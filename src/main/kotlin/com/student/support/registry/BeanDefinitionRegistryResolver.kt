package com.student.support.registry

import com.student.core.factory.BeanDefinition

/**
 * @author Student
 */
interface BeanDefinitionRegistryResolver
{
    fun resolve(beanDefinition: BeanDefinition) : BeanDefinition
}