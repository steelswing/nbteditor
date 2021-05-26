
package vic.nbt;

import java.util.ArrayList;
import static org.jnbt.NBTConstants.TYPE_BYTE;
import static org.jnbt.NBTConstants.TYPE_BYTE_ARRAY;
import static org.jnbt.NBTConstants.TYPE_COMPOUND;
import static org.jnbt.NBTConstants.TYPE_DOUBLE;
import static org.jnbt.NBTConstants.TYPE_FLOAT;
import static org.jnbt.NBTConstants.TYPE_INT;
import static org.jnbt.NBTConstants.TYPE_INT_ARRAY;
import static org.jnbt.NBTConstants.TYPE_LIST;
import static org.jnbt.NBTConstants.TYPE_LONG;
import static org.jnbt.NBTConstants.TYPE_SHORT;

public class NodeFilter {

    private ArrayList<Integer> tagTypes;
    private String name;
    private boolean acceptAll;

    public static ArrayList<NodeFilter> ALL_FILTERS = new ArrayList<NodeFilter>();

    public static NodeFilter FILTER_ALL = new NodeFilter("All (Number, Arrays, String, Container)", new int[]{}, true);
    public static NodeFilter FILTER_NUMBER = new NodeFilter("Number (Integer, Floating point)", new int[]{TYPE_BYTE, TYPE_SHORT, TYPE_INT, TYPE_LONG, TYPE_FLOAT, TYPE_DOUBLE}, false);
    public static NodeFilter FILTER_INTEGER = new NodeFilter("Integer (Byte, Short, Int, Long)", new int[]{TYPE_BYTE, TYPE_SHORT, TYPE_INT, TYPE_LONG}, false);
    public static NodeFilter FILTER_FLOATING_POINT = new NodeFilter("Floating point (Float, Double)", new int[]{TYPE_FLOAT, TYPE_DOUBLE}, false);
    public static NodeFilter FILTER_ARRAYS = new NodeFilter("Arays (ByteArray, IntArray)", new int[]{TYPE_BYTE_ARRAY, TYPE_INT_ARRAY}, false);
    public static NodeFilter FILTER_CONTAINER = new NodeFilter("Container (List, Compound)", new int[]{TYPE_LIST, TYPE_COMPOUND}, false);
    public static NodeFilter FILTER_BYTE = new NodeFilter("Byte", new int[]{TYPE_BYTE}, false);
    public static NodeFilter FILTER_SHORT = new NodeFilter("Short", new int[]{TYPE_SHORT}, false);
    public static NodeFilter FILTER_INT = new NodeFilter("Int", new int[]{TYPE_INT}, false);
    public static NodeFilter FILTER_LONG = new NodeFilter("Long", new int[]{TYPE_LONG}, false);
    public static NodeFilter FILTER_FLOAT = new NodeFilter("Float", new int[]{TYPE_FLOAT}, false);
    public static NodeFilter FILTER_DOUBLE = new NodeFilter("Double", new int[]{TYPE_DOUBLE}, false);
    public static NodeFilter FILTER_BYTE_ARRAY = new NodeFilter("ByteArray", new int[]{TYPE_BYTE_ARRAY}, false);
    public static NodeFilter FILTER_INT_ARRAY = new NodeFilter("IntArray", new int[]{TYPE_INT_ARRAY}, false);
    public static NodeFilter FILTER_LIST = new NodeFilter("List", new int[]{TYPE_LIST}, false);
    public static NodeFilter FILTER_COMPOUND = new NodeFilter("Compound", new int[]{TYPE_COMPOUND}, false);

    private NodeFilter(String name, int[] tagTypes, boolean acceptAll) {
        this.tagTypes = new ArrayList<Integer>();
        for (int i : tagTypes) {
            this.tagTypes.add(i);
        }
        this.name = name;
        this.acceptAll = acceptAll;
        ALL_FILTERS.add(this);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean accept(TagNodeBase node) {
        if (acceptAll) {
            return true;
        }
        return tagTypes.contains(node.getType());
    }
}
