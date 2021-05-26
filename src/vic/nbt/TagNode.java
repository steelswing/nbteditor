
package vic.nbt;

public interface TagNode extends TagNodeBase {

    public boolean containsTagWithName(String name);

    public void updateNode(TagNodeBase node, String oldName, String newName);

    public TagNode removeNode(TagNodeBase node);

    public TagNode addNode(TagNodeBase node);
}
