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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class PercentileCounterTest {

	@Test
	public void test() {
		final PercentileCounter rp = new PercentileCounter(10, 100, 1000);

		// 40% under 10
		rp.increment(3);
		rp.increment(4);

		// 60% under 100
		rp.increment(23);
		// 80% under 1000
		rp.increment(124);

		// 20% exceeded max limit
		rp.increment(1433);

		final List<Percentile> list = rp.toList();

		assertEquals(5, rp.getTotal());
		assertEquals(3, list.size());

		a(list.get(0), 40.0, 2, 10, 5);
		a(list.get(1), 60.0, 3, 100, 5);
		a(list.get(2), 80.0, 4, 1000, 5);

		assertEquals(10, rp.estimatePercentile(20.0));
		assertEquals(10, rp.estimatePercentile(40.0));
		assertEquals(100, rp.estimatePercentile(50.0));
		assertEquals(1000, rp.estimatePercentile(70.0));
		assertEquals(Integer.MAX_VALUE, rp.estimatePercentile(80.1));
	}

	@Test
	public void testIndex() {
		final PercentileCounter rp = new PercentileCounter(10, 100, 1000);

		assertEquals(0, rp.index(0));
		assertEquals(0, rp.index(10));
		assertEquals(1, rp.index(11));
		assertEquals(1, rp.index(100));
		assertEquals(2, rp.index(500));
		assertEquals(2, rp.index(1000));
		assertEquals(3, rp.index(1001));
		assertEquals(3, rp.index(1500));
	}

	private void a(final Percentile p, final double percentage, final int sum, final int limit, final int total) {
		assertEquals("expected " + percentage, percentage, p.getPercentage(), 0.01);
		assertEquals(p.getSum(), sum);
		assertEquals(p.getLimit(), limit);
		assertEquals(p.getTotal(), total);
	}
}
