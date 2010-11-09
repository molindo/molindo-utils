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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.molindo.utils.concurrent.FactoryThread.FactoryThreadGroup;
import at.molindo.utils.concurrent.FactoryThread.IRunnableFactory;

public class FactoryThreadTest {

	@Test
	public void testRun() throws InterruptedException {
		
		final int[] count = {0};
		
		FactoryThread t = new FactoryThread(new IRunnableFactory() {

			@Override
			public Runnable newRunnable() {
				return new Runnable() {

					@Override
					public void run() {
						if (count[0] >= 10) {
							throw new RuntimeException();
						}
						count[0]++;
					}
				};
			}
		}) {
			protected void handleException(Throwable t, int consecutiveErrors, boolean terminate) {
				if (terminate) {
					assertEquals(2, consecutiveErrors);
				} else {
					assertEquals(1, consecutiveErrors);
				}
			}
		}.setMaxErrors(2);
		
		t.start();
		t.join();

		assertEquals(10, count[0]);
	}

	@Test
	public void testGroup() throws InterruptedException {
		final int[] count = {0};
		FactoryThreadGroup group = new FactoryThreadGroup(FactoryThreadTest.class.getSimpleName(), 4, new IRunnableFactory() {
			
			@Override
			public Runnable newRunnable() {
				return new Runnable() {

					@Override
					public void run() {
						synchronized (count) {
							if (count[0] >= 10) {
								throw new RuntimeException();
							}
							count[0]++;
						}
					}
				};
			}
		}) {
			
			protected void handleException(Throwable t, int consecutiveErrors, boolean terminate) {
				if (terminate) {
					assertEquals(2, consecutiveErrors);
				} else {
					assertEquals(1, consecutiveErrors);
				}
			}
			
		}.setMaxErrors(2).start().join();
		
		assertEquals(10, count[0]);
		assertEquals(group.activeCount(), 0);
	}
}
