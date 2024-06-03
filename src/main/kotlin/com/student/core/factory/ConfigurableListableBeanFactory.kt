package com.student.core.factory

import com.student.support.beanfactorypostpostprocssor.BeanFactoryPostProcessor
import com.student.support.beanpostprocssors.BeanPostProcessor

/**
 * 🧐 TODO:需要加锁保证他们的使用，比如整个容器在还没有初始化完成前，不允许调用removeBeanByName；在初始化中，不允许进行addBean()操作。
 * 这样可以防止在并发环境下面，一边在refresh()，一边在add()或者remove()操作。
 * @author Student
 */
interface ConfigurableListableBeanFactory : ListableBeanFactory
{

    fun addBean(beanName: String, beanDefinition: BeanDefinition): Boolean

    fun removeBeanByName(beanName: String): Boolean

    fun setBeanByName(beanName: String, bean: Any)

    fun addBeanFactoryPostProcessor(beanFactoryPostProcessor: BeanFactoryPostProcessor)

    fun addBeanPostProcessor(beanPostProcessor: BeanPostProcessor)

}