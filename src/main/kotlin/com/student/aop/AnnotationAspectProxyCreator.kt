package com.student.aop

import com.student.aop.test.AnnotationAdvisorFactory
import com.student.aop.test.ProxyFactory
import com.student.core.factory.BeanDefinition

/**
 * TODO: AOP部分没有考虑xml和annotation的解耦然后进行抽象。因为我时间不太够了
 * @author Student
 */
//是所有targetObj共用的
class AnnotationAspectProxyCreator(
    val beanDefinitionList: List<BeanDefinition>,
)
{
    //多个targetObj共用的
    val annotationAdvisorFactory = AnnotationAdvisorFactory(beanDefinitionList)


    //防止重复代理
    var proxyRecord: MutableSet<Any> = mutableSetOf()

    /**
     * How-to judge whether the targetObj requires proxying?
     * 1. Check whether the targetObj has already been proxied. If so, don't proxy it again.
     * 2. Check whether the targetObj has any eligible advisors. If not, don't proxy it.
     * 3. Else, proxy it
     *
     * @param targetObj
     * @param beanName
     * @return
     */
    fun createProxy(targetObj: Any, beanName: String): Any
    {
        if (proxyRecord.contains(targetObj))
        {
            throw Exception("The $targetObj has already been proxied")
        }

        //here, we create the advisors from the aspect
        val eligibleAdvisorsList = annotationAdvisorFactory.findEligibleAdvisors(targetObj)
        //没有找到任何advisors,直接返回原对象
        if (eligibleAdvisorsList.isEmpty())
        {
            return targetObj
        }
        //存在advisors，需要考虑代理
        else
        {
            val proxyFactory = ProxyFactory(targetObj, targetObj.javaClass.interfaces.toList(), eligibleAdvisorsList)
            return proxyFactory.getProxy()
        }

    }
}