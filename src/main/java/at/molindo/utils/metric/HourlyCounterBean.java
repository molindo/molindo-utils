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
package at.molindo.utils.metric;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import at.molindo.utils.collections.IteratorUtils;
import at.molindo.utils.data.Function;
import at.molindo.utils.data.PrimitiveUtils;
import at.molindo.utils.data.StringUtils;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class HourlyCounterBean {
	private static final String DELIM = " ";
	private int[] _array;
	private int _currentIndex;
	private int _granularity;
	private int _hours;
	private int _max;
	private int _min;

	private static int[] splitInts(String valueString, String delim) {
		Iterable<Integer> values = IteratorUtils.transform(StringUtils.split(valueString, delim),
				new Function<String, Integer>() {

					@Override
					public Integer apply(String input) throws NumberFormatException {
						return StringUtils.empty(input) ? 0 : Integer.valueOf(input);
					}

				});

		return PrimitiveUtils.primitive(IteratorUtils.list(values), 0);
	}

	public HourlyCounterBean() {
	}

	public HourlyCounterBean(final int[] array, final int currentIndex, final int granularity, final int hours,
			final int max, final int min) {
		setArray(array);
		setCurrentIndex(currentIndex);
		setGranularity(granularity);
		setHours(hours);
		setMax(max);
		setMin(min);
	}

	public HourlyCounter toHourlyCounter() {
		return new HourlyCounter(this);
	}

	@XmlTransient
	public int[] getArray() {
		return _array;
	}

	public String getValues() {
		return _array == null ? null : StringUtils.join(DELIM, Arrays.asList(PrimitiveUtils.object(_array)));
	}

	public int getCurrentIndex() {
		return _currentIndex;
	}

	public int getHours() {
		return _hours;
	}

	public int getMax() {
		return _max;
	}

	public int getMin() {
		return _min;
	}

	public int getGranularity() {
		return _granularity;
	}

	public void setArray(final int[] array) {
		_array = array;
	}

	public void setValues(final String values) {
		setArray(values == null ? null : splitInts(values, DELIM));
	}

	public void setCurrentIndex(final int currentIndex) {
		_currentIndex = currentIndex;
	}

	public void setHours(final int hours) {
		_hours = hours;
	}

	public void setMax(final int max) {
		_max = max;
	}

	public void setMin(final int min) {
		_min = min;
	}

	public void setGranularity(final int granularity) {
		_granularity = granularity;
	}

}
