package com.xzb

import java.io.File

data class Page(val title :String = "readme",
                val outputDir : String = ".",
                val fileName :String = "${title.trim().toLowerCase()}.md"
){
    val file = File(outputDir,fileName)

    fun write(markDown:String)
    {
        file.writeText("""
            # $title
             
        """.trimIndent() + markDown)
    }
}