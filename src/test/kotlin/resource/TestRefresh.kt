package resource

import com.student.core.application.AnnotationApplicationContext


val context = AnnotationApplicationContext("com/student/demo")

fun testPrepareRefresh(){

}




fun main(){

    context.refresh()
}


