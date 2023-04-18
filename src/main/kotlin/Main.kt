import editors.DatabaseEditor
import editors.HiltDataModuleEditor
import editors.HiltDomainModuleEditor
import writers.*
import java.io.File

const val ROOT_PATH =
    "/Users/mykytahvozdylko/AndroidStudioProjects/FuturesCalculator/app/src/main/java/com/hedgehog/futurescalculator"

fun main(args: Array<String>) {
    val rootFile = File(System.getProperty("user.dir"))
//    val rootFile = File(ROOT_PATH)
    val packagePath = getPackage(rootFile)

    println("Enter entity name: ")
    val enteredEntityName = readln()
    val tableName = enteredEntityName + "Table"

    val entityWriter = EntityWriter(
        rootPath = rootFile.path,
        enteredEntityName = enteredEntityName,
        rootPackage = packagePath,
        tableName = tableName
    )

    val domainWriter = DomainWriter(
        rootPath = rootFile.path,
        enteredEntityName = enteredEntityName,
        rootPackage = packagePath
    )

    val mappingWriter = MappingWriter(
        rootPath = rootFile.path,
        enteredEntityName = enteredEntityName,
        rootPackage = packagePath,
        entityImport = entityWriter.importPath,
        domainImport = domainWriter.importPath,
        entity = entityWriter.className,
        domain = domainWriter.className
    )

    val daoWriter = DaoWriter(
        rootPath = rootFile.path,
        enteredEntityName = enteredEntityName,
        packagePath = packagePath,
        entityWriter = entityWriter,
        tableName = tableName
    )

    val datasourceWriter = DatasourceWriter(
        rootPath = rootFile.path,
        enteredEntityName = enteredEntityName,
        packagePath = packagePath,
        dao = daoWriter.className,
        daoImport = daoWriter.importPath,
        entityImport = entityWriter.importPath,
        domain = domainWriter.className,
        domainImport = domainWriter.importPath,
        mappingImport = mappingWriter.importPath
    )

    val usecaseWriter = UseCaseWriter(
        rootPath = rootFile.path,
        enteredEntity = enteredEntityName,
        rootPackage = packagePath,
        domainImport = domainWriter.importPath,
        domain = domainWriter.className,
        datasourceImport = datasourceWriter.importPath,
        datasource = datasourceWriter.className
    )

    DatabaseEditor(
        rootFile = rootFile,
        entityWriter = entityWriter,
        daoWriter = daoWriter
    )

    HiltDataModuleEditor(
        rootFile = rootFile,
        datasourceWriter = datasourceWriter,
        daoWriter = daoWriter,
    )

    HiltDomainModuleEditor(
        rootFile = rootFile,
        usecaseWriter = usecaseWriter
    )
}

private fun getPackage(rootFile: File): String {
    val startRange = if (rootFile.path.contains("java")) {
        rootFile.path.indexOf("java" + File.separator) + 5
    } else {
        rootFile.path.indexOf("kotlin" + File.separator) + 7
    }
    val packagePath = rootFile.path.subSequence(startRange, rootFile.path.length)
        .replace(Regex(File.separator), ".")
    return packagePath
}


