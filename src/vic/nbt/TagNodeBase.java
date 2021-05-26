
package vic.nbt;

import javax.swing.tree.TreeNode;
import org.jnbt.Tag;

public interface TagNodeBase extends TreeNode, Cloneable {

    public String getName();

    public int getType();

    public void setParent(TagNode tagNode);

    public void setName(String name) throws IllegalArgumentException;

    public Tag toNBTTag();

    public TagNodeBase clone();
}
