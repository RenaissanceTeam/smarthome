package smarthome.raspberry

import java.io.File
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList


fun main() {
    val classes = findAllClasses()
    val allFiles = findAllFiles()

    File("classInfo.out").printWriter().use { output ->


        allFiles
                .map { file ->
                    val filename = file.name.split(".")[0]
                    val c = classes.keys.find { it.simpleName == filename } ?: return@map null

                    c to file
                }
                .filterNotNull()
                .forEachIndexed { index, it ->
                    output.println("_#subkey")
                    output.println(getTitle(it.first))
                    output.println()
                    output.println(it.second.readText())
                    output.println()
                    output.println()
                }

    }
}

private fun findAllClasses(): Map<Class<Any>, ClassInfo> {
    val classes = JavaFindClassesHelper.getClasses("smarthome")
            .map { it to ClassInfo(getTitle(it)) }
            .toMap()
    return classes
}

private fun findAllFiles(): List<File> {
    val allFiles = Files.walk(Paths.get("."))
            .filter {
                Files.isRegularFile(it)
            }
            .map { it.toFile() }
            .filter { !it.path.contains("/out/") }
            .filter { !it.path.contains("/build/") }
            .filter { !it.path.contains("/thirdpartydevices/") }
            .filter { !it.name.contains("Test.kt") }
            .filter { it.name.endsWith("kt") }
            .toList()
    return allFiles
}

data class ClassInfo(
        val title: String,
        val sourceCode: String = ""
)

private fun getTitle(c: Class<Any>): String {
    var result = ""
    if (c.isInterface) {
        result += "Интерфейс "
    } else {
        if (Modifier.isAbstract(c.modifiers)) result += "Абстрактный "
        result += "Класс "
    }
    result += c.simpleName

    return result
}
