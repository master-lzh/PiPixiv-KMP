import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import java.io.OutputStreamWriter

class MapGeneratorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.mrl.pixiv.data.GenerateMap")
        val ret = symbols.filter { !it.validate() }.toList()

        symbols.filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Visitor(), Unit) }

        return ret
    }

    private inner class Visitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.packageName.asString()
            val className = classDeclaration.simpleName.asString()
            val fileName = "${className}MapExtensions"
            val file = codeGenerator.createNewFile(
                Dependencies(false, classDeclaration.containingFile!!),
                packageName,
                fileName
            )

            OutputStreamWriter(file).use { writer ->
                writer.write("package $packageName\n\n")
                writer.write("@Suppress(\"SENSELESS_COMPARISON\")\n")
                writer.write("fun $className.toMap(): Map<String, Any?> {\n")
                writer.write("    return mapOf(\n")
                classDeclaration.getAllProperties().forEach { property ->
                    val type = property.type.resolve()
                    val propName = property.simpleName.asString().toSnakeCase()
                    when {
                        type.isSubclassOf("IBaseEnum") -> {
                            writer.write("        \"$propName\" to this.${property.simpleName.asString()}.value,\n")
                        }

                        else -> {
                            writer.write("        \"$propName\" to this.${property.simpleName.asString()},\n")
                        }
                    }
                }
                writer.write("    ).filterValues { it != null }\n")
                writer.write("}\n")
            }
        }
    }

    // 扩展函数，用于判断一个类型是否继承了某个接口
    fun KSType.isSubclassOf(interfaceName: String): Boolean {
        val declaration = this.declaration as KSClassDeclaration? ?: return false
        declaration.superTypes.forEach {
            if (it.resolve().toString() == interfaceName) {
                return true
            }
        }
        return false
    }

    private fun String.toSnakeCase(): String {
        return this.fold("") { acc, c ->
            if (c.isUpperCase()) {
                if (acc.isNotEmpty()) acc + "_" + c.lowercaseChar()
                else acc + c.lowercaseChar()
            } else {
                acc + c
            }
        }
    }
}

class MapGeneratorProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return MapGeneratorProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}