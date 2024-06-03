package com.student.core.application

import com.student.core.annotation.Autowired
import com.student.core.annotation.Value
import com.student.core.factory.AnnotationBeanDefinition
import com.student.core.factory.BeanDefinition
import com.student.support.registry.AnnotationBeanDefinitionRegistryResolver
import com.student.support.registry.BeanDefinitionRegistryResolver

/**
 * @author Student
 */
class AnnotationApplicationContext(packagePath: String) : AbstractApplicationContext(packagePath)
{
    override var beanDefinitionRegistryResolver: BeanDefinitionRegistryResolver =
            AnnotationBeanDefinitionRegistryResolver()

    /**
     * 在这里考虑代理的后置处理
     */
    override fun registerExtraBeanPostProcessors()
    {

    }

    //TODO: handle $ only
    private fun searchInjectValueCandidate(valueStr: String): String
    {

        if (valueStr.startsWith("\$"))
        {
            val startIndex = valueStr.indexOf("{")
            val endIndex = valueStr.indexOf("}")
            val key = valueStr.substring(startIndex + 1, endIndex)
            //TODO: 从系统变量中获取值
            val findDollarValueCandidate = findDollarValueCandidate(key)

            findDollarValueCandidate ?: throw IllegalArgumentException("Cannot find the value of $key")

            return findDollarValueCandidate
        }
        throw IllegalArgumentException("Cannot find the value of $valueStr")
    }

    private fun searchInjectAutowire(beanName: String): Any?
    {
        return getBean(beanName)
    }

    override fun injectBean(beanName: String, beanDefinition: BeanDefinition, instance: Any)
    {
        val metaData = beanDefinition.getMetaData()
        //filed, @Autowired and @Value
        /**
         * @Autowired
         * XxxService xxxService;
         *
         * @Value("\${JAVA_HOME}")
         * String javaHome;
         */
        metaData.getAllFields().forEach { field ->
            if (field.annotations.any { it.annotationClass.simpleName == "Autowired" })
            {
                val simpleName = field.type.simpleName
                val beanName1 = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)
                val candidateObj = searchInjectAutowire(beanName1)

                candidateObj
                        ?: throw IllegalArgumentException("Autowired: Cannot find the candidate bean of $beanName1")

                candidateObj.let {
                    field.isAccessible = true
                    field.set(instance, getBean(beanName1))
                }
            }
            //TODO: Value的处理涉及到字符状态机或正则表达式，我只处理${}这种情况；#{}这种是要考虑配置文件的
            else if (field.annotations.any { it.annotationClass.simpleName == "Value" })
            {
                val valueStr = field.getAnnotation(Value::class.java).value
                //${}
                val candidateValue = searchInjectValueCandidate(valueStr)

                candidateValue.let {
                    field.isAccessible = true
                    field.set(instance, it)
                }
            }
        }

        //setter injection
        metaData.getAllMethods().forEach { method ->
            if (method.isAnnotationPresent(Autowired::class.java) && method.parameters.size == 1 && method.name.startsWith(
                    "set"
                )
            )
            {
                val parameter = method.parameters[0]
                if (parameter.isAnnotationPresent(Value::class.java))
                {
                    val valueStr = parameter.getAnnotation(Value::class.java).value
                    val candidateValue = searchInjectValueCandidate(valueStr)
                    candidateValue.let {
                        method.isAccessible = true
                        method.invoke(instance, it)
                    }
                }
                else
                {
                    val simpleName = parameter.type.simpleName
                    val beanName1 = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)
                    val candidateObj = searchInjectAutowire(beanName1)
                    candidateObj.let {
                        method.isAccessible = true
                        method.invoke(instance, getBean(beanName1))
                    }
                }
            }

        }
    }


    private fun findDollarValueCandidate(key: String): String?
    {
        var candidateValue = systemVariablesCache[key]
        if (candidateValue != null)
        {
            return candidateValue
        }
        candidateValue = systemPropertiesCache[key]

        return candidateValue

    }

    override fun setBeanByName(beanName: String, bean: Any)
    {
        TODO("Not yet implemented")
    }

    /**
     * Thread-safe. Add the bean definition into the [beanDefinitionsCache].
     * The subclass is responsible for implementing the [resolveBeanDefinition] by initializing the [com.student.support.registry.BeanDefinitionRegistryResolver]
     *
     * @see com.student.support.registry.BeanDefinitionRegistryResolver
     * @see AnnotationApplicationContext
     * @param beanDefinition [BeanDefinition]
     */
    override fun registerBeanDefinition(beanDefinition: BeanDefinition)
    {
        synchronized(this.beanDefinitionsMap) {
            //manage only the class annotated with @Component
            if (beanDefinition.getMetaData().isClassAnnotatedWith("Component"))
            {
                this.beanDefinitionsMap.put(
                    beanDefinition.getSimpleBeanName(),
                    beanDefinitionRegistryResolver.resolve(beanDefinition)
                )
            }

        }
    }

    override fun registerBeanDefinition(className: String)
    {
        // 这里已经写死了用的就是AnnotationBeanDefinition
        registerBeanDefinition(AnnotationBeanDefinition(Class.forName(className)))
    }

}