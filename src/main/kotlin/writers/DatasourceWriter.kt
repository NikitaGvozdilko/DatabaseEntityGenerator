package writers

import utils.WriteUtils
import java.nio.file.Paths

class DatasourceWriter(
    rootPath: String,
    enteredEntityName: String,
    packagePath: String,
    dao: String,
    daoImport: String,
    entityImport: String,
    domain: String,
    domainImport: String,
    mappingImport: String
) : ModelWriter, Injectable {
    override val className = "${enteredEntityName}DataSource"
    override val classFileName: String = "${className}.kt"
    override val importPath: String = "$packagePath.data.datasource.$className"
    override val implementationClassName: String = "${className}Impl"
    override val implementationImport: String = "$packagePath.data.datasource.$implementationClassName"

    init {
        val path = Paths.get(rootPath, "data", "datasource").toString()
        WriteUtils.createFileAndWrite(
            folderPath = path,
            fileName = classFileName,
            textToWriteInFile = """
                |package $packagePath.data.datasource
                |
                |import $daoImport
                |import $entityImport
                |import $domainImport
                |import $mappingImport
                |import javax.inject.Inject
                |import kotlinx.coroutines.flow.Flow
                |import kotlinx.coroutines.flow.map
                |
                |interface $className {
                |    fun getList(): Flow<List<$domain>>
                |    suspend fun getItem(id: Long): $domain
                |    suspend fun add(domain: $domain)
                |    suspend fun addAll(list: List<$domain>)
                |    suspend fun clear()
                |}
                |
                |class $implementationClassName @Inject constructor(
                |    private val dao: $dao
                |) : $className {
                |    override fun getList(): Flow<List<$domain>> {
                |        return dao.getList().map { list ->
                |            list.map { it.toDomain() }
                |        }
                |    }
                |
                |    override suspend fun getItem(id: Long): $domain {
                |        return dao.getItem(id).toDomain()
                |    }
                |
                |    override suspend fun add(domain: $domain) {
                |        dao.insert(domain.toEntity())
                |    }
                |
                |    override suspend fun addAll(list: List<$domain>) {
                |        dao.insertAll(list.map { it.toEntity() })
                |    }
                |
                |    override suspend fun clear() {
                |        dao.clear()
                |    }
                |}
                """.trimIndent()
        )
    }
}