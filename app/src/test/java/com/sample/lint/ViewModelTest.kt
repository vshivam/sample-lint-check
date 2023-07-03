package com.sample.lint

import com.sample.annotations.TestId
import com.sample.annotations.TestReporter
import org.junit.Test

@TestReporter
class ViewModelTest {

    @Test
    @TestId(42)
    fun verifyBehavior() {
        // test
    }
}
