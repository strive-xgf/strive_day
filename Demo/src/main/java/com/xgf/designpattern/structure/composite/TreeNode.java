package com.xgf.designpattern.structure.composite;

import com.alibaba.fastjson.JSON;
import com.xgf.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-12-22 16:43
 * @description 组合模式 - 树节点模拟
 **/

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {

    /**
     * 名称
     */
    private String name;

    /**
     * 父节点 【只能有一个父节点】
     */
//    private TreeNode parentNode;
    private String parentNodeName;

    /**
     * 子节点
     */
    private List<TreeNode> childrenNodeList;

    public void initChildrenNodeList() {
        if (CollectionUtils.isEmpty(childrenNodeList)) {
            childrenNodeList = new ArrayList<>();
        }
    }


    /**
     * 新增一个子节点
     * @param node 节点
     */
    public void add(TreeNode node) {
        initChildrenNodeList();
        // 并集
        remove(node);
        childrenNodeList.add(node);
    }


    /**
     * 批量新增子节点
     * @param nodeList 节点集合
     */
    public void batchAdd(List<TreeNode> nodeList) {
        initChildrenNodeList();
        nodeList = nodeList.stream().distinct().collect(Collectors.toList());
        batchRemove(nodeList);
        childrenNodeList.addAll(nodeList);
    }

    /**
     * 删除一个子节点
     * @param node 节点
     */
    public void remove(TreeNode node) {
        initChildrenNodeList();
        childrenNodeList.remove(node);
    }

    /**
     * 批量删除子节点
     * @param nodeList 节点集合
     */
    public void batchRemove(List<TreeNode> nodeList) {
        initChildrenNodeList();
        childrenNodeList.removeAll(nodeList);
    }

    /**
     * 新增子节点，并关联父节点
     * @param node 节点
     */
    public void addAndRelate(TreeNode node) {
        batchAddAndRelate(Collections.singletonList(node));
    }

    public void batchAddAndRelate(List<TreeNode> nodeList) {
        existParentNodeThrow(nodeList);
        batchAdd(nodeList);
        batchRelateParent(nodeList, this);
    }

    /**
     * 移除子节点，并删除父节点
     * @param node
     */
    public void removeAndRelate(TreeNode node) {
        batchRemoveAndRelate(Collections.singletonList(node));
    }

    public void batchRemoveAndRelate(List<TreeNode> nodeList) {
        batchClearParent(nodeList);
        batchRemove(nodeList);
    }

    public void batchRemoveAndRelateAll() {
        batchRemoveAndRelate(getChildrenNodeList());
    }

    /**
     * 批量关联父节点
     * @param nodeList 子节点列表
     * @param parentNodeParam 父节点
     */
    private void batchRelateParent(List<TreeNode> nodeList, TreeNode parentNodeParam) {
        if (CollectionUtils.isEmpty(nodeList) || Objects.isNull(parentNodeParam)) {
            log.info("====== batchRelateParent in param is null, nodeList = 【{}】, parentNodeParam = 【{}】",
                    JSON.toJSONString(nodeList), JSON.toJSONString(parentNodeParam));
            return;
        }
        nodeList.stream().filter(p -> StringUtils.isBlank(p.getParentNodeName())).forEach(p -> p.setParentNodeName(parentNodeParam.getName()));
    }

    /**
     * 批量清除父节点
     * @param nodeList 子节点列表
     */
    private void batchClearParent(List<TreeNode> nodeList) {
        if (CollectionUtils.isEmpty(nodeList)) {
            log.info("====== batchClearParent in param is null, nodeList = 【{}】", JSON.toJSONString(nodeList));
            return;
        }
        nodeList.forEach(p -> p.setParentNodeName(null));
    }


    /**
     * 存在父节点抛出异常
     * @param nodeList 节点集合
     */
    private void existParentNodeThrow(List<TreeNode> nodeList) {
        List<TreeNode> resultList = Optional.ofNullable(nodeList)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(p -> StringUtils.isNotBlank(p.getParentNodeName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        throw new CustomException("existParentNodeThrowException", "节点存在父节点 > \n" + JSON.toJSONString(resultList));

    }

}
