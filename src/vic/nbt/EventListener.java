
package vic.nbt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jnbt.NBTConstants;
import vic.nbt.layouts.VerticalLayout;

public class EventListener implements ActionListener {

    public static EventListener instance = new EventListener();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == NBTEditor.itemCompressOption) {
            // open new modal window
            final JDialog edit = new JDialog(NBTEditor.frame);
            edit.setTitle(NBTEditor.itemCompressOption.getText());
            edit.setModal(true);
            JPanel panel = new JPanel();

            Map<NBTComressType, JRadioButton> buttonsMap = new LinkedHashMap<>();
            for (NBTComressType value : NBTComressType.values()) {
                buttonsMap.put(value, new JRadioButton(value.name));
            }
            ArrayList<JRadioButton> buttonsList = new ArrayList<>(buttonsMap.values());

            ButtonGroup buttonGroup = new ButtonGroup();
            for (JRadioButton button : buttonsList) {
                buttonGroup.add(button);
            }
            // set selected current type
            buttonGroup.clearSelection();
            buttonsMap.get(NBTEditor.comressType).setSelected(true);

            MouseAdapter okEvent = new MouseAdapter() {
                private NBTComressType tempCompressType = NBTEditor.comressType;

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        int index = 0;

                        for (Enumeration<AbstractButton> enumeration = buttonGroup.getElements(); enumeration.hasMoreElements(); index++) {
                            JRadioButton button = (JRadioButton) enumeration.nextElement();
                            if (button.isSelected()) {
                                tempCompressType = NBTComressType.values()[index];
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    // apply compress type
                    NBTEditor.comressType = tempCompressType;
                    // close modal window
                    edit.dispose();
                }
            };
            MouseAdapter cancelEvent = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // close modal window
                    edit.dispose();
                }
            };

            // init swing listener
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        cancelEvent.mouseClicked(null);
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
                        return false;
                    }
                    if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                        okEvent.mouseClicked(null);
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
                    }
                    return false;
                }
            });
            JButton cancel = new JButton("Cancel");
            cancel.addMouseListener(cancelEvent);
            JButton ok = new JButton("OK");
            ok.addMouseListener(okEvent);

            panel.setLayout(new BorderLayout() {
                @Override
                public void layoutContainer(Container target) {
                    synchronized (target.getTreeLock()) {
                        for (int i = 0; i < target.getComponentCount(); i++) {
                            Component c = target.getComponent(i);
                            int w = target.getWidth();
                            int h = target.getHeight();

                            if (i == 0) {
                                c.setBounds(5, 5, w - 10, h - 40);
                            }
                            if (i == 1) {
                                c.setBounds(w - 75 - 75, h - 25, 70, 20);
                            }
                            if (i == 2) {
                                c.setBounds(w - 75, h - 25, 70, 20);
                            }
                        }
                    }
                }
            });

            { // create main panel & add buttons
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new VerticalLayout());
                for (JRadioButton button : buttonsList) {
                    contentPanel.add(button);
                }
                panel.add(contentPanel);
            }

            panel.add(cancel);
            panel.add(ok);

            edit.add(panel);
            edit.pack();

            edit.setSize(new Dimension(350, 160));
            edit.setLocationRelativeTo(NBTEditor.frame);
            edit.setVisible(true);
            return;
        }

        //A lot of quick-and-dirty code.
        if (e.getSource() == NBTEditor.itemRename || e.getSource() == NBTEditor.buttonRename) {
            NBTEditor.nbtTree.startEditingAtPath(NBTEditor.nbtTree.getSelectionPath());
            NBTEditor.modified = true;
            NBTEditor.updateName();
        } else if (e.getSource() == NBTEditor.itemEdit || e.getSource() == NBTEditor.buttonEdit) {
            NBTEditor.editCell();
        } else if (e.getSource() == NBTEditor.itemOpen || e.getSource() == NBTEditor.buttonOpen) {
            NBTEditor.open();
        } else if (e.getSource() == NBTEditor.itemCopy || e.getSource() == NBTEditor.itemCut) {
            NBTEditor.copy();
        } else if (e.getSource() == NBTEditor.itemPaste) {
            NBTEditor.paste();
        } else if (e.getSource() == NBTEditor.itemMoveUp || e.getSource() == NBTEditor.itemMoveDown) {
            int i = e.getSource() == NBTEditor.itemMoveUp ? -1 : 1;
            TagNodeBase node = (TagNodeBase) NBTEditor.nbtTree.getLastSelectedPathComponent();
            ListTagNode parent = (ListTagNode) node.getParent();
            parent.changeDestination(node, parent.getIndex(node) + i);
            ((DefaultTreeModel) NBTEditor.nbtTree.getModel()).nodeStructureChanged(parent);
            NBTEditor.nbtTree.setSelectionPath(new TreePath(((DefaultTreeModel) NBTEditor.nbtTree.getModel()).getPathToRoot(node)));
        } else if (e.getSource() == NBTEditor.itemAbout) {
            JEditorPane editor = new JEditorPane();
            editor.setContentType("text/html");
            editor.setEditable(false);
            editor.setBackground(new Color(0, 0, 0, 0));
            editor.setOpaque(false);
            editor.setText(
                    "<center><b>NBT Editor made by Victorious3 & steelswing</b></center><hr>" +
                    "Version: " + NBTEditor.version + "<br/>" +
                    "GitHub: <a href='https://github.com/steelswing/nbteditor'>https://github.com/steelswing/nbteditor</a><br/>" +
                    "Original GitHub: <a href='https://github.com/Victorious3/NBT-Editor'>https://github.com/Victorious3/NBT-Editor</a><br/>" +
                    "Uses a modified version of JNBT &copy <a href='http://jnbt.sourceforge.net/'>Graham Edgecombe</a><br/>" +
                    "<hr><center><a href='http://creativecommons.org/licenses/by-nc-sa/4.0/'><img src='http://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png'/></a></center>"
            );
            editor.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            JOptionPane.showMessageDialog(NBTEditor.frame, editor, "About", JOptionPane.PLAIN_MESSAGE);
        } else if (e.getSource() == NBTEditor.itemFind) {
            NBTEditor.search();
        } else if (e.getSource() == NBTEditor.itemFindNext) {
            NBTEditor.searchNext(false);
        } else if (e.getSource() == NBTEditor.itemReload || e.getSource() == NBTEditor.buttonReload) {
            NBTEditor.reload();
        } else if (e.getSource() == NBTEditor.itemNew || e.getSource() == NBTEditor.buttonNew) {
            NBTEditor.newFile();
        } else if (e.getSource() == NBTEditor.itemSave || e.getSource() == NBTEditor.buttonSave) {
            NBTEditor.save();
        } else if (e.getSource() == NBTEditor.itemSaveAs) {
            NBTEditor.saveAs();
        } else if (e.getSource() == NBTEditor.itemQuit) {
            NBTEditor.frame.dispatchEvent(new WindowEvent(NBTEditor.frame, WindowEvent.WINDOW_CLOSING));
        }

        if (e.getSource() == NBTEditor.itemDelete || e.getSource() == NBTEditor.buttonDelete || e.getSource() == NBTEditor.itemCut) {
            TagNodeBase node = (TagNodeBase) NBTEditor.nbtTree.getLastSelectedPathComponent();
            int index = node.getParent().getIndex(node);
            ((TagNode) ((TagNodeBase) NBTEditor.nbtTree.getLastSelectedPathComponent()).getParent()).removeNode(node);
            ((DefaultTreeModel) NBTEditor.nbtTree.getModel()).nodesWereRemoved(node.getParent(), new int[]{index}, new Object[]{node});
            if (index < node.getParent().getChildCount()) {
                NBTEditor.nbtTree.setSelectionPath(new TreePath(((DefaultTreeModel) NBTEditor.nbtTree.getModel()).getPathToRoot(node.getParent().getChildAt(index))));
            } else if (index > 0) {
                NBTEditor.nbtTree.setSelectionPath(new TreePath(((DefaultTreeModel) NBTEditor.nbtTree.getModel()).getPathToRoot(node.getParent().getChildAt(index - 1))));
            } else {
                NBTEditor.nbtTree.setSelectionPath(new TreePath(((DefaultTreeModel) NBTEditor.nbtTree.getModel()).getPathToRoot(node.getParent())));
            }
            NBTEditor.modified = true;
            NBTEditor.updateName();
        }

        int type = 0;
        boolean list = false;

        if (e.getSource() == NBTEditor.buttonTagByte) {
            type = NBTConstants.TYPE_BYTE;
        } else if (e.getSource() == NBTEditor.buttonTagShort) {
            type = NBTConstants.TYPE_SHORT;
        } else if (e.getSource() == NBTEditor.buttonTagInt) {
            type = NBTConstants.TYPE_INT;
        } else if (e.getSource() == NBTEditor.buttonTagLong) {
            type = NBTConstants.TYPE_LONG;
        } else if (e.getSource() == NBTEditor.buttonTagFloat) {
            type = NBTConstants.TYPE_FLOAT;
        } else if (e.getSource() == NBTEditor.buttonTagDouble) {
            type = NBTConstants.TYPE_DOUBLE;
        } else if (e.getSource() == NBTEditor.buttonTagByteArray) {
            type = NBTConstants.TYPE_BYTE_ARRAY;
        } else if (e.getSource() == NBTEditor.buttonTagIntArray) {
            type = NBTConstants.TYPE_INT_ARRAY;
        } else if (e.getSource() == NBTEditor.buttonTagString) {
            type = NBTConstants.TYPE_STRING;
        } else if (e.getSource() == NBTEditor.buttonTagCompound) {
            type = NBTConstants.TYPE_COMPOUND;
        } else if (e.getSource() == NBTEditor.itemTagByte) {
            type = NBTConstants.TYPE_BYTE;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagShort) {
            type = NBTConstants.TYPE_SHORT;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagInt) {
            type = NBTConstants.TYPE_INT;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagLong) {
            type = NBTConstants.TYPE_LONG;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagFloat) {
            type = NBTConstants.TYPE_FLOAT;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagDouble) {
            type = NBTConstants.TYPE_DOUBLE;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagByteArray) {
            type = NBTConstants.TYPE_BYTE_ARRAY;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagIntArray) {
            type = NBTConstants.TYPE_INT_ARRAY;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagString) {
            type = NBTConstants.TYPE_STRING;
            list = true;
        } else if (e.getSource() == NBTEditor.itemTagCompound) {
            type = NBTConstants.TYPE_COMPOUND;
            list = true;
        }

        if (type != 0) {
            TagNodeBase node;
            if (list) {
                node = new ListTagNode(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())), type);
            } else {
                if (type == NBTConstants.TYPE_INT_ARRAY) {
                    node = new TagLeaf(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())), type, new int[0]);
                } else if (type == NBTConstants.TYPE_BYTE_ARRAY) {
                    node = new TagLeaf(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())), type, new byte[0]);
                } else if (type == NBTConstants.TYPE_STRING) {
                    node = new TagLeaf(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())), type, "");
                } else if (type == NBTConstants.TYPE_COMPOUND) {
                    node = new CompoundTagNode(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())));
                } else {
                    node = new TagLeaf(Utils.getNextFreeName(((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent())), type, 0);
                    try {
                        //TODO This is rather inconvenient.
                        ((TagLeaf) node).setValue("0");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            TagNode parent = ((TagNode) NBTEditor.nbtTree.getLastSelectedPathComponent());
            parent.addNode(node);
            TreePath path = new TreePath(((DefaultTreeModel) NBTEditor.nbtTree.getModel()).getPathToRoot(node));
            ((DefaultTreeModel) NBTEditor.nbtTree.getModel()).nodeChanged(parent);
            ((DefaultTreeModel) NBTEditor.nbtTree.getModel()).nodesWereInserted(parent, new int[]{parent.getIndex(node)});
            NBTEditor.nbtTree.setSelectionPath(path);
            NBTEditor.nbtTree.startEditingAtPath(path);

            NBTEditor.modified = true;
            NBTEditor.updateName();
        }
    }
}
