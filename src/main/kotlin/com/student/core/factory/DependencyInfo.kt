package com.student.core.factory

class DependencyInfo(
    var dependencyName: String,
    var dependencyClassType: Class<*>,
    var fieldOrMethod: String,
    var autowiredOrValue: String
){


}
