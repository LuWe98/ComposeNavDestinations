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

    // works with Studio 4.1 or later; see com.android.tools.lint.detector.api.Api / ApiKt
    override val minApi: Int get() = 8

    override val vendor = Vendor(
        vendorName = "ComposeNavDestinations",
        feedbackUrl = "https://github.com/LuWe98/ComposeNavDestinations/issues",
        contact = "https://github.com/LuWe98/ComposeNavDestinations"
    )

}