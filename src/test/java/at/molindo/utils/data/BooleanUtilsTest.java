/**
 * Copyright 2016 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.utils.data;

import static at.molindo.utils.data.BooleanUtils.isFalse;
import static at.molindo.utils.data.BooleanUtils.isFalseOrNull;
import static at.molindo.utils.data.BooleanUtils.isTrue;
import static at.molindo.utils.data.BooleanUtils.isTrueOrNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BooleanUtilsTest {

	@Test
	public void testIsTrue() {
		assertTrue(isTrue(Boolean.TRUE));
		assertFalse(isTrue(Boolean.FALSE));
		assertFalse(isTrue(null));
		assertTrue(isTrue(new Boolean(true)));
		assertFalse(isTrue(new Boolean(false)));
	}

	@Test
	public void testIsFalse() {
		assertFalse(isFalse(Boolean.TRUE));
		assertTrue(isFalse(Boolean.FALSE));
		assertFalse(isFalse(null));
		assertFalse(isFalse(new Boolean(true)));
		assertTrue(isFalse(new Boolean(false)));
	}

	@Test
	public void testIsTrueOrNull() {
		assertTrue(isTrueOrNull(Boolean.TRUE));
		assertFalse(isTrueOrNull(Boolean.FALSE));
		assertTrue(isTrueOrNull(null));
		assertTrue(isTrueOrNull(new Boolean(true)));
		assertFalse(isTrueOrNull(new Boolean(false)));
	}

	@Test
	public void testIsFalseOrNull() {
		assertFalse(isFalseOrNull(Boolean.TRUE));
		assertTrue(isFalseOrNull(Boolean.FALSE));
		assertTrue(isFalseOrNull(null));
		assertFalse(isFalseOrNull(new Boolean(true)));
		assertTrue(isFalseOrNull(new Boolean(false)));
	}
}
