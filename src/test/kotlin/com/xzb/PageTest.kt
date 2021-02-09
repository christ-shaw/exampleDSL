package com.xzb


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

 class PageTest {

    @Test
    fun writeTest() {
        Page().write("## test Result" )
        val file = File("readme.md")
        assert(file.exists())
        var content : String = ""
        file.forEachLine {
            content += it
        }
        assert(content.contains("test Result"))
    }
}