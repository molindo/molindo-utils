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

package at.molindo.utils.metric;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(HourlyCounterAdapter.class)
public class HourlyCounter implements ICounter {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_HOURS = 168;
	private static final int DEFAULT_GRANULARITY = 60;

	private final int[] _array;
	private final int _hours;
	private final int _granularity;

	private int _currentIndex;
	private int _max = 0;
	private int _min = 0;

	private String _title;

	public HourlyCounter() {
		this(DEFAULT_HOURS, DEFAULT_GRANULARITY);
	}

	public HourlyCounter(final int hours) {
		this(hours, DEFAULT_GRANULARITY);
	}

	public HourlyCounter(final int hours, final int granularity) {
		_hours = hours;
		_granularity = checkGranularity(granularity);

		_array = new int[_hours * 60 / _granularity];
		_currentIndex = calcIndex();
	}

	public HourlyCounter(final HourlyCounter hc) {
		synchronized (hc) {
			_array = hc._array;
			_currentIndex = hc._currentIndex;
			_hours = hc._hours;
			_max = hc._max;
			_min = hc._min;
			_granularity = hc._granularity;
		}
	}

	public HourlyCounter(final HourlyCounterBean hourlyCounterBean) {
		_array = hourlyCounterBean.getArray();
		_currentIndex = hourlyCounterBean.getCurrentIndex();
		_hours = hourlyCounterBean.getHours();
		_max = hourlyCounterBean.getMax();
		_min = hourlyCounterBean.getMin();
		_granularity = hourlyCounterBean.getGranularity();
	}

	@Override
	public int getCount() {
		checkTime();
		return _array[index(_currentIndex)];
	}

	/**
	 * e.g. -1 or 1 for previous hour
	 *
	 * @param offset
	 * @return
	 */
	public int getCount(int offset) {
		offset = Math.abs(offset);
		return _array[index(_currentIndex - offset)];
	}

	private int index(final int index) {
		if (index <= _currentIndex - _array.length || index > _currentIndex) {
			throw new IllegalArgumentException("must be > " + -(_array.length - 1) + " and <= " + _currentIndex
					+ " but was " + index);
		}

		return (_array.length + index) % _array.length;
	}

	public int getHours() {
		return _hours;
	}

	@Override
	public void increment() {
		increment(1);
	}

	@Override
	public void increment(final int count) {
		if (count < 1) {
			return;
		}
		checkTime();
		_array[index(_currentIndex)] += count;
	}

	private synchronized void checkTime() {
		if (_currentIndex != calcIndex()) {
			final int val = _array[index(_currentIndex)];
			if (val > _max) {
				_max = val;
			}
			if (_min == 0 || val < _min) {
				_min = val;
			}

			incrementIndex();
		}
	}

	private void incrementIndex() {

		final int newIndex = calcIndex();
		if (newIndex > _currentIndex) {
			do {
				_array[index(++_currentIndex)] = 0;
			} while (newIndex > _currentIndex);
		}
	}

	private int calcIndex() {
		return (int) (System.currentTimeMillis() / (_granularity * 60000L));
	}

	@Override
	public String getTitle() {
		return _title;
	}

	public void setTitle(final String title) {
		_title = title;
	}

	/**
	 * max val of previous hours (getCount() might return a greater value)
	 *
	 * @return
	 */
	public int getMax() {
		return _max;
	}

	/**
	 * min val of previous hours (getCount() might return a lower value)
	 *
	 * @return
	 */
	public int getMin() {
		return _min;
	}

	public int[] getData() {
		final int[] data = new int[_array.length];
		for (int i = 0; i < _array.length; i++) {
			data[i] = getCount(_array.length - (i + 1));
		}
		return data;
	}

	public int getGranularity() {
		return _granularity;
	}

	private int checkGranularity(final int granularity) {
		if (60 % granularity != 0) {
			throw new IllegalArgumentException(
					"60 mod granularity must be 0 (i.e. 60, 30, 20, 15, 12, 10, 6, 5, 4, 3, 2, 1)");
		}
		return granularity;
	}

	public HourlyCounterBean toHourlyCounterBean() {
		return new HourlyCounterBean(_array, _currentIndex, _granularity, _hours, _max, _min);
	}
}
