/*
 *
 *  * Copyright 2019 The Netty Project
 *  *
 *  * The Netty Project licenses this file to you under the Apache License,
 *  * version 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at:
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations
 *  * under the License.
 *
 */

package io.netty.handler.codec.quic.tls;

public enum Cryptor {

    //TODO
    ONE_RTT {
        @Override
        public byte[] decryptHeader(byte[] sample, byte firstByte, byte[] pn, boolean isShort) {
            return new byte[0];
        }

        @Override
        public byte[] decryptContent(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] seal(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] encryptHeader(byte[] sample, byte[] header, boolean isShort) {
            return new byte[0];
        }
    },
    INITIAL {
        @Override
        public byte[] decryptHeader(byte[] sample, byte firstByte, byte[] pn, boolean isShort) {
            return new byte[0];
        }

        @Override
        public byte[] decryptContent(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] seal(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] encryptHeader(byte[] sample, byte[] header, boolean isShort) {
            return new byte[0];
        }
    },
    HANDSHAKE {
        @Override
        public byte[] decryptHeader(byte[] sample, byte firstByte, byte[] pn, boolean isShort) {
            return new byte[0];
        }

        @Override
        public byte[] decryptContent(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] seal(byte[] encrypted, long packetNumber, byte[] header) {
            return new byte[0];
        }

        @Override
        public byte[] encryptHeader(byte[] sample, byte[] header, boolean isShort) {
            return new byte[0];
        }
    };

    public abstract byte[] decryptHeader(byte[] sample, byte firstByte, byte[] pn, boolean isShort);

    public abstract byte[] decryptContent(byte[] encrypted, long packetNumber, byte[] header);

    public abstract byte[] seal(byte[] encrypted, long packetNumber, byte[] header);

    public abstract byte[] encryptHeader(byte[] sample, byte[] header, boolean isShort);
}