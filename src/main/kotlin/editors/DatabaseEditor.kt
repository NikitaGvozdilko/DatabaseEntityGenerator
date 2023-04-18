package editors

import utils.FileUtil
import utils.addImports
import utils.asIdentificator
import writers.EntityWriter
import writers.ModelWriter
import java.io.File
import java.lang.Exception

class DatabaseEditor(rootFile: File, entityWriter: EntityWriter, daoWriter: ModelWriter) {
    init {
        val databaseFile = findDatabaseFile(rootFile) ?: throw Exception()
        val daoIdentifier = daoWriter.className.asIdentificator()
        val databaseContent = databaseFile.readText()
            .addImports(entityWriter.importPath, daoWriter.importPath)
            .replace("[", "[\n        ${entityWriter.className}::class,")
            .replace("RoomDatabase() {", "RoomDatabase() {\n    abstract val $daoIdentifier: ${daoWriter.className}")
        databaseFile.writeText(databaseContent)
    }

    private fun findDatabaseFile(rootFile: File): File? {
        return FileUtil.searchFileInDirectory(rootFile, "Database.kt")
    }
}