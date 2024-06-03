package com.student.demo

import com.student.core.annotation.Autowired
import com.student.core.annotation.Component
import com.student.core.annotation.Value

/**
 * @author Student
 */

@Component
class TestClass1
{
//    constructor(){}
//    @Autowired
//    private var testClass2: TestClass2? = null

    @Value("\${JAVA_HOME}")
    private var JAVA_HOME: String? = null


    fun foo(): Unit
    {
        println("Hello World")
    }

    private var testClass2: TestClass2? = null

    @Autowired
    fun setTestClass2(testClass2: TestClass2)
    {
        this.testClass2 = testClass2
    }

}

