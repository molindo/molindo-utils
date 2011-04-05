/**
 * Copyright 2010 Molindo GmbH
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

package at.molindo.utils.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectPoolTest {

	@Test
	public void test() {
		ObjectPool<List<String>> pool = new ObjectPool<List<String>>(10) {

			@Override
			protected List<String> create() {
				return new ArrayList<String>();
			}

		};

		List<String> list = pool.get();
		assertNotNull("pool did return null object", list);
		assertNotSame("pool did return same list", list, pool.get());

		pool.put(list);
		assertSame("pool did not return same list after put(..)", list, pool.get());
	}
}
