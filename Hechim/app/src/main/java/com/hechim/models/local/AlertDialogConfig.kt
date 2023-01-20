package com.hechim.models.local

data class AlertDialogConfig(
    val title: String,
    val description: String,
    val positiveButtonText: String,
    val negativeButtonText: String,
    val positiveButtonCallback: () -> Unit,
    val negativeButtonCallback: () -> Unit
)
