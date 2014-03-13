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

package at.molindo.utils.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import at.molindo.utils.io.CharsetUtils;
import at.molindo.utils.io.Compression;
import at.molindo.utils.io.FileUtils;

public class Hash {

	private static final int STREAM_BUFFER_SIZE = 4096;

	private final byte[] _bytes;
	private final IAlgorithm _algorithm;

	public static Hash md5(String content) {
		return hash(Algorithm.MD5, content);
	}

	public static Hash md5(byte[] content) {
		return hash(Algorithm.MD5, content);
	}

	public static Hash sha256(String content) {
		return hash(Algorithm.SHA_256, content);
	}

	public static Hash sha256(byte[] content) {
		return hash(Algorithm.SHA_256, content);
	}

	public static Hash sha512(String content) {
		return hash(Algorithm.SHA_512, content);
	}

	public static Hash sha512(byte[] content) {
		return hash(Algorithm.SHA_512, content);
	}

	public static Hash hash(Algorithm algorithm, String content) {
		return algorithm.builder().add(content).hash();
	}

	public static Hash hash(Algorithm algorithm, byte[] content) {
		return algorithm.builder().add(content).hash();
	}

	public Hash(String hex, IAlgorithm algorithm) {
		this(HexUtils.bytes(hex), algorithm);
	}

	public Hash(byte[] bytes, IAlgorithm algorithm) {
		if (bytes == null) {
			throw new NullPointerException("bytes");
		}
		if (algorithm == null) {
			throw new NullPointerException("algorithm");
		}
		_bytes = bytes;
		_algorithm = algorithm;
	}

	public String toHex() {
		return HexUtils.string(_bytes);
	}

	public IAlgorithm getAlgorithm() {
		return _algorithm;
	}

	public byte[] getBytes() {
		return _bytes;
	}

	@Override
	public String toString() {
		return toHex();
	}

	public boolean validate(String content) {
		return equals(getAlgorithm().builder().add(content).hash());
	}

	public boolean validate(String content, Charset charset) {
		return equals(getAlgorithm().builder().add(content, charset).hash());
	}

	public boolean validate(byte[] content) {
		return equals(getAlgorithm().builder().add(content).hash());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (_algorithm == null ? 0 : _algorithm.hashCode());
		result = prime * result + Arrays.hashCode(_bytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Hash other = (Hash) obj;
		if (_algorithm == null) {
			if (other._algorithm != null) {
				return false;
			}
		} else if (!_algorithm.equals(other._algorithm)) {
			return false;
		}
		if (!Arrays.equals(_bytes, other._bytes)) {
			return false;
		}
		return true;
	}

	public interface IHashBuilder {
		IHashBuilder add(String string);

		IHashBuilder add(String string, Charset charset);

		IHashBuilder add(byte[] bytes);

		IHashBuilder add(byte[] bytes, int offset, int len);

		IHashBuilder add(ByteBuffer bytes);

		IHashBuilder add(InputStream stream) throws IOException;

		IHashBuilder add(File file) throws IOException;

		IHashBuilder add(File file, Compression compression) throws IOException;

		Hash hash();

		IHashBuilder reset();
	}

	public interface IAlgorithm {
		IHashBuilder builder();
	}

	public enum Algorithm implements IAlgorithm {
		MD2, MD5, SHA, SHA_256, SHA_384, SHA_512;

		private final String _name = name().replace('_', '-');

		public String getName() {
			return _name;
		}

		@Override
		public IHashBuilder builder() {
			return new IHashBuilder() {
				private final MessageDigest _md = newMessageDigest();

				@Override
				public IHashBuilder add(final byte[] bytes) {
					_md.update(bytes);
					return this;
				}

				@Override
				public IHashBuilder add(final byte[] bytes, int offset, int len) {
					_md.update(bytes, offset, len);
					return this;
				}

				@Override
				public IHashBuilder add(final ByteBuffer bytes) {
					_md.update(bytes);
					return this;
				}

				@Override
				public IHashBuilder add(final String string) {
					return add(string, CharsetUtils.UTF_8);
				}

				@Override
				public IHashBuilder add(final String string, final Charset charset) {
					_md.update(string.getBytes(charset));
					return this;
				}

				@Override
				public IHashBuilder add(InputStream stream) throws IOException {
					byte[] buffer = new byte[STREAM_BUFFER_SIZE];
					int n;
					while ((n = stream.read(buffer)) > 0) {
						add(buffer, 0, n);
					}
					return this;
				}

				@Override
				public IHashBuilder add(File file) throws IOException {
					return add(file, Compression.NONE);
				}

				@Override
				public IHashBuilder add(File file, Compression compression) throws FileNotFoundException, IOException {
					return add(FileUtils.in(file, compression));
				}

				@Override
				public Hash hash() {
					return new Hash(_md.digest(), Algorithm.this);
				}

				@Override
				public IHashBuilder reset() {
					_md.reset();
					return this;
				}
			};
		}

		private MessageDigest newMessageDigest() {
			try {
				return MessageDigest.getInstance(getName());
			} catch (final NoSuchAlgorithmException e) {
				throw new RuntimeException("no such algorithm: " + getName(), e);
			}
		}
	}

}
