import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Important Note: set static degree variable in Tree.java to "3"!!
 */
class TreeDegree3Test {
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
    void fileTests(){
        Tree<Integer, Integer, Integer> tree = new Tree<Integer, Integer, Integer>(monoid);
        int i = 1;//in order to know what output file to use next
        //insert
        Scanner input;
        try {
            input = new Scanner(new File("src/testResources/inputDegree3_insert.txt")); //makes the change in build.gradle for test resources seem unnecessary
            while (input.hasNext()){
                if (input.hasNextInt()){//build tree
                    tree.insert(input.nextInt(), 20);
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
            input = new Scanner(new File("src/testResources/inputDegree3_delete.txt")); //makes the change in build.gradle for test resources seem unnecessary
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
        String path = "src/testResources/output_degree3_".concat(String.valueOf(i)).concat(".txt");
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