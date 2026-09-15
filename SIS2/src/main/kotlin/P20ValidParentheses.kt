// LeetCode 20. Valid Parentheses (Easy) - stack

private val closers = mapOf(')' to '(', ']' to '[', '}' to '{')

// Standard stack problem: every closing bracket has to cancel the last opener.
// I fold over a nullable stack, where null means "already broken", so one bad
// bracket poisons everything after it and I don't need to break out early.

fun isValid(s: String): Boolean =
    s.fold<ArrayDeque<Char>?>(ArrayDeque()) { stack, c ->
        val opener = closers[c]
        when {
            stack == null -> null
            opener == null -> stack.apply { addLast(c) }
            stack.removeLastOrNull() == opener -> stack
            else -> null
        }
    }?.isEmpty() == true
