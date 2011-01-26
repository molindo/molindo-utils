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

package at.molindo.utils.collections;

import java.util.Iterator;

import at.molindo.utils.data.StringUtils;

public class IteratorWrappers {

	private IteratorWrappers() {
	}

	public static Iterable<String> trim(final Iterable<String> iterable) {
		return new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return trim(iterable.iterator());
			}
		};
	}

	public static Iterator<String> trim(Iterator<String> iter) {
		return new IteratorWrapper<String>(iter) {

			@Override
			public String next() {
				return StringUtils.trim(super.next());
			}

		};
	}
}
