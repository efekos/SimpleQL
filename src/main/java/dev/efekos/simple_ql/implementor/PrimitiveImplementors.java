/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package dev.efekos.simple_ql.implementor;

import java.util.UUID;

public class PrimitiveImplementors {

    PrimitiveImplementors() {
        throw new UnsupportedOperationException();
    }

    public static final Implementor<String,String> STRING = new StringImplementor();
    public static final Implementor<Integer,Integer> INTEGER = new IntegerImplementor();
    public static final Implementor<Double,Double> DOUBLE = new DoubleImplementor();
    public static final Implementor<Float,Float> FLOAT = new FloatImplementor();
    public static final Implementor<Boolean,Boolean> BOOLEAN = new BooleanImplementor();
    public static final Implementor<Long,Long> LONG = new LongImplementor();
    public static final Implementor<Short,Short> SHORT = new ShortImplementor();
    public static final Implementor<Byte,Byte> BYTE = new ByteImplementor();
    public static final Implementor<UUID,String> UUID = new UUIDImplementor();

}