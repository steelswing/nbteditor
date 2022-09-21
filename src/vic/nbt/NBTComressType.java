/*
Ну вы же понимаете, что код здесь только мой?
Well, you do understand that the code here is only mine?
 */

package vic.nbt;

/**
 * File: NBTComressType.java
 * Created on 22.09.2022, 6:05:28
 *
 * @author LWJGL2
 */
public enum NBTComressType {

    NO_COMRESS("No compress"),
    GZIP_COMRESS("Gzip compress"),
    OPTIMIZED_GZIP_COMPRESS("Optimized Gzip compress");
    
    public final String name;

    private NBTComressType(String name) {
        this.name = name;
    }
}
