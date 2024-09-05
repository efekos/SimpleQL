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

import dev.efekos.simple_ql.data.GetterAction;
import dev.efekos.simple_ql.data.SetterAction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BooleanTSImplementor implements Implementor<Boolean,String> {

    @Override
    public String write(Boolean value) {
        return value?"1b":"0b";
    }

    @Override
    public Boolean read(String value) {
        return value.equals("1b");
    }

    @Override
    public SetterAction<String> setter() {
        return PreparedStatement::setString;
    }

    @Override
    public GetterAction<String> getter() {
        return ResultSet::getString;
    }

    @Override
    public String type() {
        return "TEXT";
    }
}
