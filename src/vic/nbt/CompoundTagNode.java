
package vic.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.TreeNode;
import org.jnbt.CompoundTag;
import org.jnbt.NBTConstants;
import org.jnbt.Tag;

public class CompoundTagNode implements TagNode {

    private final ArrayList<TagNodeBase> values = new ArrayList<TagNodeBase>();
    private final ArrayList<String> names = new ArrayList<String>();
    private TagNode parent;
    private String name;

    public CompoundTagNode(String name) {
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
        String out = getChildCount() + (getChildCount() == 1 ? " entry" : " entries");

        if (getParent() != null && getParent() instanceof ListTagNode) {
            return out;
        } else {
            return name + ": " + out;
        }
    }

    @Override
    public CompoundTagNode addNode(TagNodeBase node) {
        if (names.contains(node.getName())) {
            return this;
        }
        values.add(node);
        names.add(node.getName());
        node.setParent(this);
        Collections.sort(values, new NodeComperator());
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) throws IllegalArgumentException {
        if (name.equals(this.getName())) {
            return;
        } else if (getParent() != null && getParent().containsTagWithName(name)) {
            throw new IllegalArgumentException("Parent already contains a Tag by that name!");
        } else {
            String oldName = this.name;
            this.name = name;
            if (getParent() != null) {
                getParent().updateNode(this, oldName, name);
            }
        }
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(values);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return values.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return values.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return values.indexOf(node);
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
        return names.contains(name);
    }

    @Override
    public int getType() {
        return NBTConstants.TYPE_COMPOUND;
    }

    @Override
    public void setParent(TagNode tagNode) {
        this.parent = tagNode;
    }

    @Override
    public void updateNode(TagNodeBase node, String oldName, String newName) {
        names.set(names.indexOf(oldName), newName);
        node.setParent(this);
    }

    @Override
    public CompoundTagNode removeNode(TagNodeBase node) {
        names.remove(node.getName());
        values.remove(node);
        return this;
    }

    private static class NodeComperator implements Comparator<TagNodeBase> {

        private static ArrayList<Integer> order = new ArrayList<Integer>();

        static {
            //Just because TYPE_INT_ARRAY had to be after TYPE_COMPOUND...
            order.add(NBTConstants.TYPE_END);
            order.add(NBTConstants.TYPE_BYTE);
            order.add(NBTConstants.TYPE_SHORT);
            order.add(NBTConstants.TYPE_INT);
            order.add(NBTConstants.TYPE_LONG);
            order.add(NBTConstants.TYPE_FLOAT);
            order.add(NBTConstants.TYPE_DOUBLE);
            order.add(NBTConstants.TYPE_BYTE_ARRAY);
            order.add(NBTConstants.TYPE_INT_ARRAY);
            order.add(NBTConstants.TYPE_STRING);
            order.add(NBTConstants.TYPE_LIST);
            order.add(NBTConstants.TYPE_COMPOUND);
        }

        @Override
        public int compare(TagNodeBase o1, TagNodeBase o2) {
            if (o1 instanceof ListTagNode && o2 instanceof ListTagNode) {
                if (order.indexOf(((ListTagNode) o1).getTagType()) < order.indexOf(((ListTagNode) o2).getTagType())) {
                    return 1;
                }
                if (order.indexOf(((ListTagNode) o1).getTagType()) > order.indexOf(((ListTagNode) o2).getTagType())) {
                    return -1;
                } else {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            }
            if (order.indexOf(o1.getType()) <order.indexOf(o2.getType())) {
                return 1;
            }
            if (order.indexOf(o1.getType()) > order.indexOf(o2.getType())) {
                return -1;
            } else {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        }
    }

    @Override
    public Tag toNBTTag() {
        CompoundTag tag = new CompoundTag(getName(), new HashMap<String, Tag>());

        for (TagNodeBase node : values) {
            tag.putTag(node.toNBTTag());
        }

        return tag;
    }
}
