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
package at.molindo.utils.metric.percentile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class PercentileCounter implements IPercentileCounter {
	private static final long serialVersionUID = 1L;

	private final int[] _limits;
	private final int[] _counts;
	private int _total;
	private String _title;

	public PercentileCounter(final int... limits) {
		this(TimeUnit.MILLISECONDS, limits);
	}

	public PercentileCounter(final TimeUnit unit, final int... limits) {
		if (limits.length == 0) {
			throw new IllegalArgumentException("at least 1 limit required");
		}

		_limits = Arrays.copyOf(limits, limits.length);
		Arrays.sort(_limits);

		if (unit != TimeUnit.MILLISECONDS) {
			for (int i = 0; i < _limits.length; i++) {
				_limits[i] = (int) unit.toMillis(_limits[i]);
			}
		}

		_counts = new int[_limits.length];

	}

	@Override
	public void increment(final long start) {
		final long value = System.currentTimeMillis() - start;
		if (value > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("cannot increment percentile with values > Integer.MAX_VALUE, was "
					+ value + " (start=" + start + ")");
		}
		increment((int) value);
	}

	@Override
	public void increment(final int millis) {
		final int i = index(millis);
		if (i < _limits.length) {
			_counts[i]++;
		}
		_total++;
	}

	int index(final int millis) {
		// binary search

		int low = 0;
		int high = _limits.length - 1;
		int mid = -1;

		while (low <= high) {
			mid = (low + high) / 2;

			final int res = _limits[mid] - millis;
			if (res < 0) {
				low = mid + 1;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				return mid;
			}
		}

		return _limits[mid] < millis ? mid + 1 : mid;
	}

	@Override
	public int getTotal() {
		return _total;
	}

	@Override
	public int estimatePercentile(final double percentile) {
		if (percentile < 0.0 || percentile > 100.0) {
			throw new IllegalArgumentException("percentile must be between 0.0 and 100.0, was " + percentile);
		}

		for (final Percentile p : this) {
			if (percentile - p.getPercentage() <= 0.0001) {
				return p.getLimit();
			}
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public void clear() {
		for (int i = 0; i < _counts.length; i++) {
			_counts[i] = 0;
		}
		_total = 0;
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append(PercentileCounter.class.getSimpleName()).append(": ");

		Percentile last = null;
		for (final Percentile p : this) {
			if (last == null || p.getSum() != last.getSum()) {
				buf.append(p).append(", ");
			}
			last = p;
		}

		final double remaining = last == null ? 0.0 : 100.0 - last.getPercentage();

		if (Math.abs(remaining) > 0.001) {
			final int count = _total - last.getSum();
			final int limit = _limits[_limits.length - 1];
			buf.append(String.format("%d (%.2f%%) > %d ms", count, remaining, limit));
			buf.append(", ");
		}

		buf.append(_total).append(" total");

		return buf.toString();
	}

	@Override
	public Iterator<Percentile> iterator() {
		return new Iterator<Percentile>() {

			// get a copy of current state
			private final int _total = PercentileCounter.this._total;
			private final int[] _counts = Arrays.copyOf(PercentileCounter.this._counts,
					PercentileCounter.this._counts.length);

			private int _i = 0;
			private int _sum = 0;

			@Override
			public boolean hasNext() {
				return _i < _counts.length;
			}

			@Override
			public Percentile next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}

				_sum += _counts[_i];
				final Percentile p = new Percentile(_sum, _total, _limits[_i]);
				_i++;
				return p;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public List<Percentile> toList() {
		final ArrayList<Percentile> list = new ArrayList<Percentile>(_limits.length);
		for (final Percentile p : this) {
			list.add(p);
		}
		return list;
	}

	public int[] getLimits() {
		return Arrays.copyOf(_limits, _limits.length);
	}

	@Override
	public String getTitle() {
		return _title;
	}

	public void setTitle(final String title) {
		_title = title;
	}
}
