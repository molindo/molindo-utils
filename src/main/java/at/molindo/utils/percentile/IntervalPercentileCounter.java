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

package at.molindo.utils.percentile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class IntervalPercentileCounter implements IIntervalPercentileCounter {

	private static final long serialVersionUID = 1L;

	private String _title;
	private final PercentileCounter[] _counters;
	private int _total;
	private final int _start;
	private final int _millis;
	private volatile int _last;

	public IntervalPercentileCounter(final int hours, final int... limits) {
		this(TimeUnit.HOURS, hours, limits);
	}

	public IntervalPercentileCounter(final TimeUnit invervalUnit, final int hours, final int... limits) {
		this(invervalUnit, hours, TimeUnit.MILLISECONDS, limits);
	}

	public IntervalPercentileCounter(final TimeUnit invervalUnit, final int size, final TimeUnit limitsUnit,
			final int... limits) {
		if (size < 1) {
			throw new IllegalArgumentException("size must be >= 1, was " + size);
		}
		_counters = new PercentileCounter[size];
		for (int i = 0; i < _counters.length; i++) {
			_counters[i] = new PercentileCounter(limitsUnit, limits);
		}

		_millis = (int) invervalUnit.toMillis(1);
		_start = (int) (currentMillis() / _millis);
	}

	@Override
	public void increment(final long start) {
		getCurrent(true).increment(start);
		_total++;
	}

	@Override
	public void increment(final int millis) {
		getCurrent(true).increment(millis);
		_total++;
	}

	@Override
	public int getTotal() {
		return _total;
	}

	@Override
	public Iterator<Percentile> iterator() {
		return getCurrent(false).iterator();
	}

	@Override
	public List<Percentile> toList() {
		return getCurrent(false).toList();
	}

	@Override
	public List<IPercentileCounter> toCountersList() {
		final ArrayList<IPercentileCounter> counters = new ArrayList<IPercentileCounter>(_counters.length);
		final Iterator<IPercentileCounter> iter = countersIterator();
		while (iter.hasNext()) {
			counters.add(iter.next());
		}
		return counters;
	}

	public Iterator<IPercentileCounter> countersIterator() {
		return new Iterator<IPercentileCounter>() {

			int _i = 0;

			IPercentileCounter[] _counters;

			{
				_counters = new IPercentileCounter[IntervalPercentileCounter.this._counters.length];
				final int last = _last;
				for (int i = 0; i < _counters.length; i++) {
					final int index = (last + 1 + i) % _counters.length;
					final IPercentileCounter counter = IntervalPercentileCounter.this._counters[index];
					_counters[i] = new UnmodifiablePercentileCounterWrapper(counter);
				}
			}

			@Override
			public boolean hasNext() {
				return _i < _counters.length;
			}

			@Override
			public IPercentileCounter next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return _counters[_i++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("remove()");
			}
		};
	}

	@Override
	public int estimatePercentile(final double percentile) {
		return getCurrent(false).estimatePercentile(percentile);
	}

	@Override
	public void clear() {
		for (int i = 0; i < _counters.length; i++) {
			clear(i);
		}
	}

	/**
	 * don't clear but replace - avoid having to throw influence iterators
	 */
	private PercentileCounter clear(final int i) {
		return _counters[i] = new PercentileCounter(_counters[i].getLimits());
	}

	PercentileCounter getCurrent(final boolean advance) {
		if (!advance) {
			return _counters[_last];
		} else {
			int current = (int) (currentMillis() / _millis) - _start;
			current %= _counters.length;

			PercentileCounter counter = _counters[current];

			if (current != _last) {
				synchronized (this) {
					if (current != _last) {
						// clear all counters that might have been skipped due
						// to
						// lack of activity
						for (int i = (_last + 1) % _counters.length; i != current; i = (i + 1) % _counters.length) {
							clear(i);
						}
						_last = current;
						counter = clear(current);
					}
				}

			}

			return counter;
		}
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append(IntervalPercentileCounter.class.getSimpleName()).append(": current=[");
		buf.append(getCurrent(false).toString());
		buf.append("]");
		return buf.toString();
	}

	// mock this method for unit testing
	protected long currentMillis() {
		return System.currentTimeMillis();
	}

	@Override
	public int[] getLimits() {
		return getCurrent(false).getLimits();
	}

	@Override
	public String getTitle() {
		return _title;
	}

	public void setTitle(final String title) {
		_title = title;
	}

}
