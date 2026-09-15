import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P1207UniqueNumberOfOccurrencesTest {

    @Test
    fun `distinct frequencies are unique`() =
        assertTrue(uniqueOccurrences(intArrayOf(1, 2, 2, 1, 1, 3)))

    @Test
    fun `two values sharing a frequency are not unique`() =
        assertFalse(uniqueOccurrences(intArrayOf(1, 2)))

    @Test
    fun `a longer mixed case is unique`() =
        assertTrue(uniqueOccurrences(intArrayOf(-3, 0, 1, -3, 1, 1, 1, -3, 10, 0)))

    @Test
    fun `a single element is trivially unique`() = assertTrue(uniqueOccurrences(intArrayOf(7)))

    @Test
    fun `all elements equal gives one frequency`() =
        assertTrue(uniqueOccurrences(intArrayOf(5, 5, 5, 5)))

    @Test
    fun `negative values are counted like any other`() =
        assertFalse(uniqueOccurrences(intArrayOf(-1, -1, -2, -2)))
}
