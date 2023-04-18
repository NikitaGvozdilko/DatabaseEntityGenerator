package utils

import java.io.File

object FileUtil {
    fun searchFileInDirectory(directory: File, nameMatch: String): File? {
        val files = directory.listFiles()

        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    val foundFile = searchFileInDirectory(file, nameMatch)
                    if (foundFile != null) {
                        return foundFile
                    }
                } else if (file.name.contains(nameMatch)) {
                    return file
                }
            }
        }

        return null
    }
}