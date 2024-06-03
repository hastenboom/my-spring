package com.student.resource

import java.io.File
import java.net.URL
import java.util.function.Predicate

/**
 *
 * Caring only about the FILE loading, it provides a fileNameFilter to filter the files by name.
 *
 *
 * @see
 * @author Student
 */
class ClassPathFileLoader
{
    private val CLASSPATH: String = "classpath:"
    private val FILE_PROTOCOL: String = "file"


    /**
     * Load files
     *
     * @param packagePath for dir, com/example/path1/path2, for file, com/example/path1/path2/AClass.class; Be aware of the inconsistency of path, when talking about the package path, we always use "/"
     * @param fileNameFilter only work when packagePath is a directory path, filter the files(directories excluded) by name.
     * @return List<File> might be empty if no file found
     */
    fun loadFiles(
        packagePath: String,
        fileNameFilter: Predicate<String>? = null
    ): List<File>
    {

        val classLoader = ClassPathFileLoader::class.java.classLoader
        val url: URL? = classLoader.getResource(packagePath)
        url ?: throw IllegalArgumentException("Resource not found in classpath: $packagePath")


        val ret: MutableList<File> = mutableListOf()

        if (url.protocol.equals(FILE_PROTOCOL))
        {
            val file = File(url.path)
            if (file.isFile)
            {
                ret.add(file)
            }
            else
            {
                if (file.listFiles().isNullOrEmpty())
                {
                    throw IllegalArgumentException("Empty directory in classpath: ${file.path}")
                }
                extractFilesFromDirectory(file, packagePath, ret, fileNameFilter)
            }
        }
        return ret

    }


    /**
     * A helper method to extract files from directory recursively.
     *
     * FIXME: 🤔It might have some efficiency issue.
     *
     */
    private fun extractFilesFromDirectory(
        dir: File,
        packagePath: String,
        collector: MutableList<File>,
        fileNameFilter: Predicate<String>? = null
    )
    {

        val files: Array<File> = dir.listFiles()!!

        files.forEach {
            if (it.isFile && (fileNameFilter?.test(it.name) ?: true))
            {
                collector.add(it)
            }
            else if (it.isDirectory)
            {
                extractFilesFromDirectory(it, packagePath, collector, fileNameFilter)
            }
        }
    }

}



