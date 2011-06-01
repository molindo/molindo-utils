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

package at.molindo.utils.tools;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.junit.Test;

public class UrlBuilderTest {

	@Test
	public void simple() {
		assertEquals("http://example.com/", new UrlBuilder().toString());
		assertEquals("https://example.org/", new UrlBuilder("example.org").setProtocol(UrlBuilder.HTTPS).setPort(443)
				.toString());
	}

	@Test
	public void url() throws MalformedURLException {
		String[] urls = { "http://www.google.com/#q=url", "ftp://foo:bar@example.org/files",
				"https://example.org/?foo=bar&baz=" };
		for (String url : urls) {
			assertEquals(url, UrlBuilder.parse(url).toString());
		}
	}

	@Test
	public void params() {
		UrlBuilder builder = new UrlBuilder();
		builder.setQuery("?foo=bar&baz=");
		assertEquals("http://example.com/?foo=bar&baz=", builder.toString());
		builder.setParam("foo", "qux");
		assertEquals("http://example.com/?foo=qux&baz=", builder.toString());
		builder.addParam("foo", "bar");
		assertEquals("http://example.com/?foo=qux&foo=bar&baz=", builder.toString());
		builder.removeParams("foo");
		assertEquals("http://example.com/?baz=", builder.toString());
		builder.clearParams();
		assertEquals("http://example.com/", builder.toString());
	}

	@Test
	public void encode() {
		UrlBuilder builder = new UrlBuilder();
		builder.addParams("key", "foo bar", "foo+bar");
		assertEquals("http://example.com/?key=foo%20bar&key=foo%2Bbar", builder.toString());
	}
}
