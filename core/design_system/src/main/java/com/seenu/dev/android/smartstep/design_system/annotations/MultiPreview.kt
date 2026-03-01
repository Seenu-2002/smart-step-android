package com.seenu.dev.android.smartstep.design_system.annotations

import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Mobile w400", widthDp = 400, showBackground = true)
@Preview(name = "Tablet w1000", widthDp = 1000, showBackground = true)
annotation class MultiPreview()
