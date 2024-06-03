package com.student.support

import com.student.core.application.AbstractApplicationContext

/**
 * @author Student
 */
interface ApplicationContextAware
{
    fun setApplicationContext(applicationContext: AbstractApplicationContext)
}