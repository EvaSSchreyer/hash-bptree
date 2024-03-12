import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InnerNodeTest {

    Monoid<Integer, Integer> monoid = new CountingMonoid<>();
    @Test
    void getLeftSibling() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);//expected structure:      (3)     (5)
        tree.insert(4,44);//                     (2)    (4)     (6, 7)
        tree.insert(5,55);//                  (1) (2) (3) (4) (5) (6) (7,8)
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);

        assertEquals(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1), ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(2).getLeftSibling());

        tree.insert(9,99);
        assertEquals(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0), ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1).getLeftSibling());
        assertNull(((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getLeftSibling());
    }

    @Test
    void getRightSibling() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);//expected structure:      (3)     (5)
        tree.insert(4,44);//                     (2)    (4)     (6, 7)
        tree.insert(5,55);//                  (1) (2) (3) (4) (5) (6) (7,8)
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);

        assertEquals(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(2), ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1).getRightSibling());

        tree.insert(9,99);
        assertEquals(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1), ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0).getRightSibling());
        assertNull(((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getLeftSibling());
    }
}