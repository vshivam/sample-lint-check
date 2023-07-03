@file:Suppress("UnstableApiUsage")

package com.sample.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.kotlin.KotlinUClass
import java.util.EnumSet

internal class UnitTestReporterDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return VerifyTestReporterAnnotationHandler(issue = ISSUE, context)
    }

    companion object {
        const val ID = "UnitTestReportingIssue"
        val EXPLANATION =
            """
            Unit test classes containing tests annotated with @TestId should be annotated with @TestReporter. Without this annotation, test results will not be reported.   
            """.trimIndent()
        private const val BRIEF_DESCRIPTION = "Use @TestReporter"

        val ISSUE: Issue = Issue.create(
            id = ID,
            briefDescription = BRIEF_DESCRIPTION,
            explanation = EXPLANATION,
            priority = 10,
            severity = Severity.ERROR,
            implementation = Implementation(
                UnitTestReporterDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )
    }
}

class VerifyTestReporterAnnotationHandler(
    private val issue: Issue,
    private val context: JavaContext,
) : UElementHandler() {

    override fun visitClass(node: UClass) {
        if (node.isInnerClass()) return

        if (node.hasMethodsAnnotatedWithTestId() &&
            !node.hasAnnotation(REPORTER_ANNOTATION)
        ) {
            val fix = context.createLintFix(node)
            context.report(issue, node, fix)
        }
    }

    private fun JavaContext.createLintFix(node: UClass) =
        LintFix.create()
            .annotate(REPORTER_ANNOTATION)
            .range(getNameLocation(node))
            .build()

    private fun JavaContext.report(issue: Issue, node: UElement, fix: LintFix? = null) =
        report(
            issue = issue,
            scope = node,
            location = getLocation(node),
            message = issue.getExplanation(TextFormat.TEXT),
            quickfixData = fix
        )

    private fun UClass.isInnerClass() = uastParent is KotlinUClass

    private fun UClass.hasMethodsAnnotatedWithTestId(): Boolean =
        innerClasses.find { it.hasMethodsAnnotatedWithTestId() } != null ||
                methods.firstOrNull { it.hasAnnotation(TEST_ID_ANNOTATION) } != null

    private companion object {
        const val TEST_ID_ANNOTATION = "com.sample.annotations.TestId"
        const val REPORTER_ANNOTATION = "com.sample.annotations.TestReporter"
    }
}
