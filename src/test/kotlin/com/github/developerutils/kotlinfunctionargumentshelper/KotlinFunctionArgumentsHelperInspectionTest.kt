package com.github.developerutils.kotlinfunctionargumentshelper

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.idea.KotlinFileType

@Suppress("Detekt.LongParameterList")
class KotlinFunctionArgumentsHelperInspectionTest : BasePlatformTestCase() {
    fun `test fill class constructor`() {
        doAvailableTest(
            before = """
                        class User(val name: String, val age: Int)
                        fun test() {
                            User(<caret>)
                        }
                    """,
            after = """
                        class User(val name: String, val age: Int)
                        fun test() {
                            User(name = "", age = 0)
                        }
                    """
        )
    }

    fun `test can't fill class constructor`() {
        doUnavailableTest(
            before = """
                        class User(val name: String, val age: Int)
                        fun test() {
                            User("", 0<caret>)
                        }
                    """
        )
    }

    fun `test fill function`() {
        doAvailableTest(
            before = """
                        class User(val name: String, val age: Int)
                        fun foo(s: String, t: Int, u: User) {}
                        fun test() {
                            foo(<caret>)
                        }
                    """,
            after = """
                        class User(val name: String, val age: Int)
                        fun foo(s: String, t: Int, u: User) {}
                        fun test() {
                            foo(s = "", t = 0, u = User(name = "", age = 0))
                        }
                    """,
            problemDescription = FUNCTION_PROBLEM_TITLE
        )
    }

    fun `test can't fill function`() {
        doUnavailableTest(
            before = """
                        fun foo(s: String, t: Int) {}            
                        fun test() {
                            foo("", 0<caret>)
                        }
                    """
        )
    }

    fun `test fill for non primitive types`() {
        doAvailableTest(
            before = """
                        class A(a1: String, a2: Int)
                        class B(b1: Int, b2: String, a: A)
                        class C
                        class D(a: A, b: B, c: C, r: Runnable)
                        fun test() {
                            D(<caret>)
                        }
                    """,
            after = """
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
            before = """
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
            after = """
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
        val dependency = """
            package com.example

            class A
            class B(a: A)
        """
        doAvailableTest(
            before = """
                        import com.example.B
                        
                        val b = B(<caret>)
                    """,
            after = """
                        import com.example.A
                        import com.example.B
                        
                        val b = B(a = A())
                    """,
            dependencies = listOf(dependency)
        )
    }

    fun `test call java constructor`() {
        val javaDependency = """
            public class Java {
                public Java(String str) {
                }
            }
        """
        doUnavailableTest(
            before = """
                        fun test() {
                            Java(<caret>)
                        }
                    """,
            javaDependencies = listOf(javaDependency)
        )
    }

    fun `test call java method`() {
        val javaDependency = """
            public class Java {
                public Java(String str) {
                }
            
                public void foo(Java java) {
                }
            }
        """
        doUnavailableTest(
            before = """
                        fun test() {
                            Java("").foo(<caret>)
                        }
                    """,
            javaDependencies = listOf(javaDependency)
        )
    }

    fun `test fill super type call entry`() {
        doAvailableTest(
            before = """
                        open class C(p1: Int, p2: Int)
                        class D : C(<caret>)
                    """,
            after = """
                        open class C(p1: Int, p2: Int)
                        class D : C(p1 = 0, p2 = 0)
                    """
        )
    }

    fun `test fill class constructor without default values`() {
        doAvailableTest(
            before = """
                        class User(val name: String, val age: Int)
                        fun test() {
                            User(<caret>)
                        }
                    """,
            after = """
                        class User(val name: String, val age: Int)
                        fun test() {
                            User(name =, age =)
                        }
                    """,
            withoutDefaultValues = true
        )
    }

    fun `test do not fill default arguments`() {
        doAvailableTest(
            before = """
                        class User(val name: String, val age: Int = 0)
                        fun test() {
                            User(<caret>)
                        }
                    """,
            after = """
                        class User(val name: String, val age: Int = 0)
                        fun test() {
                            User(name = "")
                        }
                    """,
            withoutDefaultArguments = true
        )
    }

    fun `test fill lambda arguments`() {
        val dependency = """
            package foo
            class A
            class B(f1: () -> Unit, f2: (Int) -> String, f3: (Int, String?, A) -> String)
        """.trimIndent()

        doAvailableTest(
            """
            import foo.B
            
            fun test() {
                B(<caret>)
            }
        """, """
            import foo.A
            import foo.B
            
            fun test() {
                B(f1 = {}, f2 = {}, f3 = { i: Int, s: String?, a: A -> })
            }
        """, withoutDefaultArguments = true, dependencies = listOf(dependency)
        )
    }

    private fun doAvailableTest(
        before: String,
        after: String,
        problemDescription: String = CONSTRUCTOR_PROBLEM_TITLE,
        dependencies: List<String> = emptyList(),
        javaDependencies: List<String> = emptyList(),
        withoutDefaultValues: Boolean = false,
        withoutDefaultArguments: Boolean = false
    ) {
        val highlightInfo = doHighlighting(
            before, problemDescription, dependencies, javaDependencies, withoutDefaultValues, withoutDefaultArguments
        )
        check(highlightInfo != null) { "Problems should be detected at caret" }
        myFixture.launchAction(myFixture.findSingleIntention(problemDescription))
        myFixture.checkResult(after.trimIndent())
    }

    private fun doUnavailableTest(
        before: String,
        problemDescription: String = CONSTRUCTOR_PROBLEM_TITLE,
        dependencies: List<String> = emptyList(),
        javaDependencies: List<String> = emptyList(),
        withoutDefaultValues: Boolean = false,
        withoutDefaultArguments: Boolean = false
    ) {
        val highlightInfo = doHighlighting(
            before, problemDescription, dependencies, javaDependencies, withoutDefaultValues, withoutDefaultArguments
        )
        check(highlightInfo == null) { "No problems should be detected at caret" }
    }

    private fun doHighlighting(
        code: String,
        problemDescription: String = CONSTRUCTOR_PROBLEM_TITLE,
        dependencies: List<String>,
        javaDependencies: List<String>,
        withoutDefaultValues: Boolean,
        withoutDefaultArguments: Boolean
    ): HighlightInfo? {
        check("<caret>" in code) { "Please, add `<caret>` marker to\n$code" }

        dependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.kt", dependency.trimIndent())
        }
        javaDependencies.forEachIndexed { index, dependency ->
            myFixture.configureByText("dependency$index.java", dependency.trimIndent())
        }
        myFixture.configureByText(KotlinFileType.INSTANCE, code.trimIndent())

        val inspection = KotlinFunctionArgumentsHelperInspection(
            withoutDefaultValues = withoutDefaultValues,
            withoutDefaultArguments = withoutDefaultArguments
        )
        myFixture.enableInspections(inspection)

        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(project)
        val inspectionProfile = inspectionProfileManager.currentProfile
        val state = inspectionProfile.getToolDefaultState(inspection.shortName, project)
        state.level = HighlightDisplayLevel.WARNING

        val caretOffset = myFixture.caretOffset
        return myFixture.doHighlighting().singleOrNull {
            it.description == problemDescription && caretOffset in it.startOffset..it.endOffset
        }
    }
}
