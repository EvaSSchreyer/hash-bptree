import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Important Note: set static degree variable in Tree.java to "2"!!
 */
class TreeDegree2Test {
    Monoid<Integer, Integer> monoid = new CountingMonoid<>();

    @Test
    void shouldContainKey() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);//expected structure:      (3)     (5)
        tree.insert(4,44);//                     (2)    (4)     (6, 7)
        tree.insert(5,55);//                  (1) (2) (3) (4) (5) (6) (7,8)
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);

        LeafNode<Integer, Integer, Integer> found1 = tree.shouldContainKey(5);
        assertEquals(5, found1.keys.get(0));
        assertEquals(55, found1.getValue(0));

        LeafNode<Integer, Integer, Integer> found2 = tree.shouldContainKey(9);
        assertEquals(7, found2.keys.get(0));
        assertEquals(77, found2.getValue(0));
        assertEquals(8, found2.keys.get(1));
        assertEquals(88, found2.getValue(1));

        LeafNode<Integer, Integer, Integer> found3= tree.shouldContainKey(0);
        assertEquals(1, found3.keys.get(0));
        assertEquals(11, found3.getValue(0));
    }

    @Test
    void insertRoot() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        //insert an element in empty tree
        tree.insert(1,11);
        assertNotNull(tree.getRoot());
        assertEquals(NodeType.LeafNode, tree.getRoot().getNodeType());
        assertEquals(1, (int) tree.getRoot().keys.get(0));
        assertEquals(11, ((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getValue(0));
        assertEquals(monoid, tree.getRoot().monoid);
        assertEquals(1, tree.getRoot().label);
        assertNull(tree.getRoot().parent);
    }

    @Test
    void insertSplit() {
        //1. make new root
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        //insert an element in empty tree
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);//split, if degree = 2
        // root
        assertNotNull(tree.getRoot());
        assertEquals(NodeType.InnerNode, tree.getRoot().getNodeType());
        assertNotNull(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0));
        assertNotNull(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1));
        assertEquals(2, (int) tree.getRoot().keys.get(0));
        assertEquals(monoid, tree.getRoot().monoid);
        assertEquals(3, tree.getRoot().label);
        assertNull(tree.getRoot().parent);

        //leftChild
        LeafNode<Integer, Integer, Integer> leafFirstChild = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0);
        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0).getNodeType());
        assertEquals(1, (int) leafFirstChild.keys.get(0));
        assertEquals(11, leafFirstChild.getValue(0));
        assertEquals(monoid, leafFirstChild.monoid);
        assertEquals(1, leafFirstChild.label);
        //rightChild
        LeafNode<Integer, Integer, Integer> leafSecondChild = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(1);
        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(1).getNodeType());
        assertEquals(2, (int) leafSecondChild.keys.get(0));
        assertEquals(22, leafSecondChild.getValue(0));
        assertEquals(3, (int) leafSecondChild.keys.get(1));
        assertEquals(33, leafSecondChild.getValue(1));
        assertEquals(monoid, leafSecondChild.monoid);
        assertEquals(2, leafSecondChild.label);
        
        //2. split child node and add new key+child to root
        tree.insert(4,44);
        tree.insert(5,55);
        /*expected structure:      3
         *                     2       4
         *                  (1) (2) (3) (4,5)
         */
        //root
        assertEquals(5, tree.getRoot().label);
        assertEquals(NodeType.InnerNode, tree.getRoot().getNodeType());
        assertNotNull(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0));
        assertNotNull(((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1));
        assertEquals(1, tree.getRoot().keys.size());
        assertEquals(3, (int) tree.getRoot().keys.get(0));
        
        //new children
        InnerNode<Integer, Integer, Integer> firstChild = (InnerNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0);
        assertEquals(NodeType.InnerNode, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0).getNodeType());
        assertEquals(1, firstChild.keys.size());
        assertEquals(2, (int) firstChild.keys.get(0));
        assertEquals(2, firstChild.label);

        InnerNode<Integer, Integer, Integer> secondChild = (InnerNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(1);
        assertEquals(1, secondChild.keys.size());
        assertEquals(4, (int) secondChild.keys.get(0));
        assertEquals(3, secondChild.label);

        leafFirstChild = (LeafNode<Integer, Integer, Integer>) firstChild.getChild(0);
        assertEquals(NodeType.LeafNode, firstChild.getChild(0).getNodeType());
        assertEquals(1, leafFirstChild.keys.size());
        assertEquals(1, (int) leafFirstChild.keys.get(0));
        assertEquals(1, leafFirstChild.label);
        assertEquals(firstChild, leafFirstChild.getParent());

        LeafNode<Integer, Integer, Integer> leafLastChild = (LeafNode<Integer, Integer, Integer>) secondChild.getChild(1);
        assertEquals(NodeType.LeafNode, secondChild.getChild(0).getNodeType());
        assertEquals(2, leafLastChild.keys.size());
        assertEquals(4, (int) leafLastChild.keys.get(0));
        assertEquals(5, (int) leafLastChild.keys.get(1));
        assertEquals(2, leafLastChild.label);
        assertEquals(secondChild, leafLastChild.getParent());


        //3. split parent = InnerNode
        tree.insert(6,66);
        /*expected structure:      3
         *                     2       (4,   5)
         *                  (1) (2) (3) (4) (5,6)
         */
        tree.insert(7,77);
        /*expected structure:      3       5
         *                     2       4       6
         *                  (1) (2) (3) (4) (5) (6,7)
         */
        //root
        assertEquals(7, tree.getRoot().label);
        assertEquals(2, tree.getRoot().keys.size());
        assertEquals(3, (int) tree.getRoot().keys.get(0));
        assertEquals(5, (int) tree.getRoot().keys.get(1));

        //else
        assertEquals(1, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0).keys.size());
        assertEquals(tree.getRoot(), ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(0).getParent());
        assertEquals(1, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(1).keys.size());
        assertEquals(1, ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(2).keys.size());
    }

    @Test
    void delete() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);//expected structure:      (3      5)
        tree.insert(4,44);//                     (2)    (4)     (6, 7)
        tree.insert(5,55);//                  (1) (2) (3) (4) (5) (6) (7,8)
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);

        InnerNode<Integer, Integer, Integer> lastChild = (InnerNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(2);
        assertEquals(4, lastChild.label);
        //1. Borrow rightChild
        tree.delete(6);//new structure: (3,5); (2) (4) (7,8); (1) (2). (3) (4). (5) (7) (8)
        assertEquals(7, lastChild.keys.get(0));
        assertEquals(8, lastChild.keys.get(1));
        assertEquals(2, lastChild.keys.size());
        assertEquals(3, lastChild.label);

        Node<Integer, Integer, Integer> leafFirstLastChild = lastChild.getChild(0);
        assertEquals(NodeType.LeafNode, leafFirstLastChild.getNodeType());
        assertEquals(5, leafFirstLastChild.keys.get(0));
        assertEquals(1, leafFirstLastChild.keys.size());
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafFirstLastChild).getSizeValues());
        assertEquals(1, leafFirstLastChild.label);

        Node<Integer, Integer, Integer> leafSecondLastChild = lastChild.getChild(1);
        assertEquals(NodeType.LeafNode, leafSecondLastChild.getNodeType());
        assertEquals(7, leafSecondLastChild.keys.get(0));
        assertEquals(1, leafSecondLastChild.keys.size());
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafSecondLastChild).getSizeValues());
        assertEquals(1, leafSecondLastChild.label);

        Node<Integer, Integer, Integer> leafLastChild = lastChild.getChild(2);
        assertEquals(NodeType.LeafNode, leafLastChild.getNodeType());
        assertEquals(8, leafLastChild.keys.get(0));
        assertEquals(1, leafLastChild.keys.size());
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafLastChild).getSizeValues());
        assertEquals(1, leafLastChild.label);

        //2. Borrow leftChild
        tree.insert(6, 66);//new structure: (3,5); (2) (4) (7,8); (1) (2). (3) (4). (5,6) (7) (8)
        tree.delete(7);//new structure: (3,5); (2) (4) (6,8); (1) (2). (3) (4). (5) (6) (8)
        assertEquals(6, lastChild.keys.get(0));
        assertEquals(8, lastChild.keys.get(1));
        assertEquals(2, lastChild.keys.size());
        assertEquals(3, lastChild.label);

        assertEquals(5, leafFirstLastChild.keys.get(0));
        assertEquals(1, leafFirstLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(0, leafFirstLastChild.indexInParent);
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafFirstLastChild).getSizeValues());

        assertEquals(6, leafSecondLastChild.keys.get(0));
        assertEquals(1, leafSecondLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(1, leafSecondLastChild.indexInParent);
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafSecondLastChild).getSizeValues());

        assertEquals(8, leafLastChild.keys.get(0));
        assertEquals(1, leafLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(2, leafLastChild.indexInParent);
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafLastChild).getSizeValues());

        //3. Fusion LeafNode
        tree.delete(6);//new structure: (3,5); (2) (4) (8); (1) (2). (3) (4). (5) (8)
        assertEquals(8, lastChild.keys.get(0));
        assertEquals(1, lastChild.keys.size());
        assertEquals(2, lastChild.label);

        assertEquals(5, leafFirstLastChild.keys.get(0));
        assertEquals(1, leafFirstLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(0, leafFirstLastChild.indexInParent);
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafFirstLastChild).getSizeValues());

        assertEquals(8, leafLastChild.keys.get(0));
        assertEquals(1, leafLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(1, leafLastChild.indexInParent);
        assertEquals(1, ((LeafNode<Integer, Integer, Integer>)leafLastChild).getSizeValues());

        //4. Fusion LeafNode and Fusion InnerNode
        tree.delete(5);//new structure: (3); (2) (4,8); (1) (2). (3) (4) (8)
        assertEquals(3, tree.getRoot().keys.get(0));
        assertEquals(1, tree.getRoot().keys.size());
        assertEquals(5, tree.getRoot().label);

        lastChild = (InnerNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>) tree.getRoot()).getChild(1);
        assertEquals(4, lastChild.keys.get(0));
        assertEquals(8, lastChild.keys.get(1));
        assertEquals(2, lastChild.keys.size());

        leafFirstLastChild = lastChild.getChild(0);
        leafSecondLastChild = lastChild.getChild(1);
        leafLastChild = lastChild.getChild(2);

        assertEquals(3, leafFirstLastChild.keys.get(0));
        assertEquals(1, leafFirstLastChild.keys.size());
        assertEquals(lastChild, leafFirstLastChild.parent);
        assertEquals(0, leafFirstLastChild.indexInParent);
        assertNotNull(leafFirstLastChild.getLeftSibling());
        assertEquals(leafSecondLastChild, leafFirstLastChild.getRightSibling());

        assertEquals(4, leafSecondLastChild.keys.get(0));
        assertEquals(1, leafSecondLastChild.keys.size());
        assertEquals(lastChild, leafSecondLastChild.parent);
        assertEquals(1, leafSecondLastChild.indexInParent);
        assertEquals(leafFirstLastChild, leafSecondLastChild.getLeftSibling());
        assertEquals(leafLastChild, leafSecondLastChild.getRightSibling());

        assertEquals(8, leafLastChild.keys.get(0));
        assertEquals(1, leafLastChild.keys.size());
        assertEquals(lastChild, leafLastChild.parent);
        assertEquals(2, leafLastChild.indexInParent);
        assertEquals(leafSecondLastChild, leafLastChild.getLeftSibling());
        assertNull(leafLastChild.getRightSibling());
    }

    @Test
    void fileTests(){
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        int i = 1;//in order to know what output file to use next
        //insert
        Scanner input;
        try {
            //String path = ClassLoader.getSystemClassLoader().getResource("input_insert.txt").getPath();
            input = new Scanner(new File("src/testResources/input_insert.txt")); //makes the change in build.gradle for test resources seem unnecessary
            while (input.hasNext()){
                if (input.hasNextInt()){//build tree
                    tree.insert(input.nextInt(), 20);
                } else {
                    String string = input.next();
                    //if (string.equals("test")) {//test with outputFiles
                    fileTestHelper(i, tree);//actual test
                    System.out.println("Test" + i + ":" + "correct");
                    i++;
                    //}
                }

            }
            input.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        try{
            input = new Scanner(new File("src/testResources/input_delete.txt")); //makes the change in build.gradle for test resources seem unnecessary
            while (input.hasNext()){
                if (input.hasNextInt()){//build tree
                    tree.delete(input.nextInt());
                } else {
                    String string = input.next();
                    //if (string.equals("test")) {//test with outputFiles
                    fileTestHelper(i, tree);//actual test
                    System.out.println("Test" + i + ":" + "correct");
                    i++;
                    //}
                }

            }
            input.close();
        }  catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    void fileTestHelper(int i, Tree<Integer, Integer, Integer> tree){
        String path = "src/testResources/output_degree2_".concat(String.valueOf(i)).concat(".txt");
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        Scanner output;
        try {
            output = new Scanner(new File(path));

            while (output.hasNext()) {//build an ArrayList from output file, that has same structure as "arrayTree()" in Node
                ArrayList<Integer> tmpList = new ArrayList<>();
                while (output.hasNextInt()) {
                    tmpList.add(output.nextInt());
                }
                list.add(tmpList);
                String string = output.next();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //actual Test
        ArrayList<ArrayList<Integer>> list2 = new ArrayList<>();
        tree.getRoot().arrayTree(0, list2);
        assertEquals(list, list2);
    }
}