package com.example.zhujiang.zhujiangpro_2.treeview.utils;

import com.example.zhujiang.zhujiangpro_2.R;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodeId;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodeLabel;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiang on 16-5-25.
 */
public class TreeHelper {

    /**
     * 将用户的数据转换为树形数据
     *
     * @param datas
     * @return
     */
    public static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {

        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        for (T t : datas) {
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();

            int id = -1;
            int pid = -1;
            String label = null;

            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }

                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }

                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
            }
            node = new Node(id, pid, label);
            nodes.add(node);
        }


        /**
         * 设置关联关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);

            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getPid() == n.getId()) {
                    n.getChilds().add(m);
                    m.setParent(n);
                } else if (n.getPid() == m.getId()) {
                    m.getChilds().add(n);
                    n.setParent(m);
                }
            }
        }


        /**
         * 设置图标
         */
        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 设置node的icon
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChilds().size() > 0 && n.isExpand()) {
            // 展开的
            n.setIcon(R.mipmap.down);
        } else if (n.getChilds().size() > 0 && !n.isExpand()) {
            n.setIcon(R.mipmap.left);
        } else {
            n.setIcon(-1);
        }
    }


    /**
     * 获取排序后的node
     *
     * @param datas
     * @param <T>
     * @return
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLeve)
            throws IllegalAccessException {

        List<Node> result = new ArrayList<Node>();
        List<Node> nodes = convertDatas2Nodes(datas);
        // 获得树的根节点
        List<Node> rootNodes = getRootNode(nodes);

        for (Node n : rootNodes) {
            addNode(result, n, defaultExpandLeve, 1);
        }
        return result;
    }

    /**
     * 把一个节点的所有孩子都放入result
     *
     * @param result
     * @param n
     * @param defaultExpandLeve
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node n
            , int defaultExpandLeve, int currentLevel) {

        result.add(n);
        if (defaultExpandLeve >= currentLevel) {
            n.setExpand(true);
        }

        if (n.isLeaf())
            return;

        for (int i = 0; i < n.getChilds().size(); i++) {

            addNode(result, n.getChilds().get(i), defaultExpandLeve
                    , currentLevel + 1);
        }
    }

    /**
     * 从所有节点中过滤出根节点
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNode(List<Node> nodes) {

        List<Node> root = new ArrayList<Node>();
        for (Node n : nodes) {
            if (n.isRoot()) {
                root.add(n);
            }
        }
        return root;

    }

    /**
     * 设置过滤出可见的节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {

        List<Node> result = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

}
