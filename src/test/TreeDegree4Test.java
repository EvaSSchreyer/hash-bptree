import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Important Note: set static degree variable in Tree.java to "4"!!
 */
class TreeDegree4Test {
    Monoid<Integer, Integer> monoid = new CountingMonoid<>();

    @Test
    void shouldContainKey() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);
        tree.insert(4,44);
        tree.insert(5,55);
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);

        LeafNode<Integer, Integer, Integer> found1 = tree.shouldContainKey(5);
        assertEquals(5, found1.keys.get(0));
        assertEquals(55, found1.getValue(0));

        LeafNode<Integer, Integer, Integer> found2 = tree.shouldContainKey(9);
        assertEquals(5, found2.keys.get(0));
        assertEquals(55, found2.getValue(0));
        assertEquals(6, found2.keys.get(1));
        assertEquals(66, found2.getValue(1));
        assertEquals(7, found2.keys.get(2));
        assertEquals(77, found2.getValue(2));
        assertEquals(8, found2.keys.get(3));
        assertEquals(88, found2.getValue(3));
        assertEquals(4, found2.label);
        assertEquals(2, found2.indexInParent);

        LeafNode<Integer, Integer, Integer> found3= tree.shouldContainKey(0);
        assertEquals(1, found3.keys.get(0));
        assertEquals(11, found3.getValue(0));
    }

    @Test
    void insertOverwrite() {
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        //insert an element in empty tree
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);
        tree.insert(4,44);
        tree.insert(5,55);
        tree.insert(6,66);
        tree.insert(1,203);
        tree.insert(4, 2312);

        assertEquals(203, tree.searchValue(1));
        assertEquals(2312, tree.searchValue(4));
        assertEquals(2, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0).getSizeKeys());
        assertEquals(4, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1).getSizeKeys());
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
    void fuseTestSimple(){
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(3,33);
        tree.insert(5,55);
        tree.insert(7,77);
        tree.insert(10,101);
        tree.delete(5);
        tree.delete(3);
        assertEquals(1, tree.root.getKey(0));
        assertEquals(7, tree.root.getKey(1));
        assertEquals(10, tree.root.getKey(2));
        assertEquals(NodeType.LeafNode, tree.root.getNodeType());
        assertEquals(11, ((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getValue(0));
        assertEquals(77, ((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getValue(1));
        assertEquals(101, ((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getValue(2));
        assertEquals(0, tree.root.indexInParent);
        assertNull(((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getLeftSibling());
        assertNull(((LeafNode<Integer, Integer, Integer>)tree.getRoot()).getRightSibling());
    }

    @Test
    void fuseTest(){
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        tree.insert(1,11);
        tree.insert(3,33);
        tree.insert(5,55);
        tree.insert(7,77);
        tree.insert(10,101);
        tree.insert(13,130);
        tree.insert(15,150);
        assertEquals(5, tree.root.getKey(0));
        assertEquals(10, tree.root.getKey(1));
        assertEquals(NodeType.InnerNode, tree.root.getNodeType());
        assertEquals(0, tree.root.indexInParent);
        //children
        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0).getNodeType());
        LeafNode<Integer, Integer, Integer> leaf0 = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0);
        assertEquals(1, leaf0.getKey(0));
        assertEquals(11, leaf0.getValue(0));
        assertEquals(3, leaf0.getKey(1));
        assertEquals(33, leaf0.getValue(1));
        assertEquals(2, leaf0.label);
        assertEquals(0, leaf0.indexInParent);

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1).getNodeType());
        LeafNode<Integer, Integer, Integer> leaf1 = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1);
        assertEquals(5, leaf1.getKey(0));
        assertEquals(55, leaf1.getValue(0));
        assertEquals(7, leaf1.getKey(1));
        assertEquals(77, leaf1.getValue(1));
        assertEquals(2, leaf1.label);
        assertEquals(1, leaf1.indexInParent);

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(2).getNodeType());
        LeafNode<Integer, Integer, Integer> leaf2 = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(2);
        assertEquals(10, leaf2.getKey(0));
        assertEquals(101, leaf2.getValue(0));
        assertEquals(13, leaf2.getKey(1));
        assertEquals(130, leaf2.getValue(1));
        assertEquals(15, leaf2.getKey(2));
        assertEquals(150, leaf2.getValue(2));
        assertEquals(3, leaf2.label);
        assertEquals(2, leaf2.indexInParent);

        assertEquals(leaf1, leaf0.getRightSibling());
        assertEquals(leaf2, leaf1.getRightSibling());
        assertEquals(null, leaf2.getRightSibling());
        assertEquals(leaf1, leaf2.getLeftSibling());
        assertEquals(leaf0, leaf1.getLeftSibling());
        assertEquals(null, leaf0.getLeftSibling());
        //REAL TEST STARTING HERE
        tree.delete(3);
        assertEquals(10, tree.root.getKey(0));
        assertEquals(NodeType.InnerNode, tree.root.getNodeType());
        assertEquals(0, tree.root.indexInParent);
        assertEquals(6, tree.root.label);
        assertEquals(1, tree.root.getSizeKeys());
        assertEquals(2, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getSizeChildren());

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0).getNodeType());
        leaf0 = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(0);
        assertEquals(1, leaf0.getKey(0));
        assertEquals(11, leaf0.getValue(0));
        assertEquals(5, leaf0.getKey(1));
        assertEquals(55, leaf0.getValue(1));
        assertEquals(7, leaf0.getKey(2));
        assertEquals(77, leaf0.getValue(2));
        assertEquals(3, leaf0.label);
        assertEquals(0, leaf0.indexInParent);
        assertEquals(3, leaf0.getSizeKeys());
        assertEquals(3, leaf0.getSizeValues());

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1).getNodeType());
        leaf1 = (LeafNode<Integer, Integer, Integer>) ((InnerNode<Integer, Integer, Integer>)tree.getRoot()).getChild(1);
        assertEquals(10, leaf1.getKey(0));
        assertEquals(101, leaf1.getValue(0));
        assertEquals(13, leaf1.getKey(1));
        assertEquals(130, leaf1.getValue(1));
        assertEquals(15, leaf1.getKey(2));
        assertEquals(150, leaf1.getValue(2));
        assertEquals(3, leaf1.label);
        assertEquals(1, leaf1.indexInParent);
        assertEquals(3, leaf1.getSizeKeys());
        assertEquals(3, leaf1.getSizeValues());

        assertEquals(leaf1, leaf0.getRightSibling());
        assertEquals(null, leaf1.getRightSibling());
        assertEquals(leaf0, leaf1.getLeftSibling());
        assertEquals(null, leaf0.getLeftSibling());
    }
    @Test
    void fileTests(){
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        int i = 1;//in order to know what output file to use next
        //insert
        Scanner input;
        try {
            input = new Scanner(new File("src/testResources/inputDegree4_insert.txt")); //makes the change in build.gradle for test resources seem unnecessary
            while (input.hasNext()){
                if (input.hasNextInt()){//build tree
                    int test = input.nextInt();
                    tree.insert(test, 20);
                } else {
                    String string = input.next();
                    fileTestHelper(i, tree);//actual test
                    System.out.println("Test" + i + ":" + "correct");
                    i++;
                }

            }
            input.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        try{
            input = new Scanner(new File("src/testResources/inputDegree4_delete.txt")); //makes the change in build.gradle for test resources seem unnecessary
            while (input.hasNext()){
                if (input.hasNextInt()){//build tree
                    tree.delete(input.nextInt());
                } else {
                    String string = input.next();
                    fileTestHelper(i, tree);//actual test
                    System.out.println("Test" + i + ":" + "correct");
                    i++;
                }

            }
            input.close();
        }  catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    void fileTestHelper(int i, Tree<Integer, Integer, Integer> tree){
        String path = "src/testResources/output_degree4_".concat(String.valueOf(i)).concat(".txt");
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