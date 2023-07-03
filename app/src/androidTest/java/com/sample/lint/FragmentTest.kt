package com.sample.lint

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.annotations.TestId

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentTest {

    @Test
    @TestId(42)
    fun verifyBehavior() {
        // test
    }
}
