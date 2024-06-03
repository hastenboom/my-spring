package com.student

import com.student.aop.Advisor
import com.student.aop.AspectExpressionPointcut
import com.student.aop.annotation.Aspect
import com.student.aop.annotation.Before

/**
 *
 * @author Student
 */
//class DefaultAspectAdvisorFactory : AspectAdvisorFactory
//{
//    override fun isAspect(clazz: Class<*>): Boolean
//    {
//        return clazz.isAnnotationPresent(Aspect::class.java)
//    }
//
//    override fun getAdvisors(clazz: Class<*>): List<Advisor>
//    {
//        for (method in clazz.methods)
//        {
//            if (method.isAnnotationPresent(Before::class.java))
//            {
//                // two types: @annotation or execution
//                val expression = method.getAnnotation(Before::class.java).value
//                val pointcut = AspectExpressionPointcut()
//                pointcut.setExpression(expression)
//
//
//            }
//        }
//
//
//    }
//}