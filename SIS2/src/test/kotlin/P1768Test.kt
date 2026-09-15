import kotlin.test.Test
import kotlin.test.assertEquals

class P1768Test {
    @Test fun example1() = assertEquals("apbqcr", mergeAlternately("abc", "pqr"))
    @Test fun example2() = assertEquals("apbqrs", mergeAlternately("ab", "pqrs"))
    @Test fun example3() = assertEquals("apbqcd", mergeAlternately("abcd", "pq"))

    @Test
    fun emptyInput() {
        assertEquals("abc", mergeAlternately("abc", ""))
        assertEquals("pqr", mergeAlternately("", "pqr"))
    }
}
