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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import at.molindo.utils.percentile.IIntervalPercentileCounter;

public class Statistics implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ICounter> _counters;
	private List<IIntervalPercentileCounter> _intervalPercentileCounters;

	public void setCounters(final List<ICounter> counters) {
		_counters = counters;
	}

	public List<ICounter> getCounters() {
		if (_counters == null) {
			_counters = new LinkedList<ICounter>();
		}
		return _counters;
	}

	public List<IIntervalPercentileCounter> getIntervalPercentileCounters() {
		if (_intervalPercentileCounters == null) {
			_intervalPercentileCounters = new LinkedList<IIntervalPercentileCounter>();
		}
		return _intervalPercentileCounters;
	}

	public void setIntervalPercentileCounters(final List<IIntervalPercentileCounter> percentileCounters) {
		_intervalPercentileCounters = percentileCounters;
	}

}
