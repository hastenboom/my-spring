package com.student.support

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Student
 */
class MetaData(var beanClazz: Class<*>)
{

   //is?
   fun isClassAnnotatedWith(annotationName: String): Boolean =
           getClassAnnotations().any { it.annotationClass.simpleName == annotationName }

    fun isFieldAnnotatedWith(field: Field, annotationName: String): Boolean =
            getFieldAnnotations(field).any { it.annotationClass.simpleName == annotationName }

    fun isMethodAnnotatedWith(method: Method, annotationName: String): Boolean =
            getMethodAnnotations(method).any { it.annotationClass.simpleName == annotationName }

    //get!
    fun getInterfaces(): Array<out Class<*>> = beanClazz.interfaces

    fun getClassAnnotations(): Array<out Annotation> = beanClazz.annotations

    fun getClassAnnotationByName(annotationName: String): Annotation? =
            getClassAnnotations().firstOrNull { it.annotationClass.simpleName == annotationName }

    fun getAllFields(): Array<out Field> = beanClazz.declaredFields
    fun getFieldByName(fieldName: String): Field? =
            getAllFields().firstOrNull { it.name == fieldName }
    fun getFieldByType(clazz: Class<*>): Field? =
            getAllFields().firstOrNull { it.type == clazz }
    fun getFieldAnnotations(field: Field): Array<out Annotation> = field.annotations


    fun getAllMethods(): Array<out Method> = beanClazz.declaredMethods
    fun getMethodByName(methodName: String): Method? =
            getAllMethods().firstOrNull { it.name == methodName }
    fun getMethodAnnotations(method: Method): Array<out Annotation> = method.annotations

}