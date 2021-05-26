
package org.jnbt;

/*
 * JNBT License
 *
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the JNBT team nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The <code>TAG_Compound</code> tag.
 *
 * @author Graham Edgecombe
 *
 */
public final class CompoundTag extends Tag {

    /**
     * The value.
     */
    public final Map<String, Tag> value;

    /**
     * Creates the tag.
     *
     * @param name
     * The name.
     * @param value
     * The value.
     */
    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = value; //unmodifiable?
    }

    @Override
    public Map<String, Tag> getValue() {
        return value;
    }

    @Override
    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound" + append + ": " + value.size() +
                " entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : value.entrySet()) {
            bldr.append("   " +
                    entry.getValue().toString().replaceAll("\r\n", "\r\n   ") +
                    "\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }

    //Some custom methods
    public CompoundTag putTag(Tag tag) {
        value.put(tag.getName(), tag);
        return this;
    }

    public CompoundTag putString(String name, String string) {
        value.put(name, new StringTag(name, string));
        return this;
    }

    public CompoundTag putInt(String name, int ainteger) {
        value.put(name, new IntTag(name, ainteger));
        return this;
    }

    public CompoundTag putIntArray(String name, int[] aintArray) {
        value.put(name, new IntArrayTag(name, aintArray));
        return this;
    }

    public CompoundTag putByte(String name, byte abyte) {
        value.put(name, new ByteTag(name, abyte));
        return this;
    }

    public CompoundTag putByteArray(String name, byte[] abyteArray) {
        value.put(name, new ByteArrayTag(name, abyteArray));
        return this;
    }

    public CompoundTag putDouble(String name, double adouble) {
        value.put(name, new DoubleTag(name, adouble));
        return this;
    }

    public CompoundTag putLong(String name, long along) {
        value.put(name, new LongTag(name, along));
        return this;
    }

    public CompoundTag putShort(String name, short ashort) {
        value.put(name, new ShortTag(name, ashort));
        return this;
    }

    public CompoundTag putBoolean(String name, boolean aboolean) {
        value.put(name, new ByteTag(name, (byte) (aboolean ? 1 : 0)));
        return this;
    }

    //Same for getters
    public CompoundTag getCompoundTag(String name) {
        return value.containsKey(name) ? (CompoundTag) value.get(name) : new CompoundTag(name, new LinkedHashMap<String, Tag>());
    }

    public ListTag getListTag(String name) {
        return value.containsKey(name) ? (ListTag) value.get(name) : new ListTag(name, Tag.class, new ArrayList<Tag>());
    }

    public String getString(String name, String def) {
        return value.containsKey(name) ? (String) value.get(name).getValue() : def;
    }

    public int getInt(String name, int def) {
        return value.containsKey(name) ? (Integer) value.get(name).getValue() : def;
    }

    public int[] getIntArray(String name, int[] def) {
        return value.containsKey(name) ? (int[]) value.get(name).getValue() : def;
    }

    public byte getByte(String name, byte def) {
        return value.containsKey(name) ? (Byte) value.get(name).getValue() : def;
    }

    public byte[] getByteArray(String name, byte[] def) {
        return value.containsKey(name) ? (byte[]) value.get(name).getValue() : def;
    }

    public double getDouble(String name, double def) {
        return value.containsKey(name) ? (Double) value.get(name).getValue() : def;
    }

    public long getLong(String name, long def) {
        return value.containsKey(name) ? (Long) value.get(name).getValue() : def;
    }

    public short getShort(String name, short def) {
        return value.containsKey(name) ? (Short) value.get(name).getValue() : def;
    }

    //I can't get why booleans are not supported.
    public boolean getBoolean(String name, boolean def) {
        return value.containsKey(name) ? ((Byte) value.get(name).getValue()) == 1 : def;
    }
}
