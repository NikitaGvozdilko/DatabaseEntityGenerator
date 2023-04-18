package editors

import utils.FileUtil
import utils.addImports
import writers.UseCaseWriter
import java.io.File

class HiltDomainModuleEditor(rootFile: File, usecaseWriter: UseCaseWriter) {
    init {
        val usecaseModule = FileUtil.searchFileInDirectory(rootFile, "DomainModule.kt") ?: throw Error()

        val updatedText = usecaseModule.readText()
            .addImports(usecaseWriter.importPath, usecaseWriter.implementationImport)
            .replaceFirst(
                "@Binds",
                "@Binds\n    abstract fun bind${usecaseWriter.className}(usecase: ${usecaseWriter.implementationClassName}): ${usecaseWriter.className}\n\n    @Binds"
            )
        usecaseModule.writeText(updatedText)
    }
}