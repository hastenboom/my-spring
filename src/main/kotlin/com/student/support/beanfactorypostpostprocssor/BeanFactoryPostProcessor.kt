package com.student.support.beanfactorypostpostprocssor

import com.student.core.factory.ConfigurableListableBeanFactory

interface BeanFactoryPostProcessor
{
    fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory)
}
