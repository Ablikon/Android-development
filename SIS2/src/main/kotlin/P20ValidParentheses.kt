/** Every closing bracket mapped to the opening bracket it must cancel. */
private val CLOSING_TO_OPENING = mapOf(')' to '(', ']' to '[', '}' to '{')

/**
 * LeetCode 20. Valid Parentheses (Easy)
 * Topic: stack
 *
 * Approach: brackets nest, so the string is valid exactly when every closer cancels the most
 * recently seen opener - which is a stack. The whole scan is a single `fold` over a nullable
 * `ArrayDeque`, where `null` is the absorbing state meaning "already proven invalid", so a
 * mismatch simply poisons the accumulator instead of needing an early exit.
 */
fun isValid(s: String): Boolean =
    s.fold<ArrayDeque<Char>?>(ArrayDeque()) { stack, char ->
        val expectedOpener = CLOSING_TO_OPENING[char]
        when {
            stack == null -> null                                    // already invalid
            expectedOpener == null -> stack.apply { addLast(char) }  // an opener, push it
            stack.removeLastOrNull() == expectedOpener -> stack      // a closer that matches
            else -> null                                             // a closer that does not
        }
    }?.isEmpty() == true
