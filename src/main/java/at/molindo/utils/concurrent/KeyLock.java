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

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class KeyLock<K, V> {

	private final ConcurrentHashMap<K, Task> _map = new ConcurrentHashMap<K, Task>();

	public V withLock(final K key, final Callable<V> callable) throws Exception {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (callable == null) {
			throw new NullPointerException("callable");
		}

		try {
			Task t = new Task(callable);
			final Task prev = _map.putIfAbsent(key, t);
			if (prev != null) {
				t = prev;
				onExisting(key);
			}
			return t.perform();
		} finally {
			// first thread removes mapping
			_map.remove(key);
			
		}
	}

	/**
	 * hook that's called if key is already known. does nothing by default
	 * 
	 * @throws Exception
	 *             throw an exception to prevent waiting
	 */
	protected void onExisting(final K key) throws Exception {
		// do nothing
	}

	/**
	 * how to replace already computed results. by default it's null, as users
	 * might not want to get concurrency issues on shared results. As a
	 * replacement, one could perform a DB lookup if the task is to
	 * create/update a DB entity or do a deep clone of the object.
	 * 
	 * @param result
	 * @param ex
	 * @return
	 */
	protected V replace(final V result, final Exception ex) throws Exception {
		return null;
	}

	private final class Task {

		private final Callable<V> _callable;
		private Exception _ex;
		private boolean _done = false;
		private V _result;

		public Task(final Callable<V> callable) {
			_callable = callable;
		}

		public synchronized V perform() throws Exception {
			if (!_done) {
				try {
					_result = _callable.call();
					return _result;
				} catch (final Exception e) {
					_ex = e;
					throw e;
				} finally {
					_done = true;
				}
			} else {
				return replace(_result, _ex);
			}
		}
	}
}
