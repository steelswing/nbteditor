
package vic.nbt;

import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import org.jnbt.ByteArrayTag;
import org.jnbt.ByteTag;
import org.jnbt.DoubleTag;
import org.jnbt.FloatTag;
import org.jnbt.IntArrayTag;
import org.jnbt.IntTag;
import org.jnbt.LongTag;
import org.jnbt.NBTConstants;
import org.jnbt.ShortTag;
import org.jnbt.StringTag;
import org.jnbt.Tag;

public class TagLeaf implements TagNodeBase {

    private Object value;
    private String name;
    private final int tagType;
    private TagNode parent;

    public TagLeaf(String tagName, int tagType, Object value) {
        this.value = value;
        this.name = tagName;
        this.tagType = tagType;
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
        String out = "";

        if (tagType == NBTConstants.TYPE_BYTE_ARRAY) {
            out = Arrays.toString((byte[]) value);
        } else if (tagType == NBTConstants.TYPE_INT_ARRAY) {
            out = Arrays.toString((int[]) value);
        } else {
            out = value.toString();
        }

        if (getParent() != null && getParent() instanceof ListTagNode) {
            return out;
        } else {
            return name + ": " + out;
        }
    }

    public void setParent(TagNode parent) {
        this.parent = parent;
    }

    @Override
    public void setName(String name) throws IllegalArgumentException {
        if (name.equals(this.getName())) {
            return;
        } else if (getParent() == null) {
            this.name = name;
        } else if (getParent().containsTagWithName(name)) {
            throw new IllegalArgumentException("Parent already contains a Tag by that name!");
        } else {
            String oldName = this.name;
            this.name = name;
            getParent().updateNode(this, oldName, name);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public int getType() {
        return tagType;
    }

    public void setValue(String value) throws Exception {
        switch (tagType) {
            case NBTConstants.TYPE_BYTE:
                this.value = Byte.parseByte(value);
                break;
            case NBTConstants.TYPE_SHORT:
                this.value = Short.parseShort(value);
                break;
            case NBTConstants.TYPE_INT:
                this.value = Integer.parseInt(value);
                break;
            case NBTConstants.TYPE_DOUBLE:
                this.value = Double.parseDouble(value);
                break;
            case NBTConstants.TYPE_LONG:
                this.value = Long.parseLong(value);
                break;
            case NBTConstants.TYPE_FLOAT:
                this.value = Float.parseFloat(value);
                break;
            case NBTConstants.TYPE_STRING:
                this.value = value;
                break;
            case NBTConstants.TYPE_INT_ARRAY:
                value = value.replaceAll("[\\[\\]]", "");
                String[] s2 = value.split(",");
                this.value = new int[s2.length];
                for (int i = 0; i < s2.length; i++) {
                    String s3 = s2[i].replaceAll(" ", "");
                    ((int[]) this.value)[i] = Integer.parseInt(s3);
                }
                break;
            case NBTConstants.TYPE_BYTE_ARRAY:
                value = value.replaceAll("[\\[\\]]", "");
                String[] s3 = value.split(",");
                this.value = new byte[s3.length];
                for (int i = 0; i < s3.length; i++) {
                    String s4 = s3[i].replaceAll(" ", "");
                    ((byte[]) this.value)[i] = Byte.parseByte(s4);
                }
                break;
            default:
                throw new Exception("Illegal tag type.");
        }
    }

    public String getValueAsString() {
        if (value instanceof byte[]) {
            return Arrays.toString((byte[]) value);
        } else if (value instanceof int[]) {
            return Arrays.toString((int[]) value);
        } else {
            return value.toString();
        }
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Enumeration children() {
        return null;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    @Override
    public TagNode getParent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Tag toNBTTag() {
        switch (tagType) {
            case NBTConstants.TYPE_BYTE:
                return new ByteTag(getName(), (Byte) value);
            case NBTConstants.TYPE_SHORT:
                return new ShortTag(getName(), (Short) value);
            case NBTConstants.TYPE_INT:
                return new IntTag(getName(), (Integer) value);
            case NBTConstants.TYPE_LONG:
                return new LongTag(getName(), (Long) value);
            case NBTConstants.TYPE_FLOAT:
                return new FloatTag(getName(), (Float) value);
            case NBTConstants.TYPE_DOUBLE:
                return new DoubleTag(getName(), (Double) value);
            case NBTConstants.TYPE_BYTE_ARRAY:
                return new ByteArrayTag(getName(), (byte[]) value);
            case NBTConstants.TYPE_STRING:
                return new StringTag(getName(), (String) value);
            case NBTConstants.TYPE_INT_ARRAY:
                return new IntArrayTag(getName(), (int[]) value);
            default:
                throw new IllegalArgumentException("Invalid tag type!");
        }
    }
}
