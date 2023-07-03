package com.sample.checks

import com.android.tools.lint.checks.infrastructure.TestFiles

private val testIdAnnotationStub =
    TestFiles.kotlin(
        """
        package com.sample.annotations
    
        annotation class TestId

        """
    ).indented()

private val testReporterAnnotationStub =
    TestFiles.kotlin(
        """
        package com.sample.annotations

        annotation class TestReporter

        """
    ).indented()

val ANNOTATION_STUBS = arrayOf(testIdAnnotationStub, testReporterAnnotationStub)
