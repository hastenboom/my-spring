package com.student.demo2

import com.student.core.annotation.Bean
import com.student.core.annotation.Configuration

/**
 * @author Student
 */

@Configuration
class TestClass3
{

    @Bean
    fun getTestClass4(): TestClass4
    {
        return TestClass4()
    }

}