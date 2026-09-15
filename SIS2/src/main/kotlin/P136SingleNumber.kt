// LeetCode 136. Single Number (Easy) - bit manipulation

// XOR cancels equal numbers (x xor x == 0) and leaves the rest alone (x xor 0 == x),
// so xoring the whole array kills every pair and only the lonely one survives.
// Order doesn't matter since XOR is commutative. O(1) memory, no extra set.

fun singleNumber(nums: IntArray): Int = nums.reduce(Int::xor)
