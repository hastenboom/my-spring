package com.student.support.beanpostprocssors

/**
 * @author Student
 */
interface BeanPostProcessor
{

//    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
//
//    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }

    fun postProcessBeforeInitialization(bean:Any, beanName:String)
    fun postProcessAfterInitialization(bean:Any, beanName:String)
}