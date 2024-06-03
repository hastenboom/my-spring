package com.student.aop

import java.lang.reflect.Method
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Student
 */
class AspectExpressionPointcut : Pointcut, MethodMatcher
{
    private var packageAndClassName: String? = null

    private var returnType: String? = null
    private var argsTypeList: Array<String>? = null
    private var methodName: String? = null
    private var className: String? = null
    private var packageName: String? = null

    private lateinit var expression: String

    fun setExpression(expression: String)
    {
        this.expression = expression
    }

    override fun getMethodMatcher(): MethodMatcher
    {
        return this
    }

    //TODO check it
    override fun matches(method: Method, targetClass: Class<*>): Boolean
    {
        parsePointcutExpression()

        var returnType = method.returnType.getSimpleName();
        val argsTypeList = method.parameterTypes.map { it.simpleName }.toList()
        val methodName = method.name

        val packageAndClassName = method.declaringClass.name
        val index = packageAndClassName.lastIndexOf(".")
        val className = packageAndClassName.substring(index + 1)
        val packageName = packageAndClassName.substring(0, index)

        return matchReturnType(returnType)
                && matchArgsList(argsTypeList)
                && matchMethodName(methodName)
                && matchClassName(className)
                && matchPackageName(packageName)
    }


    // *
    private fun matchReturnType(returnType: String): Boolean
    {
        if (this.returnType.equals("*"))
        {
            return true
        }
        return this.returnType.equals(returnType)
    }

    private fun matchArgsList(argsList: List<String>): Boolean
    {
        if (this.argsTypeList!!.size == 1 && this.argsTypeList!![0] == "..")
        {
            return true
        }
        //TODO:涉及到list内容的匹配, possible error prone
        for (i in 0 until this.argsTypeList!!.size)
        {
            if (this.argsTypeList!![i] != argsList[i])
                return false
        }
        return true
    }

    // *
    private fun matchMethodName(methodName: String): Boolean
    {
        if (this.methodName.equals("*"))
        {
            return true
        }
        return this.methodName.equals(methodName)
    }

    private fun matchClassName(className: String): Boolean
    {
        if (this.className.equals("."))
        {
            return true
        }
        return this.className.equals(className)
    }

    //TODO: As I don't dive into the inner part,
    // like the com.example..Service..login(String,String)，so there might be some potential bugs here.
    private fun matchPackageName(packageName: String): Boolean
    {
        if (!this.packageName.equals(packageName) && this.className.equals("."))
            return true
        return this.packageName.equals(packageName)
    }

    private fun isExpressionTypeSupported(expression: String?): Matcher
    {
        val pattern = "^execution\\((.*)\\)"
        val r = Pattern.compile(pattern)

        val m = r.matcher(expression)

        return m
    }


    //TODO 考虑这里String的使用方式是否得当，是否应该用builder或buffer

    //String com.example.service.UserService.login(String,String)
    //* com.example.service..login(String, String)，service包下所有带login(String, String)的方法
    //* com.example.service..*(..)，service“包下所有”带“任意参数”的“所有方法”
    private fun parsePointcutExpression()
    {
        // 正则表达式匹配execution模式的切点表达式
        val matcher = isExpressionTypeSupported(expression)
        var detail: String? = null
        if (matcher.find())
        {
            detail = matcher.group(1)
            val split = detail.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            //returnType
            returnType = split[0]
            //println("ReturnType: $returnType")

            val rest1 = split[1] // package + class + method +args

            val leftBraceIndex = rest1.indexOf("(") //set package+class+method and args into two parts;
            val rightBraceIndex = rest1.indexOf(")")

            val unhandledArgs = rest1.substring(leftBraceIndex + 1, rightBraceIndex)
            argsTypeList = if (unhandledArgs.contains(".."))
            {
                arrayOf(unhandledArgs)
            }
            else
            {
                unhandledArgs.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            //println("argsList: " + argsList.contentToString())


            //methodName
            //methodName
            //* com.example.service..*(..)，service“包下所有”带“任意参数”的“所有方法”
            //                      i here
            //* com.example.service..login(String, String)，service包下所有带login(String, String)的方法
            val i = rest1.lastIndexOf(".", leftBraceIndex - 1)
            methodName = if (rest1.substring(leftBraceIndex - 1, leftBraceIndex) == "*")
            {
                "*"
            }
            else
            {
                rest1.substring(i + 1, leftBraceIndex)
            }
            //println("MethodName: $methodName")


            //packageAndClassName
            val unhandledPackageAndClassName = rest1.substring(0, i)
            packageAndClassName = unhandledPackageAndClassName
            className = if (unhandledPackageAndClassName.endsWith("."))
            {
                // it indicates any class in the package
                "."
            }
            else
            {
                unhandledPackageAndClassName.substring(unhandledPackageAndClassName.lastIndexOf(".") + 1)
            }
            packageName = unhandledPackageAndClassName.substring(0, unhandledPackageAndClassName.lastIndexOf("."))

            //            System.out.println("packageAndClassName: " + packageAndClassName);
//            println("packageName: $packageName")
//            println("className: $className")


        }
    }


}