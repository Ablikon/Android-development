import kotlin.test.Test
import kotlin.test.assertEquals

class P136SingleNumberTest {

    @Test
    fun `finds the lone value in the middle`() = assertEquals(1, singleNumber(intArrayOf(2, 2, 1)))

    @Test
    fun `finds the lone value among several pairs`() =
        assertEquals(4, singleNumber(intArrayOf(4, 1, 2, 1, 2)))

    @Test
    fun `a single element array returns it`() = assertEquals(1, singleNumber(intArrayOf(1)))

    @Test
    fun `order does not matter`() =
        assertEquals(99, singleNumber(intArrayOf(3, 99, 7, 3, 7)))

    @Test
    fun `negative numbers work too`() =
        assertEquals(-5, singleNumber(intArrayOf(8, -5, 8)))

    @Test
    fun `zero can be the lone value`() = assertEquals(0, singleNumber(intArrayOf(6, 0, 6)))
}
