package ru.skillbench.tasks.javaapi.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNodeImpl implements TreeNode{
    private TreeNode parent;
    private List<TreeNode> children;
    private boolean isExpanded;
    private Object data;

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public TreeNode getRoot() {
        TreeNode root = getParent();
        if(root != null) {
            while(root.getParent() != null) {
                root = root.getParent();
            }
        }
        return root;
    }

    @Override
    public boolean isLeaf() {
        return children == null || children.size() == 0;
    }

    @Override
    public int getChildCount() {
        return isLeaf() ? 0 : children.size();
    }

    @Override
    public Iterator<TreeNode> getChildrenIterator() {
        return children.iterator();
    }

    @Override
    public void addChild(TreeNode child) {
        if(isLeaf()) {
            children = new LinkedList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    @Override
    public boolean removeChild(TreeNode child) {
        Iterator<TreeNode> iterator = getChildrenIterator();
        TreeNode childNode;
        while(iterator.hasNext()) {
            if((childNode = iterator.next()).equals(child)) {
                childNode.setParent(null);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        if(!isLeaf()){
            Iterator<TreeNode> iterator = getChildrenIterator();
            while(iterator.hasNext()) {
                iterator.next().setExpanded(expanded);
            }
        }
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String getTreePath() {
        StringBuilder treePath = new StringBuilder(getData() == null ? "empty" : getData().toString());
        if(getParent() != null) {
            return treePath.insert(0, getParent().getTreePath() + "->").toString();
        }
        return treePath.toString();
    }

    @Override
    public TreeNode findParent(Object data) {
        if((getData() == null && data == null) || (getData() != null && getData().equals(data))) {
            return this;
        }
        if(getParent() != null) {
            return getParent().findParent(data);
        }
        return null;
    }

    @Override
    public TreeNode findChild(Object data) {
        if(children != null) {
            for(TreeNode child : children) {
                if((data == null && child.getData() == null) ||
                        (child.getData() != null && child.getData().equals(data)) ||
                        ((child = child.findChild(data)) != null)) {
                    return child;
                }
            }
        }
        return null;
    }
}