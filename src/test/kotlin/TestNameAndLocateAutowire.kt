import com.student.core.annotation.Value
import com.student.core.factory.AnnotationBeanDefinition

/**
 * @author Student
 */


fun main()
{

    println("---------------------class------------------------")
    val clazz = Class.forName("com.student.demo.TestClass1")
    val bd = AnnotationBeanDefinition(clazz)
    println(
        bd.getSimpleBeanName().substring(0, 1)
            .toLowerCase() + bd.getSimpleBeanName().substring(1)
    )

    println(clazz.name)


    val metaData = bd.getMetaData()
    println("---------------------method---------------------")
    val autowiredMethods = metaData.getAllMethods().filter { method ->
        method.name.startsWith("set") &&
                method.annotations.any { it.annotationClass.simpleName == "Autowired" }
    }
    autowiredMethods.forEach { method ->
        if (method.parameters.size == 0)
        {
            throw IllegalArgumentException("No parameter found for method ${method.name}")
        }
        else
        {
            for (parameter in method.parameters)
            {
                val simpleName = parameter.type.simpleName
                val beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)
                //然后进去找，第一个是bean，第二个是getBean()
//                method.invoke(null, null)
            }
        }
    }

    println("---------------------property---------------------")
    metaData.getAllFields().forEach { field ->
        if (field.annotations.any { it.annotationClass.simpleName == "Autowired" })
        {
            val simpleName = field.type.simpleName
            val beanName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1)
//            field.isAccessible = true
//            field.set(bean, getBean(beanName))
        }
        else if (field.annotations.any { it.annotationClass.simpleName == "Value" })
        {
            val valueStr = field.getAnnotation(Value::class.java).value
            valueResolve(valueStr)

        }
    }


}

/**
 * ${}，是要从prepare那个阶段获取系统变量值等
 *TODO: #{}，则是要从配置文件中找，我这里不打算实现他
 * @param valueStr
 */
fun valueResolve(valueStr: String)
{
    if (valueStr.startsWith("\$"))
    {
        val startIndex = valueStr.indexOf("{")
        val endIndex = valueStr.indexOf("}")
        val key = valueStr.substring(startIndex + 1, endIndex)
        //TODO: 从系统变量中获取值
        println(key)

    }
}
