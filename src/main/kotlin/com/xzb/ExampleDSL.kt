package com.xzb

import mu.KLogger
import mu.KotlinLogging
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

fun mdLink(title: String, target: String) = "[$title]($target)"
fun mdLink(page: Page) = mdLink(page.title, page.fileName)

class BlockOutputCapture()
{
    private val byteArrayInputStream = ByteArrayOutputStream()
    val printWriter = PrintWriter(byteArrayInputStream)

    fun print(message : Any?)
    {
        printWriter.println(message)
    }

    fun println(message: Any?)
    {
        printWriter.println(message)
    }

    fun output():String
    {
        printWriter.flush()
        return byteArrayInputStream.toString()
    }
    fun reset()
    {
        printWriter.flush();
        byteArrayInputStream.reset()
    }


}

class ExampleDSL(sourceRepository: SourceRepository) : AutoCloseable {
    private val buf = StringBuilder()

    operator fun String.unaryPlus() {
        println()
        buf.appendLine(this.trimIndent().trimMargin())
        buf.appendLine()
    }

    fun mdCodeBlock(
        code: String,
        type: String,
        allowLongLines: Boolean = false,
        wrap: Boolean = false,
        lineLength: Int = 80
    ) {
        // 看是否换行
        if (wrap) {
            code.lines().flatMap {
                if (it.length <= lineLength) {
                    listOf(it)
                } else {
                    it.chunked(lineLength)
                }
            }
        }.joinToString("\n")
        if (!allowLongLines) {
            var l = 1
            var error = 0
            code.lines().forEach {
                if (it.length > lineLength) {
                    logger.warn { "code block contains lines longer than 80 characters at line $l:\n$it" }
                    error++
                }
                l++
            }
            if (error > 0) {
                throw IllegalArgumentException("code block exceeds line length of $lineLength")
            }
        }

        buf.appendLine("```$type\n$code\n```\n")
    }

    //  从 class 获取文件名字
    private fun fileName(clazz: KClass<*>)
    {
        clazz.qualifiedName!!
            .replace("\\$.*?$".toRegex(), "").replace('.', File.separatorChar) + ".kt"
    }

    private fun sourcePathFroClass(clazz: KClass<*>)
    {

    }


    companion object {
        fun markdown(
            sourceRepository: SourceRepository,
            block: ExampleDSL.() -> Unit
        ): String {
            val example = ExampleDSL(sourceRepository)
            example.use(block)
            return example.buf.toString()
        }
    }

    override fun close() {

    }

}