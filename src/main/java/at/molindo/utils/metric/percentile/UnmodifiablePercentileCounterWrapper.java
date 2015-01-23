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

package at.molindo.utils.metric.percentile;

import java.util.Iterator;
import java.util.List;

public class UnmodifiablePercentileCounterWrapper implements IPercentileCounter {

	private static final long serialVersionUID = 1L;

	private final IPercentileCounter _wrapped;

	public UnmodifiablePercentileCounterWrapper(final IPercentileCounter wrapped) {
		if (wrapped == null) {
			throw new NullPointerException("wrapped");
		}
		_wrapped = wrapped;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("clear() not allowed (read-only)");
	}

	@Override
	public int estimatePercentile(final double percentile) {
		return _wrapped.estimatePercentile(percentile);
	}

	@Override
	public int getTotal() {
		return _wrapped.getTotal();
	}

	@Override
	public void increment(final long start) {
		throw new UnsupportedOperationException("increment(long) not allowed (read-only)");
	}

	@Override
	public void increment(final int millis) {
		throw new UnsupportedOperationException("increment(int) not allowed (read-only)");
	}

	@Override
	public Iterator<Percentile> iterator() {
		return _wrapped.iterator();
	}

	@Override
	public List<Percentile> toList() {
		return _wrapped.toList();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UnmodifiablePercentileCounterWrapper) {
			obj = ((UnmodifiablePercentileCounterWrapper) obj)._wrapped;
		}
		return _wrapped.equals(obj);
	}

	@Override
	public int hashCode() {
		return _wrapped.hashCode();
	}

	@Override
	public String toString() {
		return _wrapped.toString() + " (read-only)";
	}

	protected IPercentileCounter getWrapped() {
		return _wrapped;
	}

	@Override
	public String getTitle() {
		return _wrapped.getTitle();
	}

}
