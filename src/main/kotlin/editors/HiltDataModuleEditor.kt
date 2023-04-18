package editors

import utils.FileUtil
import utils.addImports
import utils.asIdentificator
import writers.Injectable
import writers.ModelWriter
import java.io.File
import java.lang.Exception

class HiltDataModuleEditor(
    rootFile: File,
    datasourceWriter: ModelWriter,
    daoWriter: ModelWriter
) {
    init {
        val dataFile = FileUtil.searchFileInDirectory(rootFile, "DataModule.kt") ?: throw Exception()
        val databaseFile = FileUtil.searchFileInDirectory(rootFile, "Database.kt") ?: throw Exception()
        val databaseName = databaseFile.nameWithoutExtension

        val implementationClassName: String
        val implementationImport: String
        if (datasourceWriter is Injectable) {
            implementationClassName = datasourceWriter.implementationClassName
            implementationImport = datasourceWriter.implementationImport
        } else {
            implementationClassName = "${datasourceWriter.className}Impl"
            implementationImport = "${datasourceWriter.importPath}Impl"
        }
        val updatedText = dataFile.readText()
            .addImports(datasourceWriter.importPath, implementationImport, daoWriter.importPath)
            .replaceFirst(
                "@Binds",
                "@Binds\n    abstract fun bind${datasourceWriter.className}(datasource: $implementationClassName): ${datasourceWriter.className}\n\n    @Binds"
            )
            .replaceFirst(
                "@Provides",
                "@Provides\n        fun provide${daoWriter.className}(database: $databaseName): ${daoWriter.className} {\n" +
                        "            return database.${daoWriter.className.asIdentificator()}\n" +
                        "        }\n\n        @Provides"
            )
        dataFile.writeText(updatedText)
    }
}