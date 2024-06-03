package com.student.core.application

import com.student.aop.AnnotationAspectProxyCreator
import com.student.core.factory.BeanDefinition
import com.student.core.factory.ConfigurableListableBeanFactory
import com.student.resource.ClassPathFileLoader
import com.student.support.ObjectFactory
import com.student.support.beanfactorypostpostprocssor.BeanFactoryPostProcessor
import com.student.support.beanpostprocssors.AutowireCandidateBeanPostProcessor
import com.student.support.beanpostprocssors.BeanPostProcessor
import com.student.support.registry.BeanDefinitionRegistry
import com.student.support.registry.DefaultSingletonBeanRegistry
import java.io.File

/**
 * @author Student
 */
abstract class AbstractApplicationContext : ConfigurableListableBeanFactory, BeanDefinitionRegistry,
    DefaultSingletonBeanRegistry
{

    constructor(packagePath: String)
    {
        this.packagePath = packagePath
        this.packagePathWithComma = packagePath.replace("/", ".")
    }

    private val packagePath: String
    private val packagePathWithComma: String//derived from packagePath

    protected val classPathFileLoader = ClassPathFileLoader()

    //The DS for the BeanDefinitionRegistry interface
    protected val beanDefinitionsMap = mutableMapOf<String, BeanDefinition>()
    protected val proxyCreator = AnnotationAspectProxyCreator(beanDefinitionsMap.values.toList())

    /**
     * check [prepareRefresh]
     */
    protected lateinit var systemVariablesCache: MutableMap<String, String>
    protected lateinit var systemPropertiesCache: MutableMap<String, String>


    protected val autowireCandidateResolver = AutowireCandidateBeanPostProcessor()

    protected val beanFactoryPostProcessorsCache = ArrayList<BeanFactoryPostProcessor>()
    protected val beanPostProcessorsCache = ArrayList<BeanPostProcessor>()


    /**
     *
     * it includes two parts:
     * - collect the environment variables and system properties
     * - scan the package path and register the BeanDefinition
     *
     * 😃 After this method, [systemVariablesCache], [systemPropertiesCache] will be initialized. Future @Value injection will utilize them.
     *
     * 😥 TODO:[beanDefinitionsCache] wil also be initialized, but this process is not completed yet,
     *  as they will be set as singleton by default. I need to provide more flexible configuration for this.
     *  - My first idea is to allow refresh() to add some BeanFactoryPostProcessors to handle the [beanDefinitionsCache] directly. But you know, the time complexity reaches O(n^2)
     *  - the second idea is to provide the `@Scope` resolver in the register process.
     */
    fun prepareRefresh()
    {
        //------------------collect the environment variables and system properties------------------
        this.systemVariablesCache = System.getenv()
        this.systemPropertiesCache = System.getProperties() as MutableMap<String, String>


        //------------------scan the package path and register the BeanDefinition------------------
        val files: List<File> = classPathFileLoader.loadFiles(packagePath)
        // extract the class names from the file paths
        // class names are like "com.student.core.bean.application.AnnotationBeanContainer" without ".class"
        val classNames = files
            .map { it.path }
            .filter { it.endsWith(".class") }
            .map { it.replace(File.separator, ".") }
            .map { it.substring(it.indexOf(packagePathWithComma), it.lastIndexOf(".class")) }

        // register the BeanDefinition using the class names
        // registerBeanDefinition is not implemented here, but in the subclass
        classNames.forEach { this.registerBeanDefinition(it) }
    }

    fun refresh()
    {
        prepareRefresh()
        registerBeanFactoryPostProcessors()
        runBeanFactoryPostProcssors(this)
        registerBeanPostProcessors()

        //!!
        finishBeanFactoryInitialization()

        finishRefresh()
    }


    fun registerBeanFactoryPostProcessors()
    {

        //for future default BFPP  registration

        registerExtraBeanFactoryPostProcessors()
    }

    // subclass can override this method to provide extra BFPPs
    open fun registerExtraBeanFactoryPostProcessors()
    {

    }

    fun registerBeanPostProcessors()
    {
        registerExtraBeanPostProcessors()
    }

    open fun registerExtraBeanPostProcessors()
    {
    }

    fun runBeanFactoryPostProcssors(beanFactory: ConfigurableListableBeanFactory)
    {
    }


    fun finishBeanFactoryInitialization()
    {
        beanDefinitionsMap.forEach { (beanName, beanDefinition) ->
            if (beanDefinition.isSingleton())
            {
                // getBean will also create the singleton instance
                getBean(beanName, beanDefinition.beanClazz)
            }
        }


    }

    fun finishRefresh()
    {

    }

    override fun addBean(beanName: String, beanDefinition: BeanDefinition): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun removeBeanByName(beanName: String): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun addBeanFactoryPostProcessor(beanFactoryPostProcessor: BeanFactoryPostProcessor)
    {
        TODO("Not yet implemented")
    }

    override fun addBeanPostProcessor(beanPostProcessor: BeanPostProcessor)
    {
        TODO("Not yet implemented")
    }

    override fun getBeansByType(type: Class<*>): List<*>
    {
        TODO("Not yet implemented")
    }

    override fun getBeansByInterface(interfaceType: Class<*>): List<*>
    {
        TODO("Not yet implemented")
    }

    override fun getBeansBySuperClass(superClass: Class<*>): List<*>
    {
        TODO("Not yet implemented")
    }

    override fun getBean(beanName: String, type: Class<*>?): Any?
    {
        val beanDefinition = beanDefinitionsMap[beanName]

        if (beanDefinition == null)
        {
            throw NoSuchElementException("""No bean named '$beanName' is defined""")
        }
        else
        {
            //singleton
            if (beanDefinition.isSingleton())
            {
                var singleton = getSingleton(beanName, true)
                if (singleton == null)
                {
                    singleton = createBean(beanName, beanDefinition)
                    addSingleton(beanName, singleton!!)
                }
                return singleton
            }
            //prototype
            else
            {
                return createBean(beanName, beanDefinition)
            }
        }
    }


    //TODO:这里要调用AspectProxyCreator的实现；
    // FA.getObject()执行的是这里
    protected fun getEarlyBeanReference(beanName: String?, bd: BeanDefinition, bean: Any?): Any?
    {
        //TODO: 这里直接调用了Annotation，这是子类的工作，我这里在偷懒
        return bean
    }

    fun createBean(beanName: String, beanDefinition: BeanDefinition): Any?
    {

        //TODO: 创建空对象，createEmptyInstance()的逻辑可以优化用来处理lazy，args等
        val instance: Any = createAnEmptyInstance(beanName, beanDefinition)

        if (beanDefinition.isSingleton())
        {
            //FA直接放在第三层
            this.singletonFactories[beanName] =
                    ObjectFactory { getEarlyBeanReference(beanName, beanDefinition, instance) }

            //!?? 🤔这行代码可能有问题，目前不知道为什么加在这里
            this.earlySingletonObjects.remove(beanName)
        }

        var exposedObj: Any = instance

        // 注入，这里的逻辑是子类实现的，xml会需要.xml文件来帮忙查找依赖关系
        // ，而annotation只需要beanDefinition的反射，因为不同的实现所以这里让子类进行实现
        //! 注意，此时exposedObj和instance是同一个对象，注入instance等同于注入exposedObj
        injectBean(beanName, beanDefinition, instance)

        //initializeBean()，内部会有代理的逻辑
        //如果被代理了，exposedObj指向PA，则此时exposedObj!=instance
        //如果没被代理，则 exposedObj仍然指向A，即exposedObj==instance
        //没被代理有两种情况：1.已经被提前代理了（代理内部的逻辑是被代理过则不允许2次代理） 2.根本就没有需要代理的操作
        exposedObj = initializeBean(beanName, beanDefinition, exposedObj)


        // 去二级缓存 earlySingletonObjects 中查看有没有当前 bean，
        // 如果有，说明发生了循环依赖，返回缓存中的 a 对象（可能是代理对象也可能是原始对象，主要看有没有切点匹配到 bean）。
        // 这里主要看的是 a ，他会在 b 依赖他之后把Fa放到Pa|a中；由于a本身依赖b，c，d等，如果这些被依赖的反过来依赖a，则a一定会被放在Pa|a的位置；


        // 如果发生循环依赖，则FA一定会转成PA|A，当A完成注入后，会进入到initialBean()的环节，此时有可能会创建代理
        if (beanDefinition.isSingleton())
        {

            val earlySingletonReference = getSingleton(beanName, false)
            if (earlySingletonReference != null)
            {
                // 这里接着上面initializeBean()的逻辑
                // 没被代理有两种情况：
                // 1.已经被提前代理了（代理内部的逻辑是被代理过则不允许2次代理）
                // 2.根本就没有需要代理的操作
                // 无论哪种情况，PA|A，都可以从二层缓存中获取对象。
                // 这实际上是这种考虑：B的过程中，不知道是不是创建了PA还是A（都要走代理）
                // 如果是创建了PA，则回到A中完成注入和初始后都不会进行代理创建，此时就会拿到A而不是PA，这是错误的
                if (exposedObj == instance)
                {
                    exposedObj = earlySingletonReference
                }
            }
        }
        return exposedObj
    }

    private fun initializeBean(beanName: String, beanDefinition: BeanDefinition, exposedObject: Any): Any
    {
        //TODO : 可以加后置处理器，以及AOP代理

        return exposedObject
    }

    /**
     * 允许使用 .xml 或者 annotation 来进行注入，却决于子类的实现
     *
     * @param beanName
     * @param beanDefinition
     * @param instance
     */
    abstract fun injectBean(beanName: String, beanDefinition: BeanDefinition, instance: Any)

    /**
     * TODO: should also consider the prototype
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private fun createAnEmptyInstance(beanName: String, beanDefinition: BeanDefinition): Any
    {
        val declaredConstructors = beanDefinition.beanClazz.getDeclaredConstructors()
        for (constructor in declaredConstructors)
        {
            if (constructor.parameterCount == 0)
            {
                return constructor.newInstance()
            }
        }
        throw IllegalArgumentException("No default constructor found for class " + beanDefinition.beanClazz.name)
    }

    public fun getBeanDefinitionMap(): MutableMap<String, BeanDefinition> = beanDefinitionsMap

}


