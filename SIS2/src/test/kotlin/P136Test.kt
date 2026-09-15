import kotlin.test.Test
import kotlin.test.assertEquals

class P136Test {
    @Test fun example1() = assertEquals(1, singleNumber(intArrayOf(2, 2, 1)))
    @Test fun example2() = assertEquals(4, singleNumber(intArrayOf(4, 1, 2, 1, 2)))
    @Test fun example3() = assertEquals(1, singleNumber(intArrayOf(1)))

    @Test
    fun negativesAndZero() {
        assertEquals(-5, singleNumber(intArrayOf(8, -5, 8)))
        assertEquals(0, singleNumber(intArrayOf(6, 0, 6)))
    }
}
