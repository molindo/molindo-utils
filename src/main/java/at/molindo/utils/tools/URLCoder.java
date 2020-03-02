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
package at.molindo.utils.tools;

import java.io.UnsupportedEncodingException;

import at.molindo.thirdparty.org.springframework.web.util.UriUtils;
import at.molindo.utils.io.CharsetUtils;

/**
 * wrapper around {@link UriUtils}.encode*(..) methods
 * 
 * @author stf@molindo.at
 */
public enum URLCoder {

	SCHEME {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeScheme(string, encoding);
		}

	},
	USER_INFO {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeUserInfo(string, encoding);
		}

	},
	HOST {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeHost(string, encoding);
		}

	},
	PORT {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodePort(string, encoding);
		}

	},
	PATH {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodePath(string, encoding);
		}

	},
	PATH_SEGMENT {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodePathSegment(string, encoding);
		}
	},
	QUERY {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeQuery(string, encoding);
		}

	},
	QUERY_PARAM {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeQueryParam(string, encoding);
		}
	},
	FRAGMENT {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeFragment(string, encoding);
		}
	},
	URI {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeUri(string, encoding);
		}
	},
	HTTP_URL {

		@Override
		public String encode(String string, String encoding) throws UnsupportedEncodingException {
			return UriUtils.encodeHttpUrl(string, encoding);
		}
	};

	public abstract String encode(String string, String encoding) throws UnsupportedEncodingException;

	public final String encode(String string) {
		try {
			return encode(string, CharsetUtils.UTF_8_NAME);
		} catch (UnsupportedEncodingException e) {
			throw new Utf8Unsupported(e);
		}
	}

	public final String decode(String string) {
		try {
			return decode(string, CharsetUtils.UTF_8_NAME);
		} catch (UnsupportedEncodingException e) {
			throw new Utf8Unsupported(e);
		}
	}

	public final String decode(String string, String encoding) throws UnsupportedEncodingException {
		return UriUtils.decode(string, encoding);
	}

	public static final class Utf8Unsupported extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public Utf8Unsupported(UnsupportedEncodingException cause) {
			super("utf-8 unsupported?", cause);
		}

	}
}
