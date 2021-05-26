
package vic.nbt;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jnbt.NBTConstants;

public class TagNodeRenderer extends DefaultTreeCellRenderer {

    public static Icon iconByte = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_black.png"));
    public static Icon iconShort = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_red.png"));
    public static Icon iconInt = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_orange.png"));
    public static Icon iconLong = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_yellow.png"));
    public static Icon iconFloat = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_pink.png"));
    public static Icon iconDouble = new ImageIcon(Utils.getImage("/vic/nbt/resources/bullet_purple.png"));
    public static Icon iconByteArray = new ImageIcon(Utils.getImage("/vic/nbt/resources/arrows_black.png"));
    public static Icon iconIntArray = new ImageIcon(Utils.getImage("/vic/nbt/resources/arrows_orange.png"));
    public static Icon iconString = new ImageIcon(Utils.getImage("/vic/nbt/resources/text_smallcaps.png"));
    public static Icon iconList = new ImageIcon(Utils.getImage("/vic/nbt/resources/note.png"));
    public static Icon iconCompound = new ImageIcon(Utils.getImage("/vic/nbt/resources/package.png"));
    public static Icon woodenBox = new ImageIcon(Utils.getImage("/vic/nbt/resources/wooden-box.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        switch (((TagNodeBase) value).getType()) {
            case NBTConstants.TYPE_BYTE:
                setIcon(iconByte);
                break;
            case NBTConstants.TYPE_SHORT:
                setIcon(iconShort);
                break;
            case NBTConstants.TYPE_INT:
                setIcon(iconInt);
                break;
            case NBTConstants.TYPE_LONG:
                setIcon(iconLong);
                break;
            case NBTConstants.TYPE_FLOAT:
                setIcon(iconFloat);
                break;
            case NBTConstants.TYPE_DOUBLE:
                setIcon(iconDouble);
                break;
            case NBTConstants.TYPE_BYTE_ARRAY:
                setIcon(iconByteArray);
                break;
            case NBTConstants.TYPE_INT_ARRAY:
                setIcon(iconIntArray);
                break;
            case NBTConstants.TYPE_STRING:
                setIcon(iconString);
                break;
            case NBTConstants.TYPE_LIST:
                setIcon(iconList);
                break;
            case NBTConstants.TYPE_COMPOUND:
                setIcon(iconCompound);
                break;
        }

        if (row == 0) {
            setIcon(woodenBox);
        }

        return this;
    }
}
