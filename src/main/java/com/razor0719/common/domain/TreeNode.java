package com.razor0719.common.domain;

/**
 * 树节点
 *
 * @author baoyl
 * @created 2018/5/3
 */
public class TreeNode<T> extends BaseBean {
    private static final long serialVersionUID = -4077971130566291777L;

    public final T data;
    public final TreeNode<T> father;
    public final int depth;

    /**
     * root
     * @param data
     */
    public TreeNode (T data) {
        this.data = data;
        father = null;
        depth = 0;
    }

    /**
     * node
     * @param father
     * @param data
     */
    public TreeNode (TreeNode<T> father, T data) {
        this.data = data;
        this.father = father;
        this.depth = father.depth + 1;
    }

    /**
     * 是否闭环
     * @return boolean
     */
    public boolean isClosedLoop() {
        TreeNode<T> treeNode = father;
        while (treeNode != null) {
            if(equals(treeNode)) {
                return true;
            }
            treeNode = treeNode.father;
        }
        return false;
    }

    /**
     * 是否引用自身闭环
     * @return boolean
     */
    public boolean isSelfLoop() {
        TreeNode<T> treeNode = father;
        while (treeNode != null) {
            if(data == treeNode.data) {
                return true;
            }
            treeNode = treeNode.father;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TreeNode<?> other = (TreeNode<?>) obj;
        if (data == null) {
            return other.data == null;
        } else {
            return data.equals(other.data);
        }
    }
}
