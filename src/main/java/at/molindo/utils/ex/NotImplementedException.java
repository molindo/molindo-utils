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
package at.molindo.utils.ex;

public class NotImplementedException extends UnsupportedOperationException {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_MESSAGE = "Code is not implemented";

	public NotImplementedException() {
		super(DEFAULT_MESSAGE);
	}

	public NotImplementedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotImplementedException(final String message) {
		super(message);
	}

	public NotImplementedException(final Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	public NotImplementedException(final Class<?> clazz) {
		super(clazz == null ? DEFAULT_MESSAGE : DEFAULT_MESSAGE + " in " + clazz);
	}
}
