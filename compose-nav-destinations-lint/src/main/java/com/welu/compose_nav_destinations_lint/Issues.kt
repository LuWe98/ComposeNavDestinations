package com.welu.compose_nav_destinations_lint

import com.android.tools.lint.detector.api.*

@Suppress("UnstableApiUsage")
object Issues {

    val ANNOTATION_CHECK_ISSUE = Issue.create(
        id = "DestinationAnnotationCheck",
        briefDescription = "Lint Mentions",
        explanation = "DestinationAnnotationCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val IMPLEMENTATION_CHECK_ISSUE = Issue.create(
        id = "DestinationImplementationCheck",
        briefDescription = "Lint Mentions",
        explanation = "DestinationImplementationCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val NAV_DESTINATION_ANNOTATION_CLASS_TYPE_ISSUE = Issue.create(
        id = "DestinationAnnotationClassTypeCheck",
        briefDescription = "Lint Mentions",
        explanation = "DestinationAnnotationClassTypeCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val NAV_GRAPH_ANNOTATION_IS_START_PARAMETER_CHECK_ISSUE = Issue.create(
        id = "NavGraphAnnotationCheck",
        briefDescription = "Lint Mentions",
        explanation = "NavGraphAnnotationCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val NAV_GRAPH_ANNOTATED_COMPONENT_CHECK = Issue.create(
        id = "NavGraphComponentCheck",
        briefDescription = "Lint Mentions",
        explanation = "NavGraphComponentCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val NAV_GRAPH_MULTIPLE_ANNOTATION_CHECK = Issue.create(
        id = "ComponentInMultipleNavGraphsCheck",
        briefDescription = "Lint Mentions",
        explanation = "ComponentInMultipleNavGraphsCheck",
        category = Category.CORRECTNESS,
        priority = 8,
        severity = Severity.ERROR,
        implementation = Implementation(ComposeNavDestinationCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )

    val ISSUES = listOf(
        ANNOTATION_CHECK_ISSUE,
        IMPLEMENTATION_CHECK_ISSUE,
        NAV_GRAPH_ANNOTATION_IS_START_PARAMETER_CHECK_ISSUE,
        NAV_DESTINATION_ANNOTATION_CLASS_TYPE_ISSUE,
        NAV_GRAPH_MULTIPLE_ANNOTATION_CHECK,
        NAV_GRAPH_ANNOTATED_COMPONENT_CHECK
    )
}