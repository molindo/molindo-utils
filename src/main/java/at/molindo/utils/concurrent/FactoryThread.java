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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class FactoryThread extends Thread {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FactoryThread.class);

	private final IRunnableFactory _factory;

	private volatile boolean _active = true;

	private int _errors;
	private int _maxErrors = Integer.MAX_VALUE;

	public FactoryThread(IRunnableFactory factory) {
		if (factory == null) {
			throw new NullPointerException("factory");
		}
		_factory = factory;
	}

	public FactoryThread(IRunnableFactory factory, String name) {
		super(name);
		if (factory == null) {
			throw new NullPointerException("factory");
		}
		_factory = factory;
	}

	public FactoryThread(IRunnableFactory factory, ThreadGroup group, String name) {
		super(group, name);
		if (factory == null) {
			throw new NullPointerException("factory");
		}
		_factory = factory;
	}

	@Override
	public void run() {
		while (_active) {
			try {
				_factory.newRunnable().run();
				_errors = 0;
			} catch (Throwable t) {
				_errors++;
				if (_errors >= _maxErrors) {
					handleException(t, _errors, true);
					setInactive();
				} else {
					handleException(t, _errors, false);
				}
			}
		}
	}

	protected void handleException(Throwable t, int consecutiveErrors, boolean terminate) {
		if (terminate) {
			log.error("unhandled exception from created runnable, terminating after " + consecutiveErrors + " consecutive errors",
					t);
		} else {
			log.warn("unhandled exception from created runnable, " + consecutiveErrors + " consecutive errors, continuing", t);
		}
	}

	public FactoryThread setInactive() {
		_active = false;
		return this;
	}

	public FactoryThread setMaxErrors(int maxErrors) {
		_maxErrors = maxErrors;
		return this;
	}

	public interface IRunnableFactory {
		@Nonnull
		Runnable newRunnable();
	}
	
	public static class FactoryThreadGroup extends ThreadGroup {

		private final List<FactoryThread> _threads = new ArrayList<FactoryThread>();
		
		public FactoryThreadGroup (String groupName, @Nonnegative int threads, @Nonnull IRunnableFactory factory) {
			super(groupName);

			for (int i = 0; i < threads; i++) {
				_threads.add(new FactoryThread(factory, this, groupName + "#" + i) {

					@Override
					protected void handleException(Throwable t, int consecutiveErrors, boolean terminate) {
						FactoryThreadGroup.this.handleException(t, consecutiveErrors, terminate);
					}
				});
			}
		}
		
		public FactoryThreadGroup setMaxErrors(int maxErrors) {
			for (FactoryThread t : _threads) {
				t.setMaxErrors(maxErrors);
			}
			return this;
		}
		
		public FactoryThreadGroup start() {
			for (FactoryThread t : _threads) {
				t.start();
			}
			return this;
		}
		
		public FactoryThreadGroup setInactive() {
			for (FactoryThread t : _threads) {
				t.setInactive();
			}
			return this;
		}
		
		public FactoryThreadGroup join() throws InterruptedException {
			for (FactoryThread t : _threads) {
				t.join();
			}
			return this;
		}
		
		protected void handleException(Throwable t, int consecutiveErrors, boolean terminate) {
			if (terminate) {
				log.error("unhandled exception from created runnable, terminating after " + consecutiveErrors + " consecutive errors",
						t);
			} else {
				log.warn("unhandled exception from created runnable, " + consecutiveErrors + " consecutive errors, continuing", t);
			}
		}
	}
}
