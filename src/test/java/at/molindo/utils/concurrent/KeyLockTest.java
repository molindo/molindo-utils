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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.junit.Test;

public class KeyLockTest {

	@Test
	public void testWithLock() throws Exception {
		final KeyLock<String, Integer> lock = new KeyLock<String, Integer>() {

			@Override
			protected Integer replace(Integer result, Exception ex) throws Exception {
				return ex == null ? result : null;
			}

		};
		final Integer[] values = new Integer[2];
		final Thread[] threads = new Thread[1];

		values[0] = lock.withLock("foo", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {

				assertEquals(1, lock.activeCount());
				assertEquals(Arrays.asList("foo"), lock.activeKeys());

				threads[0] = new Thread() {

					@Override
					public void run() {
						try {
							values[1] = lock.withLock("foo", new Callable<Integer>() {

								@Override
								public Integer call() throws Exception {
									throw new RuntimeException("must not be called");
								}

							});
						} catch (Exception e) {
							e.printStackTrace();
							fail(e.getMessage());
						}
					}

				};
				threads[0].start();

				// allow other thread to hit lock
				Thread.sleep(300);

				return 42;
			}
		});

		threads[0].join();

		assertArrayEquals(new Integer[] { 42, 42 }, values);
	}

}
