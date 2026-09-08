package kz.nazar.attention.analysis

/** The kinds of system usage events the analysis cares about. */
enum class EventType { AppOpened, AppClosed, ScreenOn, ScreenOff }

/**
 * One system usage event. Mirrors the subset of [android.app.usage.UsageEvents.Event] the app
 * reads, but carries no Android type, so the whole analysis runs on the JVM in a unit test.
 */
data class UsageEvent(
    val timestampMs: Long,
    val packageName: String?,
    val type: EventType,
)

/** A single continuous stay in one app. */
data class AppUse(
    val packageName: String,
    val startMs: Long,
    val endMs: Long,
) {
    val durationMs: Long get() = endMs - startMs
}

/**
 * A stretch of continuous phone use. A session ends when the screen goes off or when the phone is
 * left alone for longer than the configured idle gap.
 */
data class AttentionSession(
    val uses: List<AppUse>,
) {
    val startMs: Long get() = uses.first().startMs
    val endMs: Long get() = uses.last().endMs
    val durationMs: Long get() = endMs - startMs

    /** The app that opened the session - the candidate entry point of a drift chain. */
    val entryPackage: String get() = uses.first().packageName

    /** Number of times attention moved from one app to a different one. */
    val switchCount: Int get() = uses.zipWithNext().count { (a, b) -> a.packageName != b.packageName }

    /** The app that held attention longest in this session. */
    val dominantPackage: String
        get() = uses.groupBy { it.packageName }
            .mapValues { (_, group) -> group.sumOf { it.durationMs } }
            .maxByOrNull { it.value }!!
            .key

    /** Switches per minute. Higher means attention was more badly broken up. */
    val fragmentation: Double
        get() = if (durationMs <= 0L) 0.0 else switchCount * 60_000.0 / durationMs
}

/** A session long and broken enough to count as time lost rather than time spent. */
data class DriftChain(
    val session: AttentionSession,
) {
    val entryPackage: String get() = session.entryPackage
    val durationMs: Long get() = session.durationMs
}

/** How much a single app costs, aggregated over every chain it started. */
data class EntryPoint(
    val packageName: String,
    val chainCount: Int,
    val totalLostMs: Long,
) {
    val averageLostMs: Long get() = if (chainCount == 0) 0L else totalLostMs / chainCount
}

/** One edge of the app-to-app transition graph. */
data class Transition(
    val fromPackage: String,
    val toPackage: String,
    val count: Int,
)
