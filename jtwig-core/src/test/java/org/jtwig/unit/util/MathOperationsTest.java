/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.unit.util;

import static org.jtwig.util.MathOperations.areDouble;
import static org.jtwig.util.MathOperations.intDiv;
import static org.jtwig.util.MathOperations.mod;
import static org.jtwig.util.MathOperations.sum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MathOperationsTest {
    @Test
    public void sumDoubleTest() throws Exception {
        assertEquals(3.0, sum(1.0, 2.0));
    }
    @Test
    public void modTest() throws Exception {
        assertEquals(0, mod(4.0, 2.0));
    }
    @Test
    public void intDivDoubleTest() throws Exception {
        assertEquals(1, intDiv(2.0, 2.0));
    }
    @Test
    public void areDoubleTest() {
    	assertTrue(areDouble(1.0,2.0));
    }
    @Test
    public void areDoubleWithStringAndDoubleTest() {
    	assertFalse(areDouble("1.0",2.0));
    }
    @Test
    public void areDoubleEmptyParameterListTest() {
    	assertFalse(areDouble());
    }
    @Test
    public void areDoubleNullParameterListTest() {
    	Object[] parameterList = null;
    	assertFalse(areDouble(parameterList));
    }
}
