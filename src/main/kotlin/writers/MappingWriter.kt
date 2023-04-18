package writers

import utils.WriteUtils
import java.nio.file.Paths

class MappingWriter(
    rootPath: String,
    enteredEntityName: String,
    rootPackage: String,
    entityImport: String,
    domainImport: String,
    entity: String,
    domain: String
) : ModelWriter {
    override val className: String = "${enteredEntityName}Mapping"
    override val classFileName: String = "${className}.kt"
    override val importPath: String = "$rootPackage.utils.mapping.*"

    init {
        val path = Paths.get(rootPath, "utils", "mapping").toString()
        WriteUtils.createFileAndWrite(
            folderPath = path,
            fileName = classFileName,
            textToWriteInFile = """
                 |package $rootPackage.utils.mapping
                 |
                 |import $entityImport
                 |import $domainImport
                 |
                 |fun $entity.toDomain(): $domain {
                 |    return $domain(id = id)
                 |}
                 |
                 |fun $domain.toEntity(): $entity {
                 |    return $entity()
                 |}
                 """.trimIndent()
        )
    }
}