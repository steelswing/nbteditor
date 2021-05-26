
package vic.nbt;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

public class TagNodeCellEditor extends DefaultTreeCellEditor {

    public TagNodeCellEditor(JTree tree, TreeCellRenderer treeCellRenderer) {
        super(tree, (DefaultTreeCellRenderer) treeCellRenderer);
    }

    @Override
    public Component getTreeCellEditorComponent(final JTree tree, final Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        cancelCellEditing();

        final JTextField text = new JTextField(((TagNodeBase) value).getName());
        text.setSize(text.getPreferredSize());

        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stopCellEditing();
                } catch (ClassCastException e2) {
                    //This is not very elegant if you come up with a better method go for it.
                }
            }
        });
        text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    renameCell(value, text.getText());
                    ((DefaultTreeModel) tree.getModel()).nodeChanged((TreeNode) value);
                } catch (IllegalArgumentException e2) {
                    Toolkit.getDefaultToolkit().beep();
                }
                //Please tell my why I don't have to stop editing here ?!
            }
        });
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                text.setSize(text.getPreferredSize());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                text.setSize(text.getPreferredSize());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                text.setSize(text.getPreferredSize());
            }
        });

        return text;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            return false;
        }
        if (tree.getLastSelectedPathComponent() != null && tree.getLastSelectedPathComponent() instanceof TagLeaf) {
            TagLeaf cell = (TagLeaf) tree.getLastSelectedPathComponent();
            if (cell.getParent() != null && cell.getParent() instanceof ListTagNode) {
                return false;
            }
        }
        return true;
    }

    private void renameCell(Object cell, String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Length of name can't be 0!");
        }
        ((TagNodeBase) cell).setName(name);
    }
}
