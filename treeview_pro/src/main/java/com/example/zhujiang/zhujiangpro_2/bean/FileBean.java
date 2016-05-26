package com.example.zhujiang.zhujiangpro_2.bean;

import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodeId;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodeLabel;
import com.example.zhujiang.zhujiangpro_2.treeview.utils.annotation.TreeNodePid;

/**
 * Created by zhujiang on 16-5-25.
 */
public class FileBean {

    @TreeNodeId
    private int id;

    @TreeNodePid
    private int pid;

    @TreeNodeLabel
    private String label;

    private String desc;

    public FileBean() {

    }

    public FileBean(int id, int pid, String label) {
        this.id = id;
        this.pid = pid;
        this.label = label;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
