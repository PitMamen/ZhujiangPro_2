package com.example.zhujiang.zhujiangpro_2.treeview.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点对象
 * Created by zhujiang on 16-5-25.
 */
public class Node {


    private int id;
    /**
     * 当前节点的父节点
     */
    private int pid = 0;
    private String name;
    /**
     * 树的层级
     */
    private int level;

    /**
     * 是否是展开的
     */
    private boolean isExpand = false;

    private int icon;

    private Node parent;
    private List<Node> childs = new ArrayList<Node>();

    public Node() {
    }

    public Node(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取当前节点的层级
     *
     * @return
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!isExpand) {
            for (Node child : childs) {
                child.setExpand(false);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void setChilds(List<Node> childs) {
        this.childs = childs;
    }

    /**
     * 是否是根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断当前父节点是否为展开的
     *
     * @return
     */
    public boolean isParentExpand() {

        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否为叶节点（子节点）
     *
     * @return
     */
    public boolean isLeaf() {
        return childs.size() == 0;
    }


}
