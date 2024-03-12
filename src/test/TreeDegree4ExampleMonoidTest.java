import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Important Note: set static degree variable in Tree.java to "4"!!
 */
class TreeDegree4ExampleMonoidTest {
    Monoid<ExampleMonoid<Integer>, Integer> monoid = new ExampleMonoid<>(0,0, null);
    @Test
    void shouldContainKey() {
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);
        tree.insert(4,44);
        tree.insert(5,55);
        tree.insert(6,66);
        tree.insert(7,77);
        tree.insert(8,88);
        assertEquals(8, tree.root.label.greatestElement);

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> found1 = tree.shouldContainKey(5);
        assertEquals(5, found1.keys.get(0));
        assertEquals(55, found1.getValue(0));

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> found2 = tree.shouldContainKey(9);
        assertEquals(5, found2.keys.get(0));
        assertEquals(55, found2.getValue(0));
        assertEquals(6, found2.keys.get(1));
        assertEquals(66, found2.getValue(1));
        assertEquals(7, found2.keys.get(2));
        assertEquals(77, found2.getValue(2));
        assertEquals(8, found2.keys.get(3));
        assertEquals(88, found2.getValue(3));
        assertEquals(4, found2.label.count);
        assertEquals(8, found2.label.greatestElement);
        assertEquals(2, found2.indexInParent);

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> found3= tree.shouldContainKey(0);
        assertEquals(1, found3.keys.get(0));
        assertEquals(11, found3.getValue(0));
        assertEquals(2, found3.label.greatestElement);
    }

    @Test
    void insertRoot() {
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        //insert an element in empty tree
        tree.insert(1,11);
        assertNotNull(tree.getRoot());
        assertEquals(NodeType.LeafNode, tree.getRoot().getNodeType());
        assertEquals(1, (int) tree.getRoot().keys.get(0));
        assertEquals(11, ((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getValue(0));
        assertEquals(monoid, tree.getRoot().monoid);
        assertEquals(1, tree.getRoot().label.count);
        assertEquals(1, tree.getRoot().label.greatestElement);
        assertNull(tree.getRoot().parent);
    }

    @Test
    void fuseTestSimple(){
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        tree.insert(1,11);
        tree.insert(3,33);
        tree.insert(5,55);
        tree.insert(7,77);
        tree.insert(10,101);
        assertEquals(10, tree.root.label.greatestElement);
        tree.delete(5);
        tree.delete(3);
        assertEquals(10, tree.root.label.greatestElement);
        assertEquals(1, tree.root.getKey(0));
        assertEquals(7, tree.root.getKey(1));
        assertEquals(10, tree.root.getKey(2));
        assertEquals(NodeType.LeafNode, tree.root.getNodeType());
        assertEquals(11, ((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getValue(0));
        assertEquals(77, ((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getValue(1));
        assertEquals(101, ((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getValue(2));
        assertEquals(0, tree.root.indexInParent);
        assertNull(((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getLeftSibling());
        assertNull(((LeafNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getRightSibling());
    }

    @Test
    void fuseTest(){
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
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
        assertEquals(15, tree.root.label.greatestElement);
        //children
        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(0).getNodeType());
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf0 = (LeafNode<Integer, Integer, ExampleMonoid<Integer>>) ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(0);
        assertEquals(1, leaf0.getKey(0));
        assertEquals(11, leaf0.getValue(0));
        assertEquals(3, leaf0.getKey(1));
        assertEquals(33, leaf0.getValue(1));
        assertEquals(2, leaf0.label.count);
        assertEquals(3, leaf0.label.greatestElement);
        assertEquals(0, leaf0.indexInParent);

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(1).getNodeType());
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf1 = (LeafNode<Integer, Integer, ExampleMonoid<Integer>>) ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(1);
        assertEquals(5, leaf1.getKey(0));
        assertEquals(55, leaf1.getValue(0));
        assertEquals(7, leaf1.getKey(1));
        assertEquals(77, leaf1.getValue(1));
        assertEquals(2, leaf1.label.count);
        assertEquals(7, leaf1.label.greatestElement);
        assertEquals(1, leaf1.indexInParent);

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(2).getNodeType());
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf2 = (LeafNode<Integer, Integer, ExampleMonoid<Integer>>) ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(2);
        assertEquals(10, leaf2.getKey(0));
        assertEquals(101, leaf2.getValue(0));
        assertEquals(13, leaf2.getKey(1));
        assertEquals(130, leaf2.getValue(1));
        assertEquals(15, leaf2.getKey(2));
        assertEquals(150, leaf2.getValue(2));
        assertEquals(3, leaf2.label.count);
        assertEquals(15, leaf2.label.greatestElement);
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
        assertEquals(6, tree.root.label.count);
        assertEquals(15, tree.root.label.greatestElement);
        assertEquals(1, tree.root.getSizeKeys());
        assertEquals(2, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getSizeChildren());

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(0).getNodeType());
        leaf0 = (LeafNode<Integer, Integer, ExampleMonoid<Integer>>) ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(0);
        assertEquals(1, leaf0.getKey(0));
        assertEquals(11, leaf0.getValue(0));
        assertEquals(5, leaf0.getKey(1));
        assertEquals(55, leaf0.getValue(1));
        assertEquals(7, leaf0.getKey(2));
        assertEquals(77, leaf0.getValue(2));
        assertEquals(3, leaf0.label.count);
        assertEquals(7, leaf0.label.greatestElement);
        assertEquals(0, leaf0.indexInParent);
        assertEquals(3, leaf0.getSizeKeys());
        assertEquals(3, leaf0.getSizeValues());

        assertEquals(NodeType.LeafNode, ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(1).getNodeType());
        leaf1 = (LeafNode<Integer, Integer, ExampleMonoid<Integer>>) ((InnerNode<Integer, Integer, ExampleMonoid<Integer>>)tree.getRoot()).getChild(1);
        assertEquals(10, leaf1.getKey(0));
        assertEquals(101, leaf1.getValue(0));
        assertEquals(13, leaf1.getKey(1));
        assertEquals(130, leaf1.getValue(1));
        assertEquals(15, leaf1.getKey(2));
        assertEquals(150, leaf1.getValue(2));
        assertEquals(3, leaf1.label.count);
        assertEquals(15, leaf1.label.greatestElement);
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
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
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

    void fileTestHelper(int i, Tree<Integer, Integer, ExampleMonoid<Integer>> tree){
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

    @Test
    void computeFingerprint(){
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        tree.insert(1,11);
        tree.insert(2,22);
        tree.insert(3,33);
        tree.insert(4,44);//                        (7)
        tree.insert(5,55);//        (3,   5)                 (9,    13,     21,     32)
        tree.insert(6,66);// (1,2)   (3,4)  (5,6)       (7,8)  (9,12) (13,17) (21,22) (32,41,43)
        tree.insert(7,77);
        tree.insert(8,88);
        tree.insert(9,99);
        tree.insert(12,120);
        tree.insert(13,130);
        tree.insert(17,170);
        tree.insert(21,210);
        tree.insert(22,220);
        tree.insert(32,320);
        tree.insert(41,410);
        tree.insert(43,430);

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf1 = tree.shouldContainKey(1);
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf2 = tree.shouldContainKey(3);
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf3 = tree.shouldContainKey(5);
        long test1To6 = leaf1.label.hash ^ leaf2.label.hash ^ leaf3.label.hash;
        assertEquals(6, tree.computeFingerprint(7, leaf1, leaf1.searchNextBest(1)).getFirst().count);
        assertEquals(test1To6, tree.computeFingerprint(7, leaf1, 0).getFirst().hash);
        assertEquals(6, tree.computeFingerprint(7, leaf1, 0).getFirst().greatestElement);
        test1To6 = leaf1.getKey(0).hashCode() ^ leaf1.getKey(1).hashCode();
        test1To6 = test1To6 ^ leaf2.getKey(0).hashCode() ^ leaf2.getKey(1).hashCode();
        test1To6 = test1To6 ^ leaf3.getKey(0).hashCode() ^ leaf3.getKey(1).hashCode();
        assertEquals(test1To6, tree.computeFingerprint(7, leaf1, 0).getFirst().hash);

        long test2To4 = leaf1.getKey(1).hashCode() ^ leaf2.label.hash;
        assertEquals(3, tree.computeFingerprint(4+1, leaf1, leaf1.searchNextBest(2)).getFirst().count);
        assertEquals(test2To4, tree.computeFingerprint(4+1, leaf1, 1).getFirst().hash);
        assertEquals(4, tree.computeFingerprint(4+1, leaf1, 1).getFirst().greatestElement);

        long test3To5 = leaf2.label.hash ^ leaf3.getKey(0).hashCode();
        assertEquals(3, tree.computeFingerprint(5+1, leaf2, 0).getFirst().count);
        assertEquals(test3To5, tree.computeFingerprint(5+1, leaf2, 0).getFirst().hash);
        assertEquals(5, tree.computeFingerprint(5+1, leaf2, 0).getFirst().greatestElement);

        InnerNode<Integer, Integer, ExampleMonoid<Integer>> inner2 = tree.searchInnerKeyNode(9);
        long test5To43 = leaf3.label.hash ^ inner2.label.hash;
        assertEquals(13, tree.computeFingerprint(43+1, leaf3, leaf3.searchNextBest(5)).getFirst().count);
        assertEquals(test5To43, tree.computeFingerprint(43+1, leaf3, 0).getFirst().hash);
        assertEquals(43, tree.computeFingerprint(43+1, leaf3, 0).getFirst().greatestElement);

    }

    /**
     * iterates over leafNodes to determine the hashValue - needed for comparison
     * @return the computed hashValue
     */
    ExampleMonoid<Integer> helperComputeFingerprint(LeafNode<Integer, Integer, ExampleMonoid<Integer>> firstLeaf, int indexFirstKey, LeafNode<Integer, Integer, ExampleMonoid<Integer>> lastLeaf, int indexLastKey, int y){
        ExampleMonoid<Integer> test = new ExampleMonoid<>(0, 0, null);
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf = firstLeaf;
        //combine all relevant Elements in our firstLeafNode and all following LeafNodes that are nor our lastLeafNode
        while (leaf != lastLeaf){
            if (leaf == firstLeaf){//add values from firstLeaf
                for (int i = indexFirstKey; i < firstLeaf.getSizeKeys(); i++){
                    test = test.combine(test, test.mapIntoMonoid(firstLeaf.getKey(i)));
                }
            } else {//add values from all leaves between the first and last Leaf of range
                test = test.combine(test, leaf.label);
            }
            leaf = leaf.getRightSibling();//check the next Leaf
        }
        //now we want to add the lastLeafNode -> reset our firstIndex, if the LeafNodes (first and least) are different
        if (firstLeaf != lastLeaf){
            indexFirstKey = 0;//only 0 if the leaf nodes are different
        }
        //here leaf == lastLeaf -> add values from last leaf
        for (int i = indexFirstKey; i <= indexLastKey; i++){
            //indexLastKey could point to the fist element outside of our range, and it could also be exactly "leaf.getSizeKeys()"
            if (i < leaf.getSizeKeys() && leaf.getKey(i) <= y){
                test = test.combine(test, test.mapIntoMonoid(leaf.getKey(i)));
            }
        }
        return test;
    }
    @Test
    void computeFingerprintBigger(){
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        for (int i = 1; i < 100; i++){
            tree.insert(i, i*2);
        }
        /*
        structure:                          [19, 37, 55, 73]
                            [7, 13]  [25, 31]  [43, 49]  [61, 67]  [79, 85, 91]
               [3, 5) (9, 11)  (15, 17)  (21, 23)  (27, 29)  (33, 35)  (39, 41)  (45, 47)  (51, 53)  (57, 59)  (63, 65)  (69, 71)  (75, 77)  (81, 83)  (87, 89)  (93, 95, 97]
        [1, 2)  (3, 4)  (5, 6)  (7, 8)  (9, 10)  (11, 12)  (13, 14)  (15, 16)  (17, 18)  (19, 20)  (21, 22)  (23, 24)  (25, 26)  (27, 28)  (29, 30)  (31, 32)  (33, 34)  (35, 36)  (37, 38)  (39, 40)  (41, 42)  (43, 44)  (45, 46)  (47, 48)  (49, 50)  (51, 52)  (53, 54)  (55, 56)  (57, 58)  (59, 60)  (61, 62)  (63, 64)  (65, 66)  (67, 68)  (69, 70)  (71, 72)  (73, 74)  (75, 76)  (77, 78)  (79, 80)  (81, 82)  (83, 84)  (85, 86)  (87, 88)  (89, 90)  (91, 92)  (93, 94)  (95, 96)  (97, 98, 99]]
         */
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf1 = tree.shouldContainKey(9);//9,10
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf2 = leaf1.getRightSibling();//11,12
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf3 = leaf2.getRightSibling();//13,14
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf4 = leaf3.getRightSibling();//15,16
        long test9To15 = leaf1.label.hash ^ leaf2.label.hash ^ leaf3.label.hash ^ leaf4.getKey(0).hashCode();
        Pair<ExampleMonoid<Integer>, Pair<Node<Integer, Integer, ExampleMonoid<Integer>>, Integer>> pair = tree.computeFingerprint(15+1, leaf1, leaf1.searchNextBest(9)/*= 0*/);
        assertEquals(test9To15, pair.getFirst().hash);
        assertEquals(16, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf5 = tree.shouldContainKey(30);//29,30
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf6 = leaf5.getRightSibling();//31,32
        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf7 = leaf6.getRightSibling();//33,34
        long test30To34 = leaf5.getKey(1).hashCode() ^ leaf6.label.hash ^ leaf7.label.hash;
        pair = tree.computeFingerprint(34+1, leaf5, leaf5.searchNextBest(30));
        assertEquals(test30To34, pair.getFirst().hash);
        assertEquals(35, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        InnerNode<Integer, Integer, ExampleMonoid<Integer>> inner1 = tree.searchInnerKeyNode(15);//inner: 15,17
        long test13To18 = inner1.label.hash;
        pair = tree.computeFingerprint(18+1, leaf3, 0);
        assertEquals(test13To18, pair.getFirst().hash);
        assertEquals(19, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));
        //37, because aggDOWN is not used

        //TEST what happens if we put an InnerNode into our function
        InnerNode<Integer, Integer, ExampleMonoid<Integer>> inner2 = tree.searchInnerKeyNode(21);//21,23
        long test19To19 = inner2.getChild(0).getKey(0).hashCode();
        pair = tree.computeFingerprint(19+1, inner1.parent.parent, 1);
        assertEquals(1, pair.getFirst().count);
        assertEquals(test19To19, pair.getFirst().hash);
        assertEquals(19, pair.getFirst().greatestElement);
        assertEquals(20, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        long test19To20 = inner2.getChild(0).label.hash;
        pair = tree.computeFingerprint(20+1, inner1.parent.parent, 1);
        assertEquals(2, pair.getFirst().count);
        assertEquals(test19To20, pair.getFirst().hash);
        assertEquals(20, pair.getFirst().greatestElement);
        assertEquals(21, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        long test19To24 = inner2.label.hash;
        pair = tree.computeFingerprint(24+1, inner1.parent.parent, 1);
        assertEquals(6, pair.getFirst().count);
        assertEquals(test19To24, pair.getFirst().hash);
        assertEquals(24, pair.getFirst().greatestElement);
        assertEquals(25, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));//not an InnerNode because we do use aggDOWN here

        pair = tree.computeFingerprint(24+1, inner2.getChild(0), 0);
        assertEquals(6, pair.getFirst().count);
        assertEquals(test19To24, pair.getFirst().hash);
        assertEquals(24, pair.getFirst().greatestElement);
        assertEquals(25, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));
        //still not an InnerNode because we do use aggDOWN here (even though different starting point)
        // -> Reason: after adding 24, we check if there are still values > 24 but < 25

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf9 = tree.shouldContainKey(25);//33,34
        long test19To25 = inner2.label.hash ^ leaf9.getKey(0).hashCode();
        pair = tree.computeFingerprint(25+1, inner1.parent.parent, 1);
        assertEquals(7, pair.getFirst().count);
        assertEquals(test19To25, pair.getFirst().hash);
        assertEquals(25, pair.getFirst().greatestElement);
        assertEquals(26, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf10 = tree.shouldContainKey(1);
        long test1To3 = leaf10.label.hash ^ leaf10.getRightSibling().getKey(0).hashCode();
        pair = tree.computeFingerprint(3+1, tree.root, 0);
        assertEquals(3, pair.getFirst().count);
        assertEquals(test1To3, pair.getFirst().hash);
        assertEquals(3, pair.getFirst().greatestElement);
        assertEquals(4, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        InnerNode<Integer, Integer, ExampleMonoid<Integer>> inner3 = tree.searchInnerKeyNode(7);
        pair = tree.computeFingerprint(3+1, inner3, 0);
        assertEquals(3, pair.getFirst().count);
        assertEquals(test1To3, pair.getFirst().hash);
        assertEquals(3, pair.getFirst().greatestElement);
        assertEquals(4, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        pair = tree.computeFingerprint(3+1, inner3.getChild(0), 0);
        assertEquals(3, pair.getFirst().count);
        assertEquals(test1To3, pair.getFirst().hash);
        assertEquals(3, pair.getFirst().greatestElement);
        assertEquals(4, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));
        //this test is done

        LeafNode<Integer, Integer, ExampleMonoid<Integer>> leaf8 = tree.shouldContainKey(19);//19,20
        long test11To19 = leaf2.label.hash ^ inner1.label.hash ^ leaf8.getKey(0).hashCode();
        pair = tree.computeFingerprint(19+1, leaf2, 0);
        assertEquals(test11To19, pair.getFirst().hash);
        assertEquals(20, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        long test12To19 = leaf2.getKey(1).hashCode() ^ inner1.label.hash ^ leaf8.getKey(0).hashCode();
        assertEquals(test12To19, tree.computeFingerprint(19+1, leaf2, 1).getFirst().hash);
        assertEquals(20, pair.getSecond().getFirst().getKey(pair.getSecond().getSecond()));

        assertEquals(leaf2.getKey(0).hashCode(), tree.computeFingerprint(11+1, leaf2, 0).getFirst().hash);

        for (int j = 0; j < 100; j++){
            System.out.println("Test number: " + j);
            Random ran = new Random();
            int tmp = ran.nextInt(99) + 1;
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafTMP = tree.shouldContainKey(tmp);
            int tmpIndex = leafTMP.searchNextBest(tmp);

            int tmp2 = ran.nextInt((99 - tmp) + 1) + tmp;
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafTMP2 = tree.shouldContainKey(tmp2);
            int tmpIndex2 = leafTMP2.searchNextBest(tmp2);

            assertEquals(tmp, leafTMP.getKey(tmpIndex));
            assertEquals(tmp2, leafTMP2.getKey(tmpIndex2));

            ExampleMonoid<Integer> test = helperComputeFingerprint(leafTMP, tmpIndex, leafTMP2, tmpIndex2, tmp2);
            ExampleMonoid<Integer> returnedValue = tree.computeFingerprint(tmp2+1, leafTMP, tmpIndex).getFirst();
            assertEquals(test.count, returnedValue.count);
            assertEquals(test.hash, returnedValue.hash);
            assertEquals(test.greatestElement, returnedValue.greatestElement);
            System.out.println("Test number: " + j + " correct");
        }
    }

    @Test
    void computeFingerprintRandom(){
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        Random ran = new Random();
        //build randomized tree
        tree.insert(ran.nextInt(10000), ran.nextInt(100));
        while (tree.root.label.count < 500){
            int tmp = ran.nextInt(10000);
            tree.insert(tmp, ran.nextInt(100));
        }
        //tree.root.printTree();

        for (int i = 0; i < 1000; i++){
            //get the range we want to count/...
            int firstKey = ran.nextInt(10000);
            int secondKey = ran.nextInt((10000 - firstKey)) + firstKey;
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafFirstKey = tree.shouldContainKey(firstKey);
            int indexFirstKey = leafFirstKey.searchNextBest(firstKey);

            Pair<ExampleMonoid<Integer>, Pair<Node<Integer, Integer, ExampleMonoid<Integer>>, Integer>> pair = tree.computeFingerprint(secondKey+1, leafFirstKey, indexFirstKey);
            //the range should contain between 10 or 20 Elements (amount of elements determined by "pair.getFirst().count")
            while (pair.getFirst().count < 10 || pair.getFirst().count > 20){
                firstKey = ran.nextInt(10000);
                secondKey = ran.nextInt((10000 - firstKey)) + firstKey;
                leafFirstKey = tree.shouldContainKey(firstKey);
                indexFirstKey = leafFirstKey.searchNextBest(firstKey);

                pair = tree.computeFingerprint(secondKey+1, leafFirstKey, indexFirstKey);
            }
            //Print the range
            System.out.println("\nParam 1: " + firstKey);
            System.out.println("Param 2: " + secondKey);
            //get the leaf and index of the last element of range
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafSecondKey = tree.shouldContainKey(secondKey);
            int indexSecondKey = leafSecondKey.searchNextBest(secondKey);

            //the Key and the found Leaf+Index can be of different values, because Key dos not necessarily exists in our tree

            //iterate over leafNodes to determine the hashValue - needed for comparison
            ExampleMonoid<Integer> test = helperComputeFingerprint(leafFirstKey, indexFirstKey, leafSecondKey, indexSecondKey, secondKey);
            Pair<ExampleMonoid<Integer>, Pair<Node<Integer, Integer, ExampleMonoid<Integer>>, Integer>> returnedValue = tree.computeFingerprint(secondKey+1, leafFirstKey, indexFirstKey);

            assertEquals(test.count, returnedValue.getFirst().count);
            assertEquals(test.hash, returnedValue.getFirst().hash);
            assertEquals(test.greatestElement, returnedValue.getFirst().greatestElement);
            if (returnedValue.getSecond().getFirst() != null){//checks the returned node
                assertEquals(NodeType.LeafNode, returnedValue.getSecond().getFirst().getNodeType()); //I can't get InnerNodes
                LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafReturnedKey = tree.shouldContainKey(secondKey+1);
                int indexReturnedKey = leafReturnedKey.searchNextBest(secondKey+1);
                if (indexReturnedKey >= leafReturnedKey.keys.size()){
                    leafReturnedKey = leafReturnedKey.getRightSibling();
                    indexReturnedKey = 0;
                }
                assertEquals(leafReturnedKey, returnedValue.getSecond().getFirst());
                assertEquals(leafReturnedKey.keys.get(indexReturnedKey), returnedValue.getSecond().getFirst().keys.get(returnedValue.getSecond().getSecond()));
            } else {
                System.out.println("NULL --- Test number: " + i + " has returned null as the node");
            }
            System.out.println("Test number: " + i + " correct");
        }
    }

    @Test
    void computeFingerprintInnerNodes() {
        Tree<Integer, Integer, ExampleMonoid<Integer>> tree = new Tree<Integer, Integer, ExampleMonoid<Integer>>(monoid);
        Random ran = new Random();
        //build randomized tree
        tree.insert(ran.nextInt(10000), ran.nextInt(100));
        while (tree.root.label.count < 500){
            int tmp = ran.nextInt(10000);
            tree.insert(tmp, ran.nextInt(100));
        }

        for (int i = 0; i < 1000; i++){
            //get the range we want to count/...
            int firstKey = ran.nextInt(10000);
            InnerNode<Integer, Integer, ExampleMonoid<Integer>> innerFirstKey = tree.searchInnerKeyNode(firstKey);
            while (innerFirstKey == null){//find an innerNode
                firstKey = ran.nextInt(10000);
                innerFirstKey = tree.searchInnerKeyNode(firstKey);
            }
            int secondKey = ran.nextInt((10000 - firstKey)) + firstKey;
            int indexFirstKey = innerFirstKey.searchNextBest(firstKey)+1;//+1 because we use child indices

            //Print the range
            System.out.println("\nParam 1: " + firstKey);
            System.out.println("Param 2: " + secondKey);
            //get the leaf and index of the last element of range
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafSecondKey = tree.shouldContainKey(secondKey);
            int indexSecondKey = leafSecondKey.searchNextBest(secondKey);

            //the Key and the found Leaf+Index can be of different values, because Key dos not necessarily exists in our tree

            Pair<ExampleMonoid<Integer>, Pair<Node<Integer, Integer, ExampleMonoid<Integer>>, Integer>> returnedValue = tree.computeFingerprint(secondKey+1, innerFirstKey, indexFirstKey);

            //iterate over leafNodes to determine the hashValue - needed for comparison
            LeafNode<Integer, Integer, ExampleMonoid<Integer>> leafFirstKeyHelper = tree.shouldContainKey(firstKey);
            int indexFirstKeyHelper = leafFirstKeyHelper.searchNextBest(firstKey);
            ExampleMonoid<Integer> test = helperComputeFingerprint(leafFirstKeyHelper, indexFirstKeyHelper, leafSecondKey, indexSecondKey, secondKey);

            assertEquals(test.count, returnedValue.getFirst().count);
            assertEquals(test.hash, returnedValue.getFirst().hash);
            assertEquals(test.greatestElement, returnedValue.getFirst().greatestElement);
            //assertEquals(NodeType.LeafNode, returnedValue.getSecond().getFirst().getNodeType()); //I can get InnerNodes
            System.out.println("Test number: " + i + " correct");
        }

    }

    @Test
    void associativityStringTest(){
        Monoid<ConCatKeyMonoid<Character>, Character> monoidString = new ConCatKeyMonoid<>(0,0, null,null);
        Tree<Character, Integer, ConCatKeyMonoid<Character>> tree = new Tree<Character, Integer, ConCatKeyMonoid<Character>>(monoidString);
        for (int i = 97; i<= 122; i++){
            tree.insert((char) i, i);
        }
        LeafNode<Character, Integer, ConCatKeyMonoid<Character>> leaf1 = tree.shouldContainKey('c');
        ArrayList<Character> testCtoH = new ArrayList<>(Arrays.asList('c', 'd', 'e', 'f', 'g', 'h'));
        ConCatKeyMonoid<Character> test = tree.computeFingerprint('i', leaf1, leaf1.searchNextBest('c')).getFirst();
        assertEquals(testCtoH, test.concatKey);
    }
}