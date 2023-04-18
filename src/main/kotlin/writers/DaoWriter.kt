package writers

import utils.WriteUtils
import java.nio.file.Paths

private const val DAO_IMPORT_PATH = "data.database.dao"
private const val DAO_PATH = "/data/database/dao"

class DaoWriter(
    rootPath: String,
    enteredEntityName: String,
    packagePath: String,
    entityWriter: ModelWriter,
    tableName: String,
) : ModelWriter {
    override val className = "${enteredEntityName}Dao"
    override val classFileName: String = "${className}.kt"
    override val importPath: String = "$packagePath.$DAO_IMPORT_PATH.$className"

    init {
        val path = Paths.get(rootPath, "data", "database", "dao").toString()
        val entityName = entityWriter.className
        WriteUtils.createFileAndWrite(
            folderPath = path,
            fileName = classFileName,
            textToWriteInFile = """
                |package $packagePath.$DAO_IMPORT_PATH
                |
                |import androidx.room.Dao
                |import androidx.room.Insert
                |import androidx.room.OnConflictStrategy
                |import androidx.room.Query
                |import ${entityWriter.importPath}
                |import kotlinx.coroutines.flow.Flow
                |
                |@Dao
                |interface $className {
                |    @Query("SELECT * FROM $tableName")
                |    fun getList(): Flow<List<$entityName>>
                |
                |    @Query("SELECT * FROM $tableName WHERE id=:id")
                |    suspend fun getItem(id: Long): $entityName
                |
                |    @Insert(onConflict = OnConflictStrategy.REPLACE)
                |    suspend fun insert(entity: $entityName)
                |
                |    @Insert(onConflict = OnConflictStrategy.REPLACE)
                |    suspend fun insertAll(list: List<$entityName>)
                |
                |    @Query("DELETE FROM $tableName")
                |    suspend fun clear()
                |}
            """
        )
    }
}