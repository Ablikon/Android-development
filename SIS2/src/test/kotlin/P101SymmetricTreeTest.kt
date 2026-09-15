import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class P101SymmetricTreeTest {

    /** Builds a tree from a level-order list, using null for a missing child, as LeetCode does. */
    private fun treeOf(vararg levelOrder: Int?): TreeNode? {
        if (levelOrder.isEmpty() || levelOrder[0] == null) return null
        val root = TreeNode(levelOrder[0]!!)
        val queue = ArrayDeque(listOf(root))
        var index = 1
        while (queue.isNotEmpty() && index < levelOrder.size) {
            val node = queue.removeFirst()
            levelOrder.getOrNull(index++)?.let { node.left = TreeNode(it).also(queue::addLast) }
            levelOrder.getOrNull(index++)?.let { node.right = TreeNode(it).also(queue::addLast) }
        }
        return root
    }

    @Test
    fun `a mirrored tree is symmetric`() =
        assertTrue(isSymmetric(treeOf(1, 2, 2, 3, 4, 4, 3)))

    @Test
    fun `matching shape with mismatched values is not symmetric`() =
        assertFalse(isSymmetric(treeOf(1, 2, 2, null, 3, null, 3)))

    @Test
    fun `an empty tree is symmetric`() = assertTrue(isSymmetric(null))

    @Test
    fun `a single node is symmetric`() = assertTrue(isSymmetric(treeOf(1)))

    @Test
    fun `equal values in an asymmetric shape are not symmetric`() =
        assertFalse(isSymmetric(treeOf(1, 2, 2, 2, null, 2)))

    @Test
    fun `deep symmetry is detected`() =
        assertTrue(isSymmetric(treeOf(1, 2, 2, 3, 4, 4, 3, 5, 6, 7, 8, 8, 7, 6, 5)))
}
