package com.student.aop.test

import com.student.aop.Advisor
import com.student.aop.AspectExpressionPointcut
import com.student.aop.advice.AroundAdviceInterceptor
import com.student.aop.advice.BeforeAdviceInterceptor
import com.student.aop.advice.DefaultPointcutAdvisor
import com.student.aop.advice.PrototypeAspectInstanceFactory
import com.student.aop.annotation.After
import com.student.aop.annotation.Around
import com.student.aop.annotation.Aspect
import com.student.aop.annotation.Before
import com.student.core.factory.BeanDefinition

/**
 * @author Student
 */
class AnnotationAdvisorFactory
    (
    val beanDefinitionList: List<BeanDefinition>
)
{
    private var allAdvisorList: MutableList<Advisor>? = null


    // TODO: or targetClazz
    fun findEligibleAdvisors(targetObj: Any): List<Advisor>
    {
        val allAdvisorsList = generateAllAdvisors()

        var ret = ArrayList<Advisor>()

        for (method in targetObj.javaClass.declaredMethods)
        {
            for (advisor in allAdvisorsList)
            {
                if (advisor.getPointcut().getMethodMatcher().matches(method, targetObj.javaClass))
                {
                    ret.add(advisor)
                }
            }
        }
        return ret
    }


    private fun generateAllAdvisors(): List<Advisor>
    {
        // if it has been created before, return it
        allAdvisorList?.let { return it }

        allAdvisorList = ArrayList()
        beanDefinitionList
            .map { it.beanClazz }
            .forEach {
                allAdvisorList!!.addAll(transformAspectIntoAdvisor(it))
            }

        return allAdvisorList!!
    }

    //TODO : transformAspectIntoAdvisor
    private fun transformAspectIntoAdvisor(aspectClass: Class<*>): List<Advisor>
    {
        val arrayList = ArrayList<Advisor>()
        if (aspectClass.isAnnotationPresent(Aspect::class.java))
        {
            val aif = PrototypeAspectInstanceFactory(aspectClass)

            val methods = aspectClass.declaredMethods
            for (aspectMethod in methods)//aspectMethod annotate with @Before or @After...
            {
                // Aspect into the advisor
                if (aspectMethod.isAnnotationPresent(Before::class.java))
                {
                    val expression = aspectMethod.getAnnotation(Before::class.java).value
                    val pointcut = AspectExpressionPointcut()
                    pointcut.setExpression(expression)

                    //also the advice
                    val interceptor = BeforeAdviceInterceptor(aspectMethod, pointcut, aif)
                    //在这里加入的就是DefaultPointcutAdvisor
                    val advisor = DefaultPointcutAdvisor(pointcut, interceptor)

                    arrayList.add(advisor)
                }
                else if (aspectMethod.isAnnotationPresent(After::class.java))
                {
                    val expression = aspectMethod.getAnnotation(After::class.java).value
                    val pointcut = AspectExpressionPointcut()
                    pointcut.setExpression(expression)

                    //also the advice
                    val interceptor = BeforeAdviceInterceptor(aspectMethod, pointcut, aif)
                    val advisor = DefaultPointcutAdvisor(pointcut, interceptor)
                    arrayList.add(advisor)
                }
                else if (aspectMethod.isAnnotationPresent(Around::class.java))
                {
//                    check(aspectMethod.parameterCount != 0) { "环绕通知的参数中缺少 ProceedingJoinPoint" }
//                    check(aspectMethod.parameterTypes[0] == ProceedingJoinPoint::class.java) { "环绕通知的参数中第一个位置必须是 ProceedingJoinPoint" }

                    val expression = aspectMethod.getAnnotation(Around::class.java).value
                    val pointcut = AspectExpressionPointcut()
                    pointcut.setExpression(expression)

                    //also the advice
                    val interceptor = AroundAdviceInterceptor(aspectMethod, pointcut, aif)
                    val advisor = DefaultPointcutAdvisor(pointcut, interceptor)
                    arrayList.add(advisor)
                }
            }
        }
        return arrayList
    }

}