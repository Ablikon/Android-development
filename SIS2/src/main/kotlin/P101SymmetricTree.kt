/**
 * The node type LeetCode supplies for binary-tree problems, reproduced verbatim - including the
 * backticked `val` field name, which is a Kotlin keyword and so has to be escaped.
 */
class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

/**
 * LeetCode 101. Symmetric Tree (Easy)
 * Topic: recursion / binary trees
 *
 * Approach: a tree is a mirror of itself when its two subtrees mirror each other, which turns a
 * one-tree question into a two-tree one and makes the recursion fall out. Two subtrees mirror when
 * their roots hold the same value and each one's left child mirrors the other's right child. The
 * base case is `a == b`, which holds only when both sides ran out at the same time.
 */
fun isSymmetric(root: TreeNode?): Boolean =
    root == null || mirrors(root.left, root.right)

private fun mirrors(a: TreeNode?, b: TreeNode?): Boolean = when {
    a == null || b == null -> a == b
    else -> a.`val` == b.`val` && mirrors(a.left, b.right) && mirrors(a.right, b.left)
}
