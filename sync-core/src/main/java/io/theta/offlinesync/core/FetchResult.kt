package io.theta.offlinesync.core

data class FetchResult(
    val changeJson: String,
    val newSinceToken: String?
)