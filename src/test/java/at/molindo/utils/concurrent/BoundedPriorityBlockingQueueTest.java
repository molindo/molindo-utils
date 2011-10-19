package at.molindo.utils.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class BoundedPriorityBlockingQueueTest {

	@Test(timeout = 1000)
	public void foo() throws InterruptedException {

		final BoundedPriorityBlockingQueue<Integer> q = new BoundedPriorityBlockingQueue<Integer>(1);

		assertTrue(q.offer(1));
		assertFalse(q.offer(2));

		assertFalse(q.offer(3, 1, TimeUnit.MILLISECONDS));

		assertEquals((Integer) 1, q.peek());
		assertEquals((Integer) 1, q.poll());
		assertNull(q.peek());

		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 10; i++) {
						Thread.sleep(10);
						assertNotNull(q.take());
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		t.start();

		for (int i = 0; i < 10; i++) {
			q.put(i);
		}
		t.join();

		assertEquals(0, q.size());
	}

	@Test
	public void priority() {
		final BoundedPriorityBlockingQueue<Integer> q = new BoundedPriorityBlockingQueue<Integer>(10);
		q.addAll(Arrays.asList(1, 4, 3, 5, 2));

		// assert 1, 2, 3, 4, 5
		for (Integer i = 1; i <= 5; i++) {
			assertEquals(i, q.poll());
		}
	}
}
