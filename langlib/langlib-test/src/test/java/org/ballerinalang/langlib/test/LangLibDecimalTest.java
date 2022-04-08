/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.langlib.test;

import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BDecimal;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.BRunUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Test cases for the lang.decimal library.
 *
 * @since 1.0
 */
public class LangLibDecimalTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/decimallib_test.bal");
    }

    @AfterClass
    public void tearDown() {
        compileResult = null;
    }

    @Test(dataProvider = "dualDecimalProvider")
    public void testSum(Object[] args, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testSum", args);
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalProvider")
    public void testSingleArgMax(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testOneArgMax", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalArrayProvider")
    public void testMax(BDecimal x, BArray xs, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testMultiArgMax", new Object[] {x, xs});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalArrayProvider")
    public void testMaxAsMethodInvok(BDecimal x, BArray xs, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testMaxAsMethodInvok", new Object[] {x, xs});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalMinProvider")
    public void testSingleArgMin(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testOneArgMin", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalMinArrayProvider")
    public void testMin(BDecimal x, BArray xs, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testMultiArgMin", new Object[] {x, xs});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalMinArrayProvider")
    public void testMinAsMethodInvok(BDecimal x, BArray xs, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testMinAsMethodInvok", new Object[] {x, xs});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalAbsProvider")
    public void testAbs(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testAbs", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalAbsProvider")
    public void testAbsAsMethodInvok(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testAbsAsMethodInvok", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "dualDecimalProvider")
    public static Object[][] dualDecimalProvider() {
        return new Object[][]{
                {new Object[]{ValueCreator.createDecimalValue("0"), ValueCreator.createDecimalValue("0")}, "0.0"},
                {new Object[]{ValueCreator.createDecimalValue("0.0"), ValueCreator.createDecimalValue("0.0")}, "0.0"},
                {new Object[]{ValueCreator.createDecimalValue("0"), ValueCreator.createDecimalValue("1")}, "1"},
                {new Object[]{ValueCreator.createDecimalValue("-1"), ValueCreator.createDecimalValue("1")}, "0"},
                {new Object[]{ValueCreator.createDecimalValue("-0"), ValueCreator.createDecimalValue("1")}, "1"}
        };
    }

    @DataProvider(name = "decimalProvider")
    public static Object[][] singleDecimalProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("0"), "0.0"},
                {ValueCreator.createDecimalValue("0.0"), "0.0"},
                {ValueCreator.createDecimalValue("0"), "0.0"},
                {ValueCreator.createDecimalValue("-1"), "-1"},
                {ValueCreator.createDecimalValue("-0"), "0.0"},
                {ValueCreator.createDecimalValue("5"), "5"}
        };
    }

    @DataProvider(name = "decimalMinProvider")
    public static Object[][] singleDecimalMinProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("0"), "0.0"},
                {ValueCreator.createDecimalValue("0.0"), "0.0"},
                {ValueCreator.createDecimalValue("0"), "0.0"},
                {ValueCreator.createDecimalValue("-1"), "-1"},
                {ValueCreator.createDecimalValue("-0"), "0.0"},
                {ValueCreator.createDecimalValue("5"), "5"}
        };
    }

    @DataProvider(name = "decimalArrayProvider")
    public static Object[][] decimalArrayProvider() {
        return new Object[][]{
                {ValueCreator.createDecimalValue("0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("0.0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("0"), getBArray("1"), "1"},
                {ValueCreator.createDecimalValue("-1"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("-0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("5"), getBArray("0", "2", "-2"), "5"},
                {ValueCreator.createDecimalValue("-511111111111199999999999222222222.2222222"), getBArray("0"), "0"}
        };
    }

    @DataProvider(name = "decimalMinArrayProvider")
    public static Object[][] decimalArrayMinProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("0.0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("0"), getBArray("1"), "0.0"},
                {ValueCreator.createDecimalValue("-1"), getBArray("0"), "-1"},
                {ValueCreator.createDecimalValue("-0"), getBArray("0"), "0"},
                {ValueCreator.createDecimalValue("5"), getBArray("0", "2", "-2"), "-2"},
                {ValueCreator.createDecimalValue("-51111111111119999999999922222.2222222"), getBArray("0"),
                        "-51111111111119999999999922222.22222"}
        };
    }

    private static BArray getBArray(String ...xs) {
        BDecimal[] decimals = new BDecimal[xs.length];
        for (int i = 0; i < xs.length; i++) {
            decimals[i] =  ValueCreator.createDecimalValue(xs[i]);
        }
        BArray bValueArray =
                ValueCreator.createArrayValue(decimals, TypeCreator.createArrayType(PredefinedTypes.TYPE_DECIMAL));
        return bValueArray;
    }

    @DataProvider(name = "decimalAbsProvider")
    public static Object[][] decimalAbsProvider() {
        return new Object[][]{
                {ValueCreator.createDecimalValue("0"), "0.0"},
                {ValueCreator.createDecimalValue("0.0"), "0.0"},
                {ValueCreator.createDecimalValue("-0"), "0.0"},
                {ValueCreator.createDecimalValue("-1"), "1"},
                {ValueCreator.createDecimalValue("-0.0"), "0.0"},
                {ValueCreator.createDecimalValue("-100.1"), "100.1"},
                {ValueCreator.createDecimalValue("100.1"), "100.1"},
                {ValueCreator.createDecimalValue("5"), "5"},
                {ValueCreator.createDecimalValue("-504023030303030303030.3030303"), "504023030303030303030.3030303"}
        };
    }

    @Test(dataProvider = "decimalRoundValueProvider")
    public void testRound(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testRound", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalRoundValueProvider")
    public void testRoundAsMethodInvok(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testRoundAsMethodInvok", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "decimalRoundValueProvider")
    public static Object[][] decimalRoundValueProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("0"), "0"},
                {ValueCreator.createDecimalValue("0.0"), "0"},
                {ValueCreator.createDecimalValue("-0"), "0"},
                {ValueCreator.createDecimalValue("-1"), "-1"},
                {ValueCreator.createDecimalValue("-0.0"), "0"},
                {ValueCreator.createDecimalValue("-100.1"), "-100"},
                {ValueCreator.createDecimalValue("100.1"), "100"},
                {ValueCreator.createDecimalValue("5"), "5"},
                {ValueCreator.createDecimalValue("504023030303030303030.3030303"), "504023030303030303030"},
                {ValueCreator.createDecimalValue("-504023030303030303030.3030303"), "-504023030303030303030"}
        };
    }

    @Test(dataProvider = "decimalRoundToFractionDigitsValueProvider")
    public void testRoundToFraction(BDecimal arg, Integer fractionDigits, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testRoundToFractionDigits",
                new Object[] {arg, fractionDigits});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "decimalRoundToFractionDigitsValueProvider")
    public static Object[][] decimalRoundToFractionDigitsValueProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("-504023030303030303030.303567"), 3, "-504023030303030303030.304"},
                {ValueCreator.createDecimalValue("-50402.303567"), 8, "-50402.303567"},
                {ValueCreator.createDecimalValue("3.303567"), 0, "3"},
                {ValueCreator.createDecimalValue("3234.5"), 0, "3234"},
                {ValueCreator.createDecimalValue("3234.303567"), 0, "3234"},

                {ValueCreator.createDecimalValue("3234.503567"), 0, "3235"},
                {ValueCreator.createDecimalValue("3233.503567"), 0, "3234"},
                {ValueCreator.createDecimalValue("-3234.303567"), 0, "-3234"},
                {ValueCreator.createDecimalValue("-3234.5"), 0, "-3234"},
                {ValueCreator.createDecimalValue("3.303567"), -1, "0"},

                {ValueCreator.createDecimalValue("3234.303567"), -2, "3.2E+3"},
                {ValueCreator.createDecimalValue("3234.303567"), -10, "0"},
                {ValueCreator.createDecimalValue("3234.303567"), 7, "3234.303567"},
                {ValueCreator.createDecimalValue("3234.303567"), 10, "3234.303567"},
                {ValueCreator.createDecimalValue("5.5555"), 4, "5.5555"},

                {ValueCreator.createDecimalValue("5.5555"), 3, "5.556"},
                {ValueCreator.createDecimalValue("5.5555"), 2, "5.56"},
                {ValueCreator.createDecimalValue("5.56"), 1, "5.6"},
                {ValueCreator.createDecimalValue("5.55"), -1, "1E+1"},
                {ValueCreator.createDecimalValue("5.55"), -200, "0"},

                {ValueCreator.createDecimalValue("5.55"), Integer.MAX_VALUE, "5.55"},
                {ValueCreator.createDecimalValue("5.55"), Integer.MIN_VALUE, "0"},
                {ValueCreator.createDecimalValue("5.5E-2"), 3, "0.055"},
                {ValueCreator.createDecimalValue("5.52156E2"), 3, "552.156"},
                {ValueCreator.createDecimalValue("5.521561234452654235E2"), 15, "552.156123445265424"},
        };
    }

    @Test(dataProvider = "decimalRoundToZeroAsCastValueProvider")
    public void testRoundToZeroWithCast(BDecimal arg) {
        BRunUtil.invoke(compileResult, "testRoundToZeroWithCast", new Object[] {arg});
    }

    @DataProvider(name = "decimalRoundToZeroAsCastValueProvider")
    public static Object[][] decimalRoundToZeroAsCastValueProvider() {
        return new Object[][] {
                {ValueCreator.createDecimalValue("-50402.303567")},
                {ValueCreator.createDecimalValue("3.303567")},
                {ValueCreator.createDecimalValue("3234.5")},
                {ValueCreator.createDecimalValue("3234.303567")},
                {ValueCreator.createDecimalValue("-3234.303567")},
                {ValueCreator.createDecimalValue("-3234.5")},
                {ValueCreator.createDecimalValue("3.303567")},
                {ValueCreator.createDecimalValue("3234.303567")},
                {ValueCreator.createDecimalValue("5.5555")},
                {ValueCreator.createDecimalValue("5.56")},
                {ValueCreator.createDecimalValue("5.55")},
                {ValueCreator.createDecimalValue("504023030303031.5035")}
        };
    }
    @Test(dataProvider = "decimalFloorValueProvider")
    public void testFloor(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testFloor", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }


    @Test(dataProvider = "decimalFloorValueProvider")
    public void testFloorAsMethodInvok(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testFloorAsMethodInvok", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "decimalFloorValueProvider")
    public static Object[][] decimalFloorValueProvider() {

        return new Object[][]{
                {ValueCreator.createDecimalValue("0"), "0"},
                {ValueCreator.createDecimalValue("0.0"), "0"},
                {ValueCreator.createDecimalValue("-0"), "0"},
                {ValueCreator.createDecimalValue("-1"), "-1"},
                {ValueCreator.createDecimalValue("-0.0"), "0"},
                {ValueCreator.createDecimalValue("-100.1"), "-101"},
                {ValueCreator.createDecimalValue("100.1"), "100"},
                {ValueCreator.createDecimalValue("5"), "5"},
                {ValueCreator.createDecimalValue("504023030303030303030.3030303"), "504023030303030303030"},
                {ValueCreator.createDecimalValue("-504023030303030303030.3030303"), "-504023030303030303031"}
        };
    }

    @Test(dataProvider = "decimalCeilingValueProvider")
    public void testCeiling(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testCeiling", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @Test(dataProvider = "decimalCeilingValueProvider")
    public void testCeilingAsMethodInvok(BDecimal arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testCeilingAsMethodInvok", new Object[] {arg});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "decimalCeilingValueProvider")
    public static Object[][] decimalCeilingValueProvider() {
        return new Object[][]{
                {ValueCreator.createDecimalValue("0"), "0"},
                {ValueCreator.createDecimalValue("0.0"), "0"},
                {ValueCreator.createDecimalValue("-0"), "0"},
                {ValueCreator.createDecimalValue("-1"), "-1"},
                {ValueCreator.createDecimalValue("-0.0"), "0"},
                {ValueCreator.createDecimalValue("-100.1"), "-100"},
                {ValueCreator.createDecimalValue("100.1"), "101"},
                {ValueCreator.createDecimalValue("5"), "5"},
                {ValueCreator.createDecimalValue("504023030303030303030.3030303"), "504023030303030303031"},
                {ValueCreator.createDecimalValue("-504023030303030303030.3030303"), "-504023030303030303030"}
        };
    }

    @Test(dataProvider = "decimalFromStringValueProvider")
    public void testFromString(String arg, String expected) {
        Object returns = BRunUtil.invoke(compileResult, "testFromString",
                new Object[] {StringUtils.fromString(arg)});
        assertEquals(returns, ValueCreator.createDecimalValue(expected));
    }

    @DataProvider(name = "decimalFromStringValueProvider")
    public static Object[][] decimalFromStringValueProvider() {
        return new Object[][] {
                { "0",      "0"},
                { "0.0",    "0.0"},
                { "-0",     "0"},
                { "-1",     "-1"},
                { "-0.0",   "0.0"},
                { "-100.1", "-100.1"},
                { "100.1",  "100.1"},
                { "5",      "5"},
                { "504023030303030303030.3030303", "504023030303030303030.3030303"},
                { "-504023030303030303030.3030303", "-504023030303030303030.3030303"}
        };
    }

    @Test
    public void testFromStringWithStringArg() {
        BRunUtil.invoke(compileResult, "testFromStringWithStringArg");
    }

    @Test
    public void testLangLibCallOnFiniteType() {
        BRunUtil.invoke(compileResult, "testLangLibCallOnFiniteType");
    }

    @Test(dataProvider = "functionsWithDecimalEqualityChecks")
    public void testFunctionsWithDecimalEqualityChecks(String function) {
        BRunUtil.invoke(compileResult, function);
    }

    @DataProvider
    public  Object[] functionsWithDecimalEqualityChecks() {
        return new String[] {
                "testDecimalEquality",
                "testDecimalNotEquality",
                "testDecimalExactEquality",
                "testDecimalNotExactEquality"
        };
    }

    @Test
    public void testFromStringFunctionWithInvalidValues() {
        BRunUtil.invoke(compileResult, "testFromStringFunctionWithInvalidValues");
    }

    @Test
    public void testQuantize() {
        BRunUtil.invoke(compileResult, "testQuantize");
    }

    @Test
    public void testQuantizeFunctionWithInvalidOutput() {
        BRunUtil.invoke(compileResult, "testQuantizeFunctionWithInvalidOutput");
    }
}
