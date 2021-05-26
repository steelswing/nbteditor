
package vic.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import org.jnbt.ListTag;
import org.jnbt.NBTConstants;
import org.jnbt.NBTUtils;
import org.jnbt.Tag;

public class ListTagNode implements TagNode {

    private final int type;
    private ArrayList<TagNodeBase> value = new ArrayList<TagNodeBase>();
    private TagNode parent;
    private String name;

    public ListTagNode(String name, int tagType) {
        this.type = tagType;
        this.name = name;
    }

    public TagNodeBase clone() {
        try {
            return (TagNodeBase) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        String out = getChildCount() + (getChildCount() == 1 ? " entry" : " entries") + " of type " + Utils.getTagName(type);

        if (getParent() != null && getParent() instanceof ListTagNode) {
            return out;
        } else {
            return name + ": " + out;
        }
    }

    @Override
    public ListTagNode addNode(TagNodeBase node) {
        if (node.getType() == type) {
            value.add(node);
            node.setParent(this);
        } else {
            throw new IllegalArgumentException("Wrong Tag type!");
        }
        return this;
    }

    public void changeDestination(TagNodeBase node, int newDestination) {
        value.remove(node);
        value.add(newDestination, node);
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(value);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return value.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return value.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return value.indexOf(node);
    }

    @Override
    public TagNode getParent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    @Override
    public boolean containsTagWithName(String name) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) throws IllegalArgumentException {
        if (name.equals(this.getName())) {
            return;
        } else if (getParent().containsTagWithName(name)) {
            throw new IllegalArgumentException("Parent already contains a Tag by that name!");
        } else {
            String oldName = this.name;
            this.name = name;
            getParent().updateNode(this, oldName, name);
        }
    }

    @Override
    public int getType() {
        return NBTConstants.TYPE_LIST;
    }

    public int getTagType() {
        return type;
    }

    @Override
    public void setParent(TagNode tagNode) {
        this.parent = tagNode;
    }

    @Override public void updateNode(TagNodeBase node, String oldName, String newName) {
    }

    @Override
    public ListTagNode removeNode(TagNodeBase node) {
        value.remove(node);
        return this;
    }

    @Override
    public Tag toNBTTag() {
        ListTag tag = new ListTag(getName(), NBTUtils.getTypeClass(getTagType()), new ArrayList<Tag>());

        for (TagNodeBase node : value) {
            tag.addTag(node.toNBTTag());
        }

        return tag;
    }
}
