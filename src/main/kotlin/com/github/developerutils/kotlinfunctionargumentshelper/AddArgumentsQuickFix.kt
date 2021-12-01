package com.github.developerutils.kotlinfunctionargumentshelper

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.builtins.isFunctionType
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.idea.core.CollectingNameValidator
import org.jetbrains.kotlin.idea.core.KotlinNameSuggester
import org.jetbrains.kotlin.idea.core.ShortenReferences
import org.jetbrains.kotlin.idea.imports.importableFqName
import org.jetbrains.kotlin.idea.intentions.callExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType

@Suppress("Detekt.ComplexMethod", "Detekt.ReturnCount")
class AddArgumentsQuickFix(
    private val description: String,
    private val withoutDefaultValues: Boolean,
    private val withoutDefaultArguments: Boolean
) : LocalQuickFix {
    override fun getName() = description

    override fun getFamilyName() = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement as? KtValueArgumentList ?: return
        val parameters = element.descriptor()?.valueParameters ?: return
        element.fillArguments(parameters)
    }

    private fun KtValueArgumentList.fillArguments(parameters: List<ValueParameterDescriptor>) {
        val arguments = this.arguments
        val argumentNames = arguments.mapNotNull { it.getArgumentName()?.asName?.identifier }
        val factory = KtPsiFactory(this)
        parameters.forEachIndexed { index, parameter ->
            if (arguments.size > index && !arguments[index].isNamed()) return@forEachIndexed
            if (parameter.name.identifier in argumentNames) return@forEachIndexed
            if (withoutDefaultArguments && parameter.declaresDefaultValue()) return@forEachIndexed
            val added = addArgument(createDefaultValueArgument(parameter, factory))
            val argumentExpression = added.getArgumentExpression()
            if (argumentExpression is KtQualifiedExpression || argumentExpression is KtLambdaExpression) {
                ShortenReferences.DEFAULT.process(argumentExpression)
            }
        }
    }

    private fun createDefaultValueArgument(
        parameter: ValueParameterDescriptor,
        factory: KtPsiFactory
    ): KtValueArgument {
        if (withoutDefaultValues) {
            return factory.createArgument(null, parameter.name)
        }

        val type = parameter.type
        val defaultValue = calculateDefaultValue(type)
        if (defaultValue != null) {
            return factory.createArgument(factory.createExpression(defaultValue), parameter.name)
        }

        val descriptor = type.constructor.declarationDescriptor as? LazyClassDescriptor
        val modality = descriptor?.modality
        if (descriptor?.kind == ClassKind.ENUM_CLASS || modality == Modality.ABSTRACT || modality == Modality.SEALED) {
            return factory.createArgument(null, parameter.name)
        }

        val fqName = descriptor?.importableFqName?.asString()
        val valueParameters =
            descriptor?.constructors?.firstOrNull { it is ClassConstructorDescriptor }?.valueParameters
        val argumentExpression = if (fqName != null && valueParameters != null) {
            (factory.createExpression("$fqName()")).also {
                val callExpression = it as? KtCallExpression ?: (it as? KtQualifiedExpression)?.callExpression
                callExpression?.valueArgumentList?.fillArguments(valueParameters)
            }
        } else {
            null
        }
        return factory.createArgument(argumentExpression, parameter.name)
    }

    private fun calculateDefaultValue(type: KotlinType) = when {
        KotlinBuiltIns.isBoolean(type) -> "false"
        KotlinBuiltIns.isChar(type) -> "''"
        KotlinBuiltIns.isDouble(type) -> "0.0"
        KotlinBuiltIns.isFloat(type) -> "0.0f"
        KotlinBuiltIns.isInt(type) || KotlinBuiltIns.isLong(type) || KotlinBuiltIns.isShort(type) -> "0"
        KotlinBuiltIns.isCollectionOrNullableCollection(type) -> "arrayOf()"
        KotlinBuiltIns.isNullableAny(type) -> "null"
        KotlinBuiltIns.isString(type) -> "\"\""
        KotlinBuiltIns.isListOrNullableList(type) -> "listOf()"
        KotlinBuiltIns.isSetOrNullableSet(type) -> "setOf()"
        KotlinBuiltIns.isMapOrNullableMap(type) -> "mapOf()"
        type.isFunctionType -> calculateLambdaDefaultvalue(type)
        type.isMarkedNullable -> "null"
        else -> null
    }

    private fun calculateLambdaDefaultvalue(ktType: KotlinType): String =
        buildString {
            append("{")
            if (ktType.arguments.size > 2) {
                val validator = CollectingNameValidator()
                val lambdaParameters = ktType.arguments.dropLast(1).joinToString(postfix = "->") {
                    val type = it.type
                    val name = KotlinNameSuggester.suggestNamesByType(type, validator, "param")[0]
                    validator.addName(name)
                    val typeText =
                        type.constructor.declarationDescriptor?.importableFqName?.asString() ?: type.toString()
                    val nullable = if (type.isMarkedNullable) "?" else ""
                    "$name: $typeText$nullable"
                }
                append(lambdaParameters)
            }
            append("}")
        }
}
