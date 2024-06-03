package resource

import com.student.resource.ClassPathFileLoader

/**
 * @author Student
 */
class TestClassPathFileResourceLoader
{
    var classPathFileLoader: ClassPathFileLoader = ClassPathFileLoader()

    fun testLoadFile()
    {
        //test for loading Resources files
        val loadFiles = classPathFileLoader.loadFiles("sfdsfsd")

        for (file in loadFiles)
        {
            println(file.length())
        }

        println()
        println()
        println()
        println()

        // test for loading .class files from classpath
        val loadFiles1 = classPathFileLoader.loadFiles("com/student/demo")

        for (file in loadFiles1)
        {
            println(file.path)
        }

    }


}


fun main()
{
    val testClassPathFileResourceLoader = TestClassPathFileResourceLoader()
    testClassPathFileResourceLoader.testLoadFile()
}