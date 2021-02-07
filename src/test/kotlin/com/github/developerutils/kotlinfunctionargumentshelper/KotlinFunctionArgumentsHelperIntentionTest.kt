package com.github.developerutils.kotlinfunctionargumentshelper

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.idea.KotlinFileType

class KotlinFunctionArgumentsHelperIntentionTest : BasePlatformTestCase() {
    fun `test fill class constructor`() {
        doAvailableTest(
            """
            class User(val name: String, val age: Int)
            fun test() {
                User(<caret>)
            }
        """,
            """
            class User(val name: String, val age: Int)
            fun test() {
                User(name = "", age = 0)
            }
        """
        )
    }

    fun `test can't fill class constructor`() {
        doUnavailableTest(
            """
            class User(val name: String, val age: Int)
            fun test() {
                User("", 0<caret>)
            }
        """
        )
    }

    fun `test fill function`() {
        doAvailableTest(
            """
            class User(val name: String, val age: Int)
            fun foo(s: String, t: Int, u: User) {}
            fun test() {
                foo(<caret>)
            }
        """,
            """
            class User(val name: String, val age: Int)
            fun foo(s: String, t: Int, u: User) {}
            fun test() {
                foo(s = "", t = 0, u = User(name = "", age = 0))
            }
        """,
            "Fill function arguments"
        )
    }

    fun `test can't fill function`() {
        doUnavailableTest(
            """
            fun foo(s: String, t: Int) {}            
            fun test() {
                foo("", 0<caret>)
            }
        """
        )
    }

    fun `test fill for non primitive types`() {
        doAvailableTest(
            """
            class A(a1: String, a2: Int)
            class B(b1: Int, b2: String, a: A)
            class C
            class D(a: A, b: B, c: C, r: Runnable)
            fun test() {
                D(<caret>)
            }
        """,
            """
            class A(a1: String, a2: Int)
            class B(b1: Int, b2: String, a: A)
            class C
            class D(a: A, b: B, c: C, r: Runnable)
            fun test() {
                D(a = A(a1 = "", a2 = 0), b = B(b1 = 0, b2 = "", a = A(a1 = "", a2 = 0)), c = C(), r =)
            }
        """
        )
    }

    fun `test don't add default value for enum,abstract,sealed`() {
        doAvailableTest(
            """
            enum class A(val a: String) {
                Foo("foo"), Bar("bar"), Baz("baz");
            }
            sealed class B(val b: String)
            abstract class C(val c: String)
            class Test(a: A, b: B, c: C)
            fun test() {
                Test(<caret>)
            }
        """,
            """
            enum class A(val a: String) {
                Foo("foo"), Bar("bar"), Baz("baz");
            }
            sealed class B(val b: String)
            abstract class C(val c: String)
            class Test(a: A, b: B, c: C)
            fun test() {
                Test(a =, b =, c =)
            }
        """
        )
    }

    fun `test add import directives`() {
        val dependency =
            """
            package com.example

            class A
            class B(a: A)
            """
        doAvailableTest(
            """
            import com.example.B
            
            val b = B(<caret>)
            """,
            """
            import com.example.A
            import com.example.B
            
            val b = B(a = A())
            """,
            dependencies = listOf(dependency)
        )
    }

    fun `test call java constructor`() {
        val javaDependency =
            """
            public class Java {
                public Java(String str) {
                }
            }
            """
        doUnavailableTest(
            """
            fun test() {
                Java(<caret>)
            }
            """,
            javaDependencies = listOf(javaDependency)
        )
    }

    fun `test call java method`() {
        val javaDependency =
            """
            public class Java {
                public Java(String str) {
                }
            
                public void foo(Java java) {
                }
            }
            """

        doUnavailableTest(
            """
            fun test() {
                Java("").foo(<caret>)
            }
        """,
            javaDependencies = listOf(javaDependency)
        )
    }

    private val intention = KotlinFunctionArgumentsHelperIntention()

    private fun doAvailableTest(
        before: String,
        after: String,
        intentionText: String = "Fill constructor arguments",
        dependencies: List<String> = emptyList(),
        javaDependencies: List<String> = emptyList()
    ) {
        checkCaret(before)
        dependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.kt", dependency.trimIndent())
        }
        javaDependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.java", dependency.trimIndent())
        }
        myFixture.configureByText(KotlinFileType.INSTANCE, before.trimIndent())
        myFixture.launchAction(intention)
        check(intentionText == intention.text) {
            "Intention text mismatch. [expected]$intentionText [actual]${intention.text}"
        }
        myFixture.checkResult(after.trimIndent())
    }

    private fun doUnavailableTest(
        before: String,
        dependencies: List<String> = emptyList(),
        javaDependencies: List<String> = emptyList()
    ) {
        checkCaret(before)
        dependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.kt", dependency.trimIndent())
        }
        javaDependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.java", dependency.trimIndent())
        }
        myFixture.configureByText(KotlinFileType.INSTANCE, before.trimIndent())
        check(intention.familyName !in myFixture.availableIntentions.mapNotNull { it.familyName }) {
            "Intention should not be available"
        }
    }

    private fun checkCaret(before: String) {
        check("<caret>" in before) {
            "Please, add `<caret>` marker to\n$before"
        }
    }
}
