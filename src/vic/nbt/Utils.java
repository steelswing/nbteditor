
package vic.nbt;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.KeyStroke;
import org.jnbt.NBTConstants;

public class Utils {

    public static File getFileFromJar(String s) {
        try {
            return new File(Utils.class.getResource(s).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage getImage(String s) {
        if (s.startsWith("/")) {
            try {
                InputStream in = Utils.class.getResourceAsStream(s);
                BufferedImage img = ImageIO.read(in);
                in.close();
                return img;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return ImageIO.read(new File(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String toString(KeyStroke k) {
        if (k.getModifiers() != 0) {
            return KeyEvent.getModifiersExText(k.getModifiers()) + "+ " + KeyEvent.getKeyText(k.getKeyCode());
        } else {
            return KeyEvent.getKeyText(k.getKeyCode());
        }
    }

    public static String getTagName(int tagType) {
        switch (tagType) {
            case NBTConstants.TYPE_BYTE:
                return "TagByte";
            case NBTConstants.TYPE_SHORT:
                return "TagShort";
            case NBTConstants.TYPE_INT:
                return "TagInt";
            case NBTConstants.TYPE_LONG:
                return "TagLong";
            case NBTConstants.TYPE_FLOAT:
                return "TagFloat";
            case NBTConstants.TYPE_DOUBLE:
                return "TagDouble";
            case NBTConstants.TYPE_BYTE_ARRAY:
                return "TagByteArray";
            case NBTConstants.TYPE_INT_ARRAY:
                return "TagIntArray";
            case NBTConstants.TYPE_STRING:
                return "TagString";
            case NBTConstants.TYPE_LIST:
                return "TagList";
            case NBTConstants.TYPE_COMPOUND:
                return "TagCompound";
            default:
                return "UNKNOWN";
        }
    }

    public static String getNextFreeName(TagNode tag) {
        int x = 1;
        String name = "property" + x;
        do {
            name = "property" + x;
            x++;
        } while (tag.containsTagWithName(name));

        return name;
    }

    public static String getExtension(File file) {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i + 1);
        }
        return extension;
    }

    public static boolean compare(Object o1, String o2) {
        if (o1 instanceof Number) {
            try {
                return new BigDecimal(o1.toString()).equals(new BigDecimal(o2));
            } catch (Exception e) {
                return false;
            }
        } else if (o1 instanceof byte[] || o1 instanceof int[]) {
            return false;
        } else {
            return o1.toString().equals(o2);
        }
    }
}
