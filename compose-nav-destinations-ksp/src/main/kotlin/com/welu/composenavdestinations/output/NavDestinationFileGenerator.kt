package com.welu.composenavdestinations.output

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.welu.composenavdestinations.extensions.dependencies
import com.welu.composenavdestinations.extensions.write
import com.welu.composenavdestinations.mapper.NavDestinationMapper

class NavDestinationFileGenerator(
    private val resolver: Resolver,
    private val codeGenerator: CodeGenerator
): FileGenerator<NavDestinationMapper.GeneratedClass> {

    override fun generate(instance: NavDestinationMapper.GeneratedClass) {
        val fileOutputStream = codeGenerator.createNewFile(
            dependencies = resolver.dependencies,
            packageName = instance.packageName,
            fileName = instance.simpleName+ "Generated"
        ).write("package ${instance.packageName}\n\n")

        fileOutputStream.close()
    }

}