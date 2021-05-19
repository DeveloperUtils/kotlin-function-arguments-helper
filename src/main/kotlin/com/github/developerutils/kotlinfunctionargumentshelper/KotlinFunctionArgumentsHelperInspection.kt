package com.github.developerutils.kotlinfunctionargumentshelper

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ui.MultipleCheckboxOptionsPanel
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.valueArgumentListVisitor
import javax.swing.JComponent

class KotlinFunctionArgumentsHelperInspection(
    @JvmField var withoutDefaultValues: Boolean = false,
    @JvmField var withoutDefaultArguments: Boolean = false
) : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ) = valueArgumentListVisitor { element ->
        val descriptor = element.descriptor() ?: return@valueArgumentListVisitor
        if (descriptor.valueParameters.size == element.arguments.size) return@valueArgumentListVisitor
        val description =
            if (descriptor is ClassConstructorDescriptor) CONSTRUCTOR_PROBLEM_TITLE else FUNCTION_PROBLEM_TITLE
        val fix = AddArgumentsQuickFix(
            description = description,
            withoutDefaultValues = withoutDefaultValues,
            withoutDefaultArguments = withoutDefaultArguments
        )
        holder.registerProblem(element, description, fix)
    }

    override fun createOptionsPanel(): JComponent = MultipleCheckboxOptionsPanel(this).apply {
        addCheckbox("Don't add default values (guessed by variable type)", "withoutDefaultValues")
        addCheckbox("Do not add arguments that have default values declared", "withoutDefaultArguments")
    }
}
