package com.student.support.registry

import com.student.core.annotation.Scope
import com.student.core.factory.BeanDefinition

/**
 * - resolve the @Configuration and @Scope annotations
 * - resolve the @Bean, @Value, @Autowired to create a dependency list;
 * @author Student
 */
class AnnotationBeanDefinitionRegistryResolver : BeanDefinitionRegistryResolver
{

    override fun resolve(beanDefinition: BeanDefinition): BeanDefinition
    {
        resolveClassAnnotation(beanDefinition)
        return beanDefinition
    }

    /**
     * Consider the minimal resolve scope.
     *
     * -  [com.student.annotation.common.Component], [Scope]
     * @param beanDefinition
     */
    private fun resolveClassAnnotation(beanDefinition: BeanDefinition)
    {
//        val metaData = beanDefinition.getMetaData()
//        //TODO: Configuration should work with tbe @Bean, consider how to cooperate both
//        if (withConfiguration(metaData))
//        {
//            beanDefinition.factoryBean = true
//        }
//        if (withScope(metaData))
//        {
//            val scope = metaData.getClassAnnotationByName("Scope") as Scope
//            if (scope.value == "prototype")
//            {
//                if (withConfiguration(metaData))
//                {
//                    throw IllegalStateException("@Configuration class cannot be @Scope(prototype) scope")
//                }
//                beanDefinition.scope = "prototype"
//            }
//        }
        val metaData = beanDefinition.getMetaData()
        if (metaData.isClassAnnotatedWith("Scope"))
        {
            val scope = metaData.getClassAnnotationByName("Scope") as Scope
            if (scope.value == "prototype")
            {
                beanDefinition.scope = "prototype"
            }
            if (scope.value == "singleton" || scope.value == "")
            {
                beanDefinition.scope = "singleton"
            }
        }
    }


    /**
     * Resolve dependency annotation
     * TODO: 重新考虑这部分的架构，目前的实现方式过于简单粗暴，不够灵活
     * 在[AnnotationApplicationContext]中，是直接使用了[AnnotationBeanDefinitionRegistryResolver]，并且直接用的是[AnnotaionBeanDefinition]
     *
     * 然后在这里， 我需要解析注册DependencyInfo，但是他本可以使用解析xml，这里是直接写死了用annotation
     * - 我的计划是
     *
     * TODO: The setter and constructor injection are not supported yet, only field injection is supported.
     * @param beanDefinition
     */
//    private fun resolveDependencyAnnotation(beanDefinition: BeanDefinition)
//    {
//        val metaData = beanDefinition.getMetaData()
//
////        //method, setter only, no constructor injection
////        for (method in metaData.getAllMethods())
////        {
////            if (metaData.isAutowiredCandidateMethod(method))
////            {
////                DependencyInfo(method.name)
////            }
////        }
//
//        for (field in metaData.getAllFields())
//        {
//            if (metaData.isFieldAnnotatedWith(field, "Autowired"))
//            {
//                beanDefinition.addDependencyInfo(DependencyInfo(field.name, field.type, "field", "Autowired"))
//            }
//            else if (metaData.isFieldAnnotatedWith(field, "Value"))
//            {
//                beanDefinition.addDependencyInfo(DependencyInfo(field.name, field.type, "field", "Value"))
//            }
//        }
//
//    }
}