import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P20Test {
    @Test fun example1() = assertTrue(isValid("()"))
    @Test fun example2() = assertTrue(isValid("()[]{}"))
    @Test fun example3() = assertFalse(isValid("(]"))
    @Test fun nested() = assertTrue(isValid("{[()]}"))

    @Test
    fun brokenOnes() {
        assertFalse(isValid("([)]"))
        assertFalse(isValid("("))
        assertFalse(isValid(")"))
        // a valid pair later must not rescue an earlier mismatch
        assertFalse(isValid("(]()"))
    }
}
