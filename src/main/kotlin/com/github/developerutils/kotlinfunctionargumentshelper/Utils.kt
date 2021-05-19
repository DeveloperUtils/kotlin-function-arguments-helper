package com.github.developerutils.kotlinfunctionargumentshelper

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.resolveToCall
import org.jetbrains.kotlin.load.java.descriptors.JavaCallableMemberDescriptor
import org.jetbrains.kotlin.psi.KtCallElement
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

const val CONSTRUCTOR_PROBLEM_TITLE = "Add missing constructor arguments"
const val FUNCTION_PROBLEM_TITLE = "Add missing function arguments"

internal fun KtValueArgumentList.descriptor(): CallableDescriptor? {
    val calleeExpression = getStrictParentOfType<KtCallElement>()?.calleeExpression ?: return null
    val descriptor = calleeExpression.resolveToCall()?.resultingDescriptor
    if (descriptor is JavaCallableMemberDescriptor) return null
    return descriptor.takeIf { it is ClassConstructorDescriptor || it is SimpleFunctionDescriptor }
}
