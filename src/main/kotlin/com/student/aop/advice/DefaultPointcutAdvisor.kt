package com.student.aop.advice

import com.student.aop.Advice
import com.student.aop.Advisor
import com.student.aop.Pointcut

/**
 * @author Student
 */
class DefaultPointcutAdvisor(pointcut: Pointcut, advice: Advice) : Advisor
{
    private val pointcut: Pointcut = pointcut
    private val advice: Advice = advice

    override fun getPointcut(): Pointcut
    {
        return pointcut
    }

    override fun getAdvice(): Advice
    {
        return advice
    }

}
