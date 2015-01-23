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

import java.io.Serializable;

public class Percentile implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int _sum;
	private final int _total;
	private final int _limit;

	public Percentile(final int sum, final int total, final int limit) {
		_sum = sum;
		_total = total;
		_limit = limit;
	}

	public int getSum() {
		return _sum;
	}

	public int getTotal() {
		return _total;
	}

	public int getLimit() {
		return _limit;
	}

	public double getPercentage() {
		return _total == 0 ? 0 : 100.0 / _total * _sum;
	}

	@Override
	public String toString() {
		return String.format("%d (%.2f%%) <= %d ms", _sum, getPercentage(), _limit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _limit;
		result = prime * result + _sum;
		result = prime * result + _total;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Percentile)) {
			return false;
		}
		final Percentile other = (Percentile) obj;
		if (_limit != other._limit) {
			return false;
		}
		if (_sum != other._sum) {
			return false;
		}
		if (_total != other._total) {
			return false;
		}
		return true;
	}

}
