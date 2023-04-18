package writers

import utils.WriteUtils
import java.nio.file.Paths

class DomainWriter(rootPath: String, enteredEntityName: String, rootPackage: String) : ModelWriter {
    override val className: String = enteredEntityName
    override val classFileName: String = "${className}.kt"
    override val importPath: String = "$rootPackage.domain.model.$className"

    init {
        val path = Paths.get(rootPath, "domain", "model").toString()
        WriteUtils.createFileAndWrite(
            folderPath = path,
            fileName = classFileName,
            textToWriteInFile = """
                |package $rootPackage.domain.model
                |
                |class $className(
                |    val id: Long
                |)
            """.trimIndent()
        )
    }
}