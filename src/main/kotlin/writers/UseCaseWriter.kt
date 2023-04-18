package writers

import utils.WriteUtils
import utils.asIdentificator
import java.nio.file.Paths

class UseCaseWriter(
    rootPath: String,
    enteredEntity: String,
    rootPackage: String,
    domainImport: String,
    domain: String,
    datasourceImport: String,
    datasource: String
) : ModelWriter, Injectable {
    override val className: String = "${enteredEntity}UseCase"
    override val classFileName: String = "$className.kt"
    override val importPath: String = "$rootPackage.domain.usecase.$className"
    override val implementationClassName: String = "${className}Impl"
    override val implementationImport: String = "$rootPackage.domain.usecase.$implementationClassName"

    init {
        val datasourceIdentificator = datasource.asIdentificator()
        val path = Paths.get(rootPath, "domain", "usecase").toString()
        WriteUtils.createFileAndWrite(
            folderPath = path,
            fileName = classFileName,
            textToWriteInFile = """
                |package $rootPackage.domain.usecase
                |
                |import $domainImport
                |import $datasourceImport
                |import javax.inject.Inject
                |import kotlinx.coroutines.flow.Flow
                |
                |interface $className {
                |    fun getList(): Flow<List<$domain>>
                |}
                |
                |class $implementationClassName @Inject constructor(
                |    private val $datasourceIdentificator: $datasource
                |) : $className {
                |    override fun getList(): Flow<List<$domain>> {
                |        return $datasourceIdentificator.getList()
                |    }
                |}
            """
        )
    }
}