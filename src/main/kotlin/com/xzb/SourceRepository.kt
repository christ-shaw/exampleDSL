package com.xzb

class SourceRepository(val repoUrl : String,
                        val branch : String = "master",
                       val sourcePath: Set<String> = setOf<String>("src/main/kotlin","src/test/kotlin"))
{
    fun md(block: ExampleDSL.()->Unit) = ExampleDSL.markdown(this,block)
    fun urlForFile(path: String) = "${repoUrl}/tree/${branch}/$path"
}