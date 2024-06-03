package com.student.aop

import java.lang.reflect.Method

/**
 * @author Student
 */
interface MethodMatcher
{
    fun matches(method: Method, targetClass: Class<*>): Boolean
}