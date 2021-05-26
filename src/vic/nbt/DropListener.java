
package vic.nbt;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

public class DropListener implements DropTargetListener {

    @Override public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override public void dragExit(DropTargetEvent dte) {
    }

    @Override public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        if (dtde.getCurrentDataFlavorsAsList().size() > 1) {
            dtde.rejectDrop();
        } else {
            DataFlavor flavor = dtde.getCurrentDataFlavorsAsList().get(0);
            if (flavor.isFlavorJavaFileListType()) {
                dtde.acceptDrop(DnDConstants.ACTION_LINK);
                try {
                    List<File> files = (List<File>) dtde.getTransferable().getTransferData(flavor);
                    if (files.size() == 1) {
                        File file = files.get(0);
                        NBTEditor.file = file;
                        NBTEditor.reload();
                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.dropComplete(false);
                }
            } else {
                dtde.rejectDrop();
            }
        }
    }

    @Override public void dropActionChanged(DropTargetDragEvent dtde) {
    }
}
