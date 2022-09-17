package com.welu.compose_nav_destinations_lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.*

//https://googlesamples.github.io/android-custom-lint-rules/api-guide.html#writingalintcheck:basics/preliminaries/apistability
//https://github.com/googlesamples/android-custom-lint-rules

@Suppress("UnstableApiUsage")
class ComposeNavDestinationsIssueRegistry : IssueRegistry() {

    override val issues: List<Issue> = Issues.ISSUES

    override val api: Int get() = CURRENT_API

    override val minApi: Int get() = 8 // works with Studio 4.1 or later; see com.android.tools.lint.detector.api.Api / ApiKt

    // Requires lint API 30.0+; if you're still building for something
    // older, just remove this property.
    override val vendor: Vendor = Vendor(
        vendorName = "Android Open Source Project",
        feedbackUrl = "https://github.com/googlesamples/android-custom-lint-rules/issues",
        contact = "https://github.com/googlesamples/android-custom-lint-rules"
    )

}