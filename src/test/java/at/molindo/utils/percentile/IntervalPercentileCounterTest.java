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

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class IntervalPercentileCounterTest {

	private static Date NOW = new Date();

	private static void advance(final int count, final TimeUnit unit) {
		NOW = new Date(NOW.getTime() + unit.toMillis(count));
	}

	@Test
	public void test() {
		final IntervalPercentileCounter c = new IntervalPercentileCounter(TimeUnit.HOURS, 12, TimeUnit.MILLISECONDS,
				10, 100, 1000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected long currentMillis() {
				return NOW.getTime();
			}
		};

		c.increment(50);
		c.increment(5);
		c.increment(300);
		c.increment(1600);

		advance(1, TimeUnit.HOURS); // 2nd hour

		// read only operation must not advance interval
		final List<Percentile> list0 = c.toList();
		assertEquals(3, list0.size());
		a(list0.get(0), 25.0, 1, 10, 4);
		a(list0.get(1), 50.0, 2, 100, 4);
		a(list0.get(2), 75.0, 3, 1000, 4);

		// write operation must advance interval
		c.increment(100);

		final List<Percentile> list1 = c.toList();
		assertEquals(3, list1.size());
		a(list1.get(0), 0.0, 0, 10, 1);
		a(list1.get(1), 100.0, 1, 100, 1);
		a(list1.get(2), 100.0, 1, 1000, 1);

		final List<IPercentileCounter> counters0 = c.toCountersList();
		assertEquals(12, counters0.size());
		assertEquals(list0, counters0.get(counters0.size() - 2).toList());
		assertEquals(list1, counters0.get(counters0.size() - 1).toList());

		advance(4, TimeUnit.HOURS); // 6th hour

		c.increment(5);
		c.increment(43);

		final List<Percentile> list2 = c.toList();
		assertEquals(3, list2.size());

		final List<IPercentileCounter> counters1 = c.toCountersList();
		assertEquals(12, counters1.size());

		assertEquals(list0, counters1.get(counters1.size() - 6).toList());
		assertEquals(list1, counters1.get(counters1.size() - 5).toList());
		assertEquals(list2, counters1.get(counters1.size() - 1).toList());

		advance(6, TimeUnit.HOURS); // 12th hour

		c.increment(20);

		advance(1, TimeUnit.HOURS); // 1st hour (wrapped)

		c.increment(32);
		c.increment(5);
		c.increment(300);
		c.increment(104);
		c.increment(2344);

		// System.out.println(IntervalPercentileChartImage.getImageSourceModel(c).getUrl());
	}

	private void a(final Percentile p, final double percentage, final int sum, final int limit, final int total) {
		assertEquals("expected " + percentage, percentage, p.getPercentage(), 0.01);
		assertEquals(p.getSum(), sum);
		assertEquals(p.getLimit(), limit);
		assertEquals(p.getTotal(), total);
	}
}
