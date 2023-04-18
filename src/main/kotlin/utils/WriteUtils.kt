package utils

import java.io.File
import java.io.FileWriter

object WriteUtils {
    fun createFileAndWrite(folderPath: String, fileName: String, textToWriteInFile: String) {
        println("Creating file $fileName by path $folderPath")
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdir()
        }

        val entity = File(folder.path, fileName)
        entity.createNewFile()

        FileWriter(entity).use {
            it.write(textToWriteInFile.trimMargin("|"))
        }
    }
}