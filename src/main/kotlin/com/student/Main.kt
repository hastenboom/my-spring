package com.student

import java.lang.reflect.Method

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


class ATestClass
{
    //"execution(String com.student.ATestClass.testMethod(String))"
    fun testMethod(input: String, output: String): String
    {
        println("Hello World")
        return "Hello World"
    }
}

fun main()
{
    val clazz = ATestClass::class.java
    clazz.declaredMethods.forEach { method ->

        var returnType = method.returnType.getSimpleName();
        println("returnType: $returnType")

        val argsList = method.parameterTypes.map { it.simpleName }.toList()
        println(argsList)


        val methodName = method.name
        println("methodName: $methodName")

        val packageAndClassName = method.declaringClass.name

        val index = packageAndClassName.lastIndexOf(".")
        val className = packageAndClassName.substring(index + 1)
        println("className: $className")
        val packageName = packageAndClassName.substring(0, index)
        println("packageName: $packageName")



    }


}