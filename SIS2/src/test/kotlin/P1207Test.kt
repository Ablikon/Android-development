import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P1207Test {
    @Test fun example1() = assertTrue(uniqueOccurrences(intArrayOf(1, 2, 2, 1, 1, 3)))
    @Test fun example2() = assertFalse(uniqueOccurrences(intArrayOf(1, 2)))
    @Test fun example3() =
        assertTrue(uniqueOccurrences(intArrayOf(-3, 0, 1, -3, 1, 1, 1, -3, 10, 0)))

    @Test fun negativesCountToo() = assertFalse(uniqueOccurrences(intArrayOf(-1, -1, -2, -2)))
}
