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
package at.molindo.utils.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RandomUtils {

	private RandomUtils() {
	}

	private static final ThreadLocal<Random> RAND = new ThreadLocal<Random>() {
		@Override
		protected Random initialValue() {
			return new Random();
		}
	};

	public static Random random() {
		return RAND.get();
	}

	/**
	 * @return a random element from the give list
	 */
	public static <T> T get(final List<T> list) {
		if (list.size() == 0) {
			throw new ArrayIndexOutOfBoundsException("empty list");
		}
		return list.get(random().nextInt(list.size()));
	}

	/**
	 * @return the list of randomly removed elements
	 */
	public static <T> List<T> remove(final List<T> list, final int amount) {
		if (list.size() == 0 || list.size() < amount) {
			throw new ArrayIndexOutOfBoundsException("list size too small");
		}

		final ArrayList<T> l = new ArrayList<T>(amount);
		for (int i = 0; i < amount; i++) {
			l.add(remove(list));
		}

		return l;
	}

	/**
	 * @return a list of randomly selected elements
	 */
	public static <T> List<T> get(final List<T> list, final int amount, final boolean uniqueResults) {
		if (list.size() == 0 || list.size() < amount) {
			throw new ArrayIndexOutOfBoundsException("list size too small");
		}

		if (!uniqueResults) {
			final ArrayList<T> res = new ArrayList<T>(amount);
			for (int i = 0; i < amount; i++) {
				res.add(get(list));
			}
			return res;
		}

		final List<T> uniqueList = new ArrayList<T>(new HashSet<T>(list));

		if (uniqueList.size() < amount) {
			throw new ArrayIndexOutOfBoundsException("list does not contain enough unique objects");
		}

		final HashSet<T> set = new HashSet<T>(amount);
		while (set.size() < amount) {
			set.add(get(uniqueList));
		}

		return new ArrayList<T>(set);
	}

	/**
	 * @return the randomly removed element from list
	 */
	public static <T> T remove(final List<T> list) {
		if (list.size() == 0) {
			throw new ArrayIndexOutOfBoundsException("empty list");
		}
		return list.remove(random().nextInt(list.size()));
	}

}
