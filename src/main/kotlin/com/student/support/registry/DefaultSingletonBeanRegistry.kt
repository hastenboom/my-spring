package com.student.support.registry

import com.student.support.ObjectFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Student
 */
open class DefaultSingletonBeanRegistry : SingletonBeanRegistry
{


    /**
     * The 1-level cache for singleton objects. Only when the beanDefinition walks through the entire **creation-DI-initialization** process, it will be cached.
     */
    protected val singletonObjects = ConcurrentHashMap<String, Any>()

    /**
     * The 2-level cache for the early singleton objects.
     * When bean being injection, the process reaches the 3-level cache generates the early singleton objects.
     * Those singleton objects will be placed in the 2-level cache
     */
    protected val earlySingletonObjects = ConcurrentHashMap<String, Any>()

    /**
     * When creating the objects (like `new A()`), the spring don't generate the objects but the object factories which will cached in the 3-level cache.
     */
    protected val singletonFactories = ConcurrentHashMap<String, ObjectFactory>()


    /**
     * TODO: I haven't considered the Proxy, AOP stuffs here;
     *
     *
     *
     * @param beanName
     * @return
     */
    /*    override fun getSingleton(beanName: String): Any? {

            var singletonInstance = this.firstLevelCache[beanName]
            if (singletonInstance == null) {
                singletonInstance = this.secondLevelCache[beanName]
                if (singletonInstance == null) {
                    //double-check pattern
                    synchronized(this.firstLevelCache) {
                        singletonInstance = this.firstLevelCache[beanName]
                        if (singletonInstance == null) {
                            singletonInstance = this.secondLevelCache[beanName]
                            if (singletonInstance == null) {
                                val objectFactory = this.thirdLevelCache[beanName]
                                if (objectFactory != null) {
                                    singletonInstance = objectFactory.getObject()
                                    //if the objFactory exist in 3-level cache, the singletonObj will not be null
                                    secondLevelCache[beanName] = singletonInstance!!
                                    thirdLevelCache.remove(beanName)
                                }
                            }
                        }
                    }
                }
            }

            return singletonInstance
        }*/

    /**
     * TODO: I haven't considered the Proxy, AOP stuffs here;
     *
     * TODO:Thread-unsafe. This is a FP style implementation.
     *
     * Check 1,[singletonObjects] and 2,[earlySingletonObjects] then 3-level,[singletonFactories] cache step by step. If singleton bean found in 1 or 2, return it. If not, check 3-level cache,
     * if it has the object factory, generate this object and put it into 2-level cache, then return it. If not, return null
     *
     * @param beanName
     * @return return a singleton object or null
     */
    override fun getSingleton(beanName: String, allowEarlyReference: Boolean): Any?
    {
        var singleton = this.singletonObjects[beanName]
        if (singleton == null)
        {
            singleton = this.earlySingletonObjects[beanName]
            if (singleton == null)
            {
                val objectFactory = this.singletonFactories[beanName]
                if (objectFactory != null && allowEarlyReference)
                {
                    // PA|A
                    // 这里是解决循环依赖的关键之一，如果A<->B，然后A先创建，则在B进行注入阶段时，会把A会在这里被找到然后放到level2中
                    singleton = objectFactory.getObject()
                    singletonObjects.remove(beanName)
                    earlySingletonObjects[beanName] = singleton!!
                }
            }
        }
        return singleton
    }


    override fun containSingleton(beanName: String): Boolean
    {
        synchronized(this.singletonObjects) {
            return this.singletonObjects.containsKey(beanName)
        }
    }


    /**
     * Thread-safe. Lock: firstLevelCache
     *
     * Register bean definition, checking only the duplication. It is the public method to register the bean definition. The detailed implementation relies on the [addSingleton]
     *
     * @param beanName not null
     * @param instance not null
     */
    open override fun registerBean(beanName: String, instance: Any): Unit
    {
        synchronized(this.singletonObjects) {
            if (containSingleton(beanName))
            {
                throw IllegalStateException("Bean with name : ['$beanName'] already exists in singleton cache")
            }
            addSingleton(beanName, instance)
        }
    }


    /**
     * Thread-safe. Lock: firstLevelCache;
     * Add singleton
     *
     * @param beanName
     * @param singletonObject
     */
    override fun addSingleton(beanName: String, singletonObject: Any)
    {
        synchronized(this.singletonObjects) {
            singletonObjects[beanName] = singletonObject
            earlySingletonObjects.remove(beanName)
            singletonFactories.remove(beanName)
        }

    }

    protected fun getSingletonMutex(): ConcurrentHashMap<String, Any>
    {
        return this.singletonObjects
    }
}