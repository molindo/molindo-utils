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

public class ExceptionUtils {

	private ExceptionUtils() {
	}

	/**
	 * @param t
	 * @return a string containing all messages from causes of t concatenated
	 *         using ", caused by " as separator
	 */
	public static String getAllMessages(final Throwable t) {
		final StringBuilder error = new StringBuilder(t.getMessage());

		Throwable cause = t.getCause();
		while (cause != null && cause.getCause() != cause) {
			error.append(", caused by ").append(cause.getMessage());
			cause = cause.getCause();
		}
		return error.toString();
	}

	public static <T> T unexpected(Throwable t) {
		throw new RuntimeException("unexpected exception", t);
	}

	public static <T extends Throwable> void throwCause(Class<T> throwableType, Throwable t) throws T {
		T cause = findCause(throwableType, t);
		if (cause != null) {
			throw cause;
		}
	}

	public static <T extends Throwable> T findCause(Class<T> throwableType, Throwable t) {
		while (t != null) {
			if (throwableType.isInstance(t)) {
				return throwableType.cast(t);
			} else {
				t = t.getCause();
			}
		}
		return null;
	}
}
