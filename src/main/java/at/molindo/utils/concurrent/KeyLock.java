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
package at.molindo.utils.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * concurrency utility that locks execution of callable by a key, i.e. not using
 * key's identity but equality.
 * 
 * 
 * @param <K>
 *            key type
 * @param <V>
 *            return type of {@link Callable}
 * 
 * @author stf@molindo.at
 */
public class KeyLock<K, V> {

	private final ConcurrentHashMap<K, Task> _map = new ConcurrentHashMap<K, Task>();
	private boolean _wait;

	public static <K, V> KeyLock<K, V> newKeyLock() {
		return new KeyLock<K, V>();
	}

	public static <K, V> KeyLock<K, V> newKeyLock(boolean wait) {
		return new KeyLock<K, V>(wait);
	}

	public KeyLock() {
		this(true);
	}

	/**
	 * @param wait
	 *            should {@link #withLock(Object, Callable)} calls wait for
	 *            concurrent calls to finish or should they fail fast
	 */
	public KeyLock(boolean wait) {
		_wait = wait;
	}

	/**
	 * execute <code>callable</code> while locking concurrent execution for
	 * <code>key</code>
	 * 
	 * @param key
	 * @param callable
	 * @return
	 * @throws KeyLockedException
	 *             only if key lock configured not to wait and key locked by
	 *             other thread
	 * @throws Exception
	 *             any exception thrown by <code>callable.call()</code>
	 * @throws NullPointerException
	 *             if <code>key</code> or <code>callable</code> are
	 *             <code>null</code>
	 */
	public V withLock(final K key, final Callable<? extends V> callable) throws Exception {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (callable == null) {
			throw new NullPointerException("callable");
		}

		Task t = new Task(callable);
		try {
			final Task prev = _map.putIfAbsent(key, t);
			if (prev != null) {
				if (_wait) {
					t = prev;
				} else {
					t = null;
					throw new KeyLockedException(key);
				}
			}
			return t.perform();
		} finally {
			// first thread removes mapping
			if (t != null) {
				_map.remove(key);
			} else {
				// only with KeyLockedException
			}
		}
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

	/**
	 * @return number of active keys
	 */
	public int activeCount() {
		return _map.size();
	}

	/**
	 * @return a newly created {@link List} of all active keys. Changes to this
	 *         list don't have any effect to this {@link KeyLock} (changes to
	 *         the values will have an undefined effect though).
	 */
	public List<K> activeKeys() {
		return new ArrayList<K>(_map.keySet());
	}

	private final class Task {

		private final Callable<? extends V> _callable;
		private Exception _ex;
		private boolean _done = false;
		private V _result;

		public Task(final Callable<? extends V> callable) {
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

	public static final class KeyLockedException extends Exception {
		private static final long serialVersionUID = 1L;
		private final Object _key;

		private KeyLockedException(Object key) {
			super("key locked by different thread");
			_key = key;
		}

		public Object getKey() {
			return _key;
		}

	}
}
