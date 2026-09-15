/**
 * LeetCode 136. Single Number (Easy)
 * Topic: bit manipulation
 *
 * Approach: XOR is associative and commutative, every value cancels itself (`x xor x == 0`), and
 * zero is its identity (`x xor 0 == x`). So folding XOR across the whole array annihilates each
 * pair regardless of order and leaves only the element that appears once - constant memory and a
 * single pass, with no auxiliary set.
 */
fun singleNumber(nums: IntArray): Int = nums.reduce(Int::xor)
