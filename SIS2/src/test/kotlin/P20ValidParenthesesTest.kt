import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P20ValidParenthesesTest {

    @Test
    fun `a single matched pair is valid`() = assertTrue(isValid("()"))

    @Test
    fun `sibling pairs of every kind are valid`() = assertTrue(isValid("()[]{}"))

    @Test
    fun `nested pairs are valid`() = assertTrue(isValid("{[()]}"))

    @Test
    fun `crossed brackets are invalid`() = assertFalse(isValid("(]"))

    @Test
    fun `interleaved brackets are invalid`() = assertFalse(isValid("([)]"))

    @Test
    fun `an unclosed opener is invalid`() = assertFalse(isValid("("))

    @Test
    fun `a closer with nothing open is invalid`() = assertFalse(isValid(")"))

    @Test
    fun `a mismatch early does not get rescued later`() = assertFalse(isValid("(]()"))

    @Test
    fun `the empty string is valid`() = assertTrue(isValid(""))
}
