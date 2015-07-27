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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import at.molindo.thirdparty.org.apache.http.client.utils.URIUtils;
import at.molindo.utils.collections.CollectionUtils;
import at.molindo.utils.collections.MapBuilder;
import at.molindo.utils.data.StringUtils;

/**
 * helps building URLs following the format:
 *
 * ${protocol}://[${user}[:${password}]@]${host}[:${port}]${path}[?${query}][#${
 * fragment}]
 *
 * @author stf@molindo.at
 */
public class UrlBuilder implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public static final String FTP = "ftp";

	private static final Map<String, Integer> PORTS = MapBuilder.builder(new HashMap<String, Integer>()).put(HTTP, 80)
			.put(HTTPS, 443).put(FTP, 21).getUnmodifiable();

	private static final String[] EMPTY_STRINGS = new String[0];

	/*
	 * This expression derived/taken from the BNF for URI (RFC2396).
	 */
	// private static final String URL_PATTERN =
	// "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";

	private static Pattern PROTOCOL = Pattern.compile("^[^:/?#]+$");
	private static Pattern HOST = Pattern.compile("^[^:@/?#]*$"); // added :@
	private static Pattern PATH = Pattern.compile("^[^?#]*$");
	private static Pattern QUERY = Pattern.compile("[^#]*");

	private String _protocol;
	private String _user;
	private String _password;
	private String _host;
	private Integer _port;
	private String _path;
	private LinkedHashMap<String, List<String>> _params;
	private String _fragment;

	/*
	 * currently unused
	 */
	private Map<String, Integer> _defaultPorts;

	public static UrlBuilder parse(String url) throws MalformedURLException {
		return new UrlBuilder(new URL(url));
	}

	public static LinkedHashMap<String, List<String>> parseQuery(String query) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();

		if (StringUtils.empty(query)) {
			return params;
		}
		query = StringUtils.stripLeading(query.trim(), "?");

		for (String param : query.split("&")) {
			String[] pair = param.split("=", 2);
			String key = decode(URLCoder.QUERY, pair[0]);
			String value = decode(URLCoder.QUERY, pair.length > 2 ? null : pair[1]);
			List<String> values = params.get(key);
			if (values == null) {
				values = new ArrayList<String>();
				params.put(key, values);
			}
			values.add(value);
		}
		return params;
	}

	public static String encode(URLCoder encoder, String s) {
		if (StringUtils.empty(s)) {
			return null;
		}
		return encoder.encode(s);
	}

	public static List<String> encodeAll(URLCoder encoder, List<String> list) {
		if (list == null) {
			return null;
		}

		List<String> encoded = new ArrayList<String>(list.size());
		for (String s : list) {
			encoded.add(encode(encoder, s));
		}
		return encoded;
	}

	public static String decode(URLCoder decoder, String s) {
		if (StringUtils.empty(s)) {
			return null;
		}

		return decoder.decode(s);
	}

	public static List<String> decodeAll(URLCoder decoder, List<String> list) {
		if (list == null) {
			return null;
		}

		List<String> decoded = new ArrayList<String>(list.size());
		for (String s : list) {
			decoded.add(decode(decoder, s));
		}
		return decoded;
	}

	/**
	 * http://example.com/
	 */
	public UrlBuilder() {
		this("example.com");
	}

	/**
	 * http://{host}/
	 */
	public UrlBuilder(String host) {
		this(HTTP, host);
	}

	/**
	 * {protocol}://{host}/
	 */
	public UrlBuilder(String protocol, String host) {
		this(protocol, host, "/");
	}

	/**
	 * {protocol}://{host}{path}
	 */
	public UrlBuilder(String protocol, String host, String path) {
		setProtocol(protocol).setHost(host).setPath(path);
	}

	public UrlBuilder(URL url) {
		setUrl(url);
	}

	protected UrlBuilder setUrl(URL url) {
		setProtocol(url.getProtocol());
		String userInfo = url.getUserInfo();
		String[] credentials = StringUtils.empty(userInfo) ? EMPTY_STRINGS : userInfo.split(":", 2);
		setUser(credentials.length > 0 ? credentials[0] : null);
		setPassword(credentials.length > 1 ? credentials[1] : null);
		setHost(url.getHost());
		int port = url.getPort();
		setPort(port == -1 ? null : port);
		String path = url.getPath();
		setPath(StringUtils.empty(path) ? "/" : path);
		setQuery(url.getQuery());
		setFragment(url.getRef());
		return this;
	}

	public UrlBuilder setProtocol(String protocol) {
		if (StringUtils.empty(protocol)) {
			throw new IllegalArgumentException("protocol must not be empty");
		}
		protocol = protocol.trim();

		if (!PROTOCOL.matcher(protocol).matches()) {
			throw new IllegalArgumentException("protocol not allowed: " + protocol);
		}

		_protocol = protocol;
		return this;
	}

	public UrlBuilder setUser(String user) {
		if (!StringUtils.empty(user)) {
			if (!HOST.matcher(user).matches()) {
				throw new IllegalArgumentException("user not allowed: " + user);
			}
		}

		_user = encode(URLCoder.USER_INFO, user);
		return this;
	}

	public UrlBuilder setPassword(String password) {
		if (!StringUtils.empty(password)) {
			if (!HOST.matcher(password).matches()) {
				throw new IllegalArgumentException("password not allowed: " + password);
			}
		}

		_password = encode(URLCoder.USER_INFO, password);
		return this;
	}

	public UrlBuilder setHost(String host) {
		if (!StringUtils.empty(host)) {
			if (!HOST.matcher(host).matches()) {
				throw new IllegalArgumentException("host not allowed: " + host);
			}
		}

		_host = host;
		return this;
	}

	public UrlBuilder setPort(Integer port) {
		if (port != null && (port < 0 || port > 65535)) {
			throw new IllegalArgumentException("port out of range: " + port);
		}
		_port = port;
		return this;
	}

	public UrlBuilder setPath(String path) {
		if (StringUtils.empty(path)) {
			throw new IllegalArgumentException("path must not be empty");
		}
		path = path.trim();

		if (!PATH.matcher(path).matches()) {
			throw new IllegalArgumentException("path not allowed: " + path);
		}

		_path = path;
		return this;
	}

	public UrlBuilder setQuery(String query) {
		if (!StringUtils.empty(query)) {
			if (!QUERY.matcher(query).matches()) {
				throw new IllegalArgumentException("query not allowed: " + query);
			}
			setParams(parseQuery(query));
		} else {
			clearParams();
		}

		return this;
	}

	public UrlBuilder setParams(LinkedHashMap<String, List<String>> params) {
		clearParams();
		addParams(params);
		return this;
	}

	public UrlBuilder addParams(LinkedHashMap<String, List<String>> params) {
		for (Map.Entry<String, List<String>> e : params.entrySet()) {
			addParams(e.getKey(), e.getValue());
		}
		return this;
	}

	public UrlBuilder setParam(String key, String value) {
		setParams(key, value);
		return this;
	}

	public UrlBuilder setParams(String key, String... values) {
		setParams(key, Arrays.asList(values));
		return this;
	}

	public UrlBuilder setParams(String key, List<String> values) {
		key = normalizeKey(key);
		LinkedHashMap<String, List<String>> params = params();
		if (values == null || values.size() == 0) {
			params.remove(key);
		} else {
			params.put(key, encodeAll(URLCoder.QUERY_PARAM, values));
		}

		return this;
	}

	public UrlBuilder addParam(String key, String value) {
		addParams(key, value);
		return this;
	}

	public UrlBuilder addParams(String key, String... values) {
		addParams(key, Arrays.asList(values));
		return this;
	}

	public UrlBuilder addParams(String key, List<String> values) {
		key = normalizeKey(key);
		LinkedHashMap<String, List<String>> params = params();
		List<String> current = params.get(key);
		if (current == null) {
			params.put(key, encodeAll(URLCoder.QUERY_PARAM, values));
		} else {
			current.addAll(encodeAll(URLCoder.QUERY_PARAM, values));
		}
		return this;
	}

	public UrlBuilder removeParams(String key) {
		if (_params != null) {
			_params.remove(normalizeKey(key));
		}
		return this;
	}

	public void clearParams() {
		if (_params != null) {
			_params.clear();
		}
	}

	private String normalizeKey(String key) {
		if (StringUtils.empty(key)) {
			throw new IllegalArgumentException("key must not be empty");
		}
		return encode(URLCoder.QUERY_PARAM, key);
	}

	private LinkedHashMap<String, List<String>> params() {
		if (_params == null) {
			// lazy init
			_params = new LinkedHashMap<String, List<String>>();
		}
		return _params;
	}

	public UrlBuilder setFragment(String fragment) {
		// encode?
		_fragment = fragment;
		return this;
	}

	public UrlBuilder resolve(String relativePath) {
		try {
			return setUrl(URIUtils.resolve(toURL().toURI(), relativePath).toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("failed to resolve URL", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("failed to resolve URL", e);
		}
	}

	public String toUrlString() {
		return toUrlString(false);
	}

	public String toUrlString(boolean sortParams) {
		StringBuilder buf = new StringBuilder();
		buf.append(_protocol).append("://");

		if (!StringUtils.empty(_host)) {
			// according to RFC3986 3.2, host is required for userinfo and port

			if (!StringUtils.empty(_user)) {
				buf.append(_user);
				if (!StringUtils.empty(_password)) {
					buf.append(":").append(_password);
				}
				buf.append("@");
			}

			buf.append(_host);

			if (_port != null) {
				Integer defaultPort = (_defaultPorts != null ? _defaultPorts : PORTS).get(_protocol);
				if (defaultPort == null || !defaultPort.equals(_port)) {
					buf.append(":").append(_port);
				}
			}

			if (!StringUtils.empty(_path) && !_path.startsWith("/")) {
				// if host is present, path must start with /
				buf.append("/");
			}
		}

		buf.append(_path);

		if (!CollectionUtils.empty(_params)) {
			buf.append("?");

			Map<String, List<String>> params = _params;
			if (sortParams) {
				params = new TreeMap<String, List<String>>(params);
			}

			for (Map.Entry<String, List<String>> e : params.entrySet()) {
				for (String value : e.getValue()) {
					buf.append(e.getKey()).append("=");
					if (!StringUtils.empty(value)) {
						buf.append(value);
					}
					buf.append("&");
				}
			}

			buf.setLength(buf.length() - 1); // strip last &
		}

		if (!StringUtils.empty(_fragment)) {
			buf.append("#").append(_fragment);

		}
		return buf.toString();
	}

	public URL toURL() {
		String url = toUrlString();
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException("builder created malformed URL: " + url);
		}
	}

	@Override
	public String toString() {
		return toUrlString();
	}

	@Override
	public UrlBuilder clone() {
		try {
			UrlBuilder clone = (UrlBuilder) super.clone();
			if (clone._params != null) {
				clone._params = new LinkedHashMap<String, List<String>>(clone._params);
			}
			if (clone._defaultPorts != null) {
				clone._defaultPorts = new LinkedHashMap<String, Integer>(clone._defaultPorts);
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("failed to clone UrlBuilder", e);
		}

	}

	public String getProtocol() {
		return _protocol;
	}

	public String getUser() {
		return _user;
	}

	public String getPassword() {
		return _password;
	}

	public String getHost() {
		return _host;
	}

	public Integer getPort() {
		return _port;
	}

	public String getPath() {
		return _path;
	}

	public LinkedHashMap<String, List<String>> getParams() {
		return _params;
	}

}
