package com.sample.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class TestId(vararg val ids: Int = [])
