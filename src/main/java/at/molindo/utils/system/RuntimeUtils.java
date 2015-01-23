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
package at.molindo.utils.system;

import java.lang.management.ManagementFactory;

public class RuntimeUtils {

	public static final int UNKNOWN = -1;

	private RuntimeUtils() {
	}

	/**
	 * @return the PID of the current process or {@link #UNKNOWN} if unavailable
	 */
	public static int getPid() {
		final String id = ManagementFactory.getRuntimeMXBean().getName();
		final String[] ids = id.split("@");
		try {
			return Integer.parseInt(ids[0]);
		} catch (final NumberFormatException e) {
			return UNKNOWN;
		}
	}
}
