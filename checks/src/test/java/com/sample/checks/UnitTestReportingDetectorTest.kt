package com.sample.checks

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.sample.checks.UnitTestReporterDetector.Companion.EXPLANATION
import com.sample.checks.UnitTestReporterDetector.Companion.ID
import org.junit.Test

class UnitTestReportingDetectorTest {

    @Test
    fun `should be clean when test function is annotated with @TestId and parent class is annotated with @UnitTestReporter`() {
        // given
        val content =
            """
            package com.sample.feature
    
            import com.sample.annotations.TestReporter
            import com.sample.annotations.TestId
    
            @TestReporter
            class TestClass {
                
                @TestId
                fun sampleTest(){}
            }
            """
        val withReportingRuleFile = kotlin(content).indented()

        // when
        val actual = lint()
            .files(withReportingRuleFile, *ANNOTATION_STUBS)
            .issues(UnitTestReporterDetector.ISSUE)
            .run()

        // then
        actual.expectClean()
    }

    @Test
    fun `should show error when test function is annotated with @TestId but parent class is not annotated with @UnitTestReporter`() {
        // given
        val content =
            """
            package com.sample.feature
                            
            import com.sample.annotations.TestId
            
            class ViewModelTest {
                
                @TestId
                fun sampleTest(){}
            }
            """
        val withoutReportingRuleFile = kotlin(content).indented()

        // when
        val actual = lint()
            .files(withoutReportingRuleFile, *ANNOTATION_STUBS)
            .issues(UnitTestReporterDetector.ISSUE)
            .run()

        // then
        val expected =
            """
            src/com/sample/feature/ViewModelTest.kt:5: Error: $EXPLANATION [$ID]
            class ViewModelTest {
            ^
            1 errors, 0 warnings
            """
        actual
            .expectErrorCount(1)
            .expect(expected)
    }
}
