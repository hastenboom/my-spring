package com.student.aop.test

import com.student.aop.Advisor
import com.student.aop.annotation.After
import com.student.aop.annotation.Aspect
import com.student.aop.annotation.Before
import com.student.core.annotation.Component
import com.student.core.factory.AnnotationBeanDefinition
import com.student.core.factory.BeanDefinition
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy


/**
 * @author Student
 */


@Aspect
@Component
class Aspect1
{
    @Before("execution(* com.student.aop.test.*.*(..))")
    fun before(): Unit
    {
        println("Aspect1 before")
    }


    @After("execution(* com.student.aop.test.*.*(..))")
    fun after(): Unit
    {
        println("Aspect1 after")
    }

}


@Component
class Target1
{

    fun foo(): Unit
    {
        println("Target1 foo")
    }
}


@Component
class Target2
{
    fun foo(): Unit
    {
        println("Target2 foo")
    }

}


var classList = mutableListOf<Class<*>>()
fun prepare()
{
    classList.add(Aspect1::class.java)
    classList.add(Target1::class.java)
    classList.add(Target2::class.java)
}

fun findMatchedAdvisors(advisorList: List<Advisor>, targetClass: Class<*>): ArrayList<Advisor>
{
    var ret = ArrayList<Advisor>()
    for (method in targetClass.declaredMethods)
    {
        for (advisor in advisorList)
        {
            if (advisor.getPointcut().getMethodMatcher().matches(method, targetClass))
            {
                ret.add(advisor)
            }
        }
    }
    return ret
}




//假设要给target1和target2用代理分别加上Aspect1的before和after方法
fun main()
{

    val bd1 = AnnotationBeanDefinition(Target1::class.java)
    val bd2 = AnnotationBeanDefinition(Target2::class.java)
    val bd3 = AnnotationBeanDefinition(Aspect1::class.java)
    val beanDefinitionList = mutableListOf<BeanDefinition>()


    val target1 = Target1()
    val target2 = Target2()


    // -------------- FA
//    val generateAdvisorsFromAspect = AnnotationAdvisorFactory()
//    val allAdvisors = generateAdvisorsFromAspect.generateAllAdvisors(beanDefinitionList)
//    val findEligibleAdvisors = generateAdvisorsFromAspect.findEligibleAdvisors(target1)
//
//
//    val FA = ProxyFactory(target1, null, findEligibleAdvisors)
//    FA.getProxy()
//




    Proxy.newProxyInstance(
        target1.javaClass.classLoader,
        arrayOf(Target1::class.java, Target2::class.java),
        InvocationHandler { proxy, method, args ->
            //注意查看 JdkDynamicAopProxy 那里的写法；


        })

}