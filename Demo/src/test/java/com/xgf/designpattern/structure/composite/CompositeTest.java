package com.xgf.designpattern.structure.composite;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xgf
 * @create 2021-12-22 17:00
 * @description 组合模式 - 树形结构（部分-整体场景，文件夹等），实现部分和整体操作的一致性
 **/

public class CompositeTest {

    TreeNode parent1 = TreeNode.builder().name("parent_1").build();
    TreeNode parent2 = TreeNode.builder().name("parent_2").build();
    TreeNode parent3 = TreeNode.builder().name("parent_3").build();

    TreeNode node1 = TreeNode.builder().name("node_1").build();
    TreeNode node2 = TreeNode.builder().name("node_2").build();
    TreeNode node3 = TreeNode.builder().name("node_3").build();
    TreeNode node4 = TreeNode.builder().name("node_4").build();
    TreeNode node5 = TreeNode.builder().name("node_5").build();
    TreeNode node6 = TreeNode.builder().name("node_6").build();
    TreeNode node7 = TreeNode.builder().name("node_7").build();
    TreeNode node8 = TreeNode.builder().name("node_8").build();
    TreeNode node9 = TreeNode.builder().name("node_9").build();


    @Test
    public void test1() {
        parent1.batchAdd(Arrays.asList(node1, node2, node3, node1, node2, node1));
        parent1.batchAdd(Arrays.asList(node1, node2, node3));
        parent2.batchAdd(Arrays.asList(node4, node5, node6));
        parent3.batchAdd(Arrays.asList(node7, node8, node9));

        System.out.println("======== test1 ========");
        System.out.println("parent1 = " + JSON.toJSONString(parent1));
        System.out.println("parent2 = " + JSON.toJSONString(parent2));
        System.out.println("parent3 = " + JSON.toJSONString(parent3));
    }

    @Test
    public void test2() {
        parent1.batchAddAndRelate(Arrays.asList(node1, node2, node3, node1, node2, node1));
//        parent1.batchAddAndRelate(Arrays.asList(node1, node2, node3));
        parent2.batchAddAndRelate(Arrays.asList(node4, node5, node6));
        parent3.batchAddAndRelate(Arrays.asList(node7, node8, node9));

        System.out.println("\n======== test2 ========");
        System.out.println("parent1 = " + JSON.toJSONString(parent1));
        System.out.println("parent2 = " + JSON.toJSONString(parent2));
        System.out.println("parent3 = " + JSON.toJSONString(parent3));

        //  parent1.addAndRelate(node4);

        System.out.println("node7_8_9 = " + JSON.toJSONString(Arrays.asList(node7, node8, node9)));
        parent3.batchRemoveAndRelateAll();
        System.out.println("node7_8_9 = " + JSON.toJSONString(Arrays.asList(node7, node8, node9)));
        System.out.println("parent3 = " + JSON.toJSONString(parent3));

        parent1.batchRemove(Arrays.asList(node6, node3, node5));
        parent1.addAndRelate(node8);
        parent1.batchRemoveAndRelate(Collections.singletonList(node9));
        System.out.println("parent1 = " + JSON.toJSONString(parent1));

    }


}
