package com.student.core.factory

/**
 * @author Student
 */
class AnnotationBeanDefinition(
    override var beanClazz: Class<*>,
    override var scope: String = "singleton",
    override var factoryBean: Boolean = false
) : BeanDefinition
{

}