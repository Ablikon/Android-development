import kotlin.test.Test
import kotlin.test.assertEquals

class P1768MergeStringsAlternatelyTest {

    @Test
    fun `equal lengths interleave completely`() =
        assertEquals("apbqcr", mergeAlternately("abc", "pqr"))

    @Test
    fun `the longer second word keeps its tail`() =
        assertEquals("apbqrs", mergeAlternately("ab", "pqrs"))

    @Test
    fun `the longer first word keeps its tail`() =
        assertEquals("apbqcd", mergeAlternately("abcd", "pq"))

    @Test
    fun `an empty word leaves the other untouched`() {
        assertEquals("abc", mergeAlternately("abc", ""))
        assertEquals("pqr", mergeAlternately("", "pqr"))
        assertEquals("", mergeAlternately("", ""))
    }

    @Test
    fun `single characters merge in order`() =
        assertEquals("ap", mergeAlternately("a", "p"))
}
