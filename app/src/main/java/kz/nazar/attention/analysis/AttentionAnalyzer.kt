package kz.nazar.attention.analysis

/**
 * Turns a raw system usage-event stream into attention sessions, drift chains and a ranking of the
 * apps that start them.
 *
 * Everything here is a pure function over a list of [UsageEvent]. That is the whole point: the
 * interesting part of this app is the analysis, and it is testable on the JVM against synthetic or
 * exported real event streams, with no device, no emulator and no waiting for a day of usage.
 *
 * @param idleGapMs a pause longer than this ends the current session.
 * @param minUseMs stays shorter than this are treated as pass-through, not as attention.
 * @param chainMinDurationMs a session must last at least this long to count as a drift chain.
 * @param chainMinSwitches a session must contain at least this many app switches to be a chain.
 */
class AttentionAnalyzer(
    private val idleGapMs: Long = 60_000L,
    private val minUseMs: Long = 1_000L,
    private val chainMinDurationMs: Long = 15 * 60_000L,
    private val chainMinSwitches: Int = 3,
) {
    init {
        require(idleGapMs > 0) { "idleGapMs must be positive" }
        require(minUseMs >= 0) { "minUseMs must not be negative" }
        require(chainMinDurationMs > 0) { "chainMinDurationMs must be positive" }
        require(chainMinSwitches >= 0) { "chainMinSwitches must not be negative" }
    }

    /**
     * Reconstructs the app stays from the event stream. An app stay is closed by the next app
     * opening, by an explicit close of that app, or by the screen going off.
     */
    fun appUses(events: List<UsageEvent>): List<AppUse> {
        val ordered = events.sortedBy { it.timestampMs }
        val uses = mutableListOf<AppUse>()
        var openPackage: String? = null
        var openedAt = 0L

        fun close(at: Long) {
            val pkg = openPackage ?: return
            if (at - openedAt >= minUseMs) uses += AppUse(pkg, openedAt, at)
            openPackage = null
        }

        for (event in ordered) {
            when (event.type) {
                EventType.AppOpened -> {
                    val pkg = event.packageName ?: continue
                    close(event.timestampMs)
                    openPackage = pkg
                    openedAt = event.timestampMs
                }
                EventType.AppClosed -> {
                    if (event.packageName == openPackage) close(event.timestampMs)
                }
                EventType.ScreenOff -> close(event.timestampMs)
                EventType.ScreenOn -> Unit
            }
        }
        return uses
    }

    /**
     * Groups app stays into sessions. A screen-off event or a gap longer than [idleGapMs] starts a
     * new session.
     */
    fun sessions(events: List<UsageEvent>): List<AttentionSession> {
        val uses = appUses(events)
        if (uses.isEmpty()) return emptyList()

        val screenOffTimes = events
            .filter { it.type == EventType.ScreenOff }
            .map { it.timestampMs }
            .sorted()

        val sessions = mutableListOf<AttentionSession>()
        var current = mutableListOf(uses.first())
        for (use in uses.drop(1)) {
            val previous = current.last()
            val gap = use.startMs - previous.endMs
            val screenWentOff = screenOffTimes.any { it in previous.endMs..use.startMs }
            if (gap > idleGapMs || screenWentOff) {
                sessions += AttentionSession(current.toList())
                current = mutableListOf(use)
            } else {
                current += use
            }
        }
        sessions += AttentionSession(current.toList())
        return sessions
    }

    /** Sessions that were long enough and broken up enough to count as lost time. */
    fun chains(events: List<UsageEvent>): List<DriftChain> =
        sessions(events)
            .filter { it.durationMs >= chainMinDurationMs && it.switchCount >= chainMinSwitches }
            .map { DriftChain(it) }

    /**
     * Ranks the apps that open drift chains, worst first. This is the app's actual answer to
     * "where does my evening go": not which app you used most, but which one let the evening go.
     */
    fun entryPoints(events: List<UsageEvent>): List<EntryPoint> =
        chains(events)
            .groupBy { it.entryPackage }
            .map { (pkg, group) ->
                EntryPoint(
                    packageName = pkg,
                    chainCount = group.size,
                    totalLostMs = group.sumOf { it.durationMs },
                )
            }
            .sortedWith(compareByDescending<EntryPoint> { it.totalLostMs }.thenBy { it.packageName })

    /** The app-to-app transition graph across every session, most travelled edge first. */
    fun transitions(events: List<UsageEvent>): List<Transition> =
        sessions(events)
            .flatMap { session -> session.uses.zipWithNext() }
            .filter { (a, b) -> a.packageName != b.packageName }
            .groupingBy { (a, b) -> a.packageName to b.packageName }
            .eachCount()
            .map { (edge, count) -> Transition(edge.first, edge.second, count) }
            .sortedWith(
                compareByDescending<Transition> { it.count }
                    .thenBy { it.fromPackage }
                    .thenBy { it.toPackage },
            )

    /** The longest single stay in one app, which is the closest thing to real focus. */
    fun longestFocus(events: List<UsageEvent>): AppUse? = appUses(events).maxByOrNull { it.durationMs }
}
