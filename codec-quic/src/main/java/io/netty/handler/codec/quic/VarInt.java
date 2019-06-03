/*
 * Copyright 2019 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.handler.codec.quic;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.quic.packet.HeaderUtil;

import java.util.Arrays;

public class VarInt {

    private int mask, size, rest;
    private byte[] rawValue;
    private long value;

    protected VarInt() {}

    private int getFrom() {
        if (value > 1073741823) {
            return  0;
        } else if (value > 16383) {
            return 4;
        } else if (value > 63) {
            return 6;
        }
        return 7;
    }

    public void write(ByteBuf buf) {
        byte[] bin = Arrays.copyOfRange(rawValue, getFrom(), 8);
        bin[0] = (byte) (bin[0] | mask);
        buf.writeBytes(bin);
    }

    public void read(long value) {
        this.value = value;
        readValue(value);
        mask = maskByValue(value);
        //TODO should we set size and rest?
    }

    private int maskByValue(long value) {
        if (value > 1073741823) {
            return 0xc0;
        } else if (value > 16383) {
            return 0x80;
        } else if (value > 63) {
            return 0x40;
        }
        return 0x0;
    }

    private void readValue(long value) {
        rawValue = new byte[8];
        for(int i = 7; i >= 0; --i) {
            rawValue[i] = (byte)((int)(value & 255L));
            value >>= 8;
        }
    }

    public void readBuf(ByteBuf buf) {
        mask = buf.readByte() & 0xFF;
        size = (mask & 0xc0) & 0xFF;
        rest = (mask & 0x3f) & 0xFF;

        int totalLength = (int) Math.pow(2, size >> 6) - 1;

        byte[] bin = HeaderUtil.read(buf, totalLength);
        byte[] pad = new byte[7 - totalLength];

        //add padding, rest and binary to result
        rawValue = new byte[pad.length + 1 + bin.length];
        System.arraycopy(pad, 0, rawValue, 0, pad.length);
        rawValue[pad.length] = (byte) rest;
        System.arraycopy(bin, 0, rawValue, pad.length + 1, bin.length);
        if (rawValue.length != 8) throw new IllegalStateException("Invalid VarInt size");
        value = ((long)rawValue[0] & 255L) << 56 |
                ((long)rawValue[1] & 255L) << 48 |
                ((long)rawValue[2] & 255L) << 40 |
                ((long)rawValue[3] & 255L) << 32 |
                ((long)rawValue[4] & 255L) << 24 |
                ((long)rawValue[5] & 255L) << 16 |
                ((long)rawValue[6] & 255L) << 8 |
                (long)rawValue[7] & 255L;
    }

    public static VarInt read(ByteBuf buf) {
        VarInt var = new VarInt();
        var.readBuf(buf);
        return var;
    }

    public static VarInt byLong(long value) {
        VarInt var = new VarInt();
        var.read(value);
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VarInt varInt = (VarInt) o;

        if (value != varInt.value) return false;
        return Arrays.equals(rawValue, varInt.rawValue);
    }

    @Override
    public String toString() {
        return "VarInt{" +
                "rawValue=" + Arrays.toString(rawValue) +
                ", value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        int result = mask;
        result = 31 * result + size;
        result = 31 * result + rest;
        result = 31 * result + Arrays.hashCode(rawValue);
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    public void mask(int mask) {
        this.mask = mask;
    }

    public int size() {
        return size;
    }

    public void size(int size) {
        this.size = size;
    }

    public int rest() {
        return rest;
    }

    public void rest(int rest) {
        this.rest = rest;
    }

    public byte[] rawValue() {
        return rawValue;
    }

    public void rawValue(byte[] rawValue) {
        this.rawValue = rawValue;
    }

    public long value() {
        return value;
    }

    public void value(long value) {
        this.value = value;
    }

    public int mask() {
        return mask;
    }

    public int asInt() {
        return (int) value;
    }

    public byte asByte() {
        return (byte) value;
    }

    public long asLong() {
        return value;
    }

}