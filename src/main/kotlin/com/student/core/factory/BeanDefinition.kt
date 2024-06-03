package com.student.core.factory

import com.student.support.MetaData

/**
 * TODO: not completed yet
 * - though I set it as interface, it haven't set some methods yet.
 * - those methods should be considered the xml configuration of the bean.
 * - but, I don't have spare time in consider the xml configuration; so, I just leave some comments here.
 * @author Student
 */
interface BeanDefinition
{

    var beanClazz: Class<*>
    var scope: String
    var factoryBean: Boolean

    fun getMetaData(): MetaData
    {
        return MetaData(beanClazz)
    }


    fun isSingleton(): Boolean
    {
        return scope == "singleton"
    }

    fun isPrototype(): Boolean
    {
        return scope == "prototype"
    }

    fun isFactoryBean(): Boolean
    {
        return factoryBean
    }



    //就单单是小写开头的类名，如TestClass1，会得到testClass1，这个是默认在所有容器进行beanName查询时使用的标识；
    fun getSimpleBeanName(): String
    {
        val simpleName = beanClazz.simpleName
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)
    }

    //这个是全类名 如：com.student.demo.TestClass1
    fun getClassNameOnly(): String
    {
        return beanClazz.name
    }


}

