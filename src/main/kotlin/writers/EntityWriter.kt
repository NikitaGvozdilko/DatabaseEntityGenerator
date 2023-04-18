package writers

import utils.WriteUtils
import java.io.File
import java.nio.file.Paths

class EntityWriter(rootPath: String, enteredEntityName: String, rootPackage: String, tableName: String) : ModelWriter {
    override val className: String = enteredEntityName + "Entity"
    override val classFileName: String = "${className}.kt"
    override val importPath: String = "${rootPackage}.data.database.model.$className"

    init {
        val folderPath = Paths.get(rootPath, "data", "database", "model").toString()
//        val folderPath = "$rootPath/data/database/model"
        WriteUtils.createFileAndWrite(
            folderPath = folderPath,
            fileName = "$className.kt",
            textToWriteInFile = """
                |package ${rootPackage}.data.database.model
                |
                |import androidx.room.Entity
                |import androidx.room.PrimaryKey
                |
                |@Entity(tableName = "$tableName")
                |class ${className}(
                |    @PrimaryKey(autoGenerate = true)
                |    val id: Long = 0
                |)
                """
        )
    }

}