package kz.nazar.attention

import kz.nazar.attention.analysis.AttentionAnalyzer
import kz.nazar.attention.analysis.EventType
import kz.nazar.attention.analysis.UsageEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The analysis is exercised entirely on synthetic event streams, so none of this needs a phone, an
 * emulator, or a day of real usage to accumulate. A stream exported from a real device drops in as
 * one more fixture.
 */
class AttentionAnalyzerTest {

    private val analyzer = AttentionAnalyzer()

    private fun open(pkg: String, atMs: Long) = UsageEvent(atMs, pkg, EventType.AppOpened)
    private fun closed(pkg: String, atMs: Long) = UsageEvent(atMs, pkg, EventType.AppClosed)
    private fun screenOff(atMs: Long) = UsageEvent(atMs, null, EventType.ScreenOff)

    /** A session long enough and broken up enough to qualify as a drift chain. */
    private fun chain(entry: String, other: String, startMs: Long) = listOf(
        open(entry, startMs),
        open(other, startMs + 60_000),
        open(entry, startMs + 120_000),
        open(other, startMs + 180_000),
        screenOff(startMs + 1_000_000),
    )

    @Test
    fun `an empty stream yields nothing`() {
        assertTrue(analyzer.sessions(emptyList()).isEmpty())
        assertTrue(analyzer.chains(emptyList()).isEmpty())
        assertTrue(analyzer.entryPoints(emptyList()).isEmpty())
        assertNull(analyzer.longestFocus(emptyList()))
    }

    @Test
    fun `a single app stay is one session with no switches`() {
        val events = listOf(open("a", 0), screenOff(30_000))
        val sessions = analyzer.sessions(events)
        assertEquals(1, sessions.size)
        assertEquals(0, sessions.first().switchCount)
        assertEquals("a", sessions.first().entryPackage)
        assertEquals(30_000L, sessions.first().durationMs)
    }

    @Test
    fun `apps used back to back belong to the same session`() {
        val events = listOf(open("a", 0), open("b", 10_000), screenOff(20_000))
        val sessions = analyzer.sessions(events)
        assertEquals(1, sessions.size)
        assertEquals(1, sessions.first().switchCount)
    }

    @Test
    fun `a long idle gap starts a new session`() {
        val events = listOf(
            open("a", 0),
            closed("a", 5_000),
            open("b", 125_000),
            screenOff(130_000),
        )
        assertEquals(2, analyzer.sessions(events).size)
    }

    @Test
    fun `the screen going off ends the session even without a long gap`() {
        val events = listOf(open("a", 0), screenOff(5_000), open("b", 6_000), screenOff(10_000))
        assertEquals(2, analyzer.sessions(events).size)
    }

    @Test
    fun `a stay too short to be attention is discarded`() {
        val events = listOf(open("a", 0), open("b", 500), open("c", 10_000), screenOff(20_000))
        val uses = analyzer.appUses(events)
        assertEquals(2, uses.size)
        assertTrue(uses.none { it.packageName == "a" })
    }

    @Test
    fun `a long fragmented session is a drift chain`() {
        val chains = analyzer.chains(chain(entry = "insta", other = "tiktok", startMs = 0))
        assertEquals(1, chains.size)
        assertEquals("insta", chains.first().entryPackage)
        assertEquals(1_000_000L, chains.first().durationMs)
    }

    @Test
    fun `a long but unbroken session is focus, not a chain`() {
        val events = listOf(open("ide", 0), screenOff(1_000_000))
        assertTrue(analyzer.chains(events).isEmpty())
    }

    @Test
    fun `a short fragmented session is not a chain`() {
        val events = listOf(
            open("a", 0),
            open("b", 10_000),
            open("a", 20_000),
            open("b", 30_000),
            screenOff(40_000),
        )
        assertTrue(analyzer.chains(events).isEmpty())
    }

    @Test
    fun `entry points are ranked by the time lost to the chains they start`() {
        val events = chain("insta", "tiktok", 0) +
            chain("insta", "tiktok", 10_000_000) +
            chain("youtube", "tiktok", 20_000_000)

        val ranking = analyzer.entryPoints(events)
        assertEquals(2, ranking.size)
        assertEquals("insta", ranking[0].packageName)
        assertEquals(2, ranking[0].chainCount)
        assertEquals(2_000_000L, ranking[0].totalLostMs)
        assertEquals(1_000_000L, ranking[0].averageLostMs)
        assertEquals("youtube", ranking[1].packageName)
        assertEquals(1, ranking[1].chainCount)
    }

    @Test
    fun `the transition graph counts each app to app edge`() {
        val events = listOf(
            open("a", 0),
            open("b", 10_000),
            open("a", 20_000),
            open("b", 30_000),
            screenOff(40_000),
        )
        val transitions = analyzer.transitions(events)
        assertEquals(2, transitions.size)
        assertEquals("a", transitions[0].fromPackage)
        assertEquals("b", transitions[0].toPackage)
        assertEquals(2, transitions[0].count)
        assertEquals(1, transitions[1].count)
    }

    @Test
    fun `the dominant app is the one that held attention longest, not the most reopened`() {
        val events = listOf(
            open("a", 0),
            open("b", 10_000),
            open("a", 20_000),
            screenOff(100_000),
        )
        assertEquals("a", analyzer.sessions(events).first().dominantPackage)
    }

    @Test
    fun `the longest single stay is reported as focus`() {
        val events = listOf(
            open("a", 0),
            open("b", 10_000),
            open("a", 20_000),
            screenOff(100_000),
        )
        val focus = analyzer.longestFocus(events)
        assertEquals("a", focus!!.packageName)
        assertEquals(80_000L, focus.durationMs)
    }

    @Test
    fun `fragmentation is expressed as switches per minute`() {
        // Two switches across a two-minute session.
        val events = listOf(
            open("a", 0),
            open("b", 40_000),
            open("c", 80_000),
            screenOff(120_000),
        )
        assertEquals(1.0, analyzer.sessions(events).first().fragmentation, 0.0001)
    }

    @Test
    fun `events arriving out of order are still analysed correctly`() {
        val ordered = listOf(open("a", 0), open("b", 10_000), screenOff(20_000))
        val shuffled = listOf(screenOff(20_000), open("b", 10_000), open("a", 0))
        assertEquals(analyzer.appUses(ordered), analyzer.appUses(shuffled))
    }

    @Test
    fun `an invalid idle gap is rejected`() {
        try {
            AttentionAnalyzer(idleGapMs = 0)
            error("expected an IllegalArgumentException")
        } catch (expected: IllegalArgumentException) {
            assertTrue(expected.message!!.contains("idleGapMs"))
        }
    }
}
