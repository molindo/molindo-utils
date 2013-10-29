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
package at.molindo.utils.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.molindo.utils.data.HexUtils;
import at.molindo.utils.io.CharsetUtils;

/**
 * {@link IDigestAlgorithm} implementation that wraps around
 * {@link MessageDigest}
 */
public enum DigestAlgorithm implements IDigestAlgorithm {
	MD2, MD5, SHA, SHA_256, SHA_384, SHA_512;

	private final String _name = name().replace('_', '-');

	public String getName() {
		return _name;
	}

	@Override
	public IDigest newDigest() {
		return new IDigest() {
			private final MessageDigest _md = newMessageDigest();

			@Override
			public IDigest add(final byte[] bytes) {
				_md.update(bytes);
				return this;
			}

			@Override
			public IDigest add(final ByteBuffer bytes) {
				_md.update(bytes);
				return this;
			}

			@Override
			public IDigest add(final String string) {
				return add(string, CharsetUtils.UTF_8);
			}

			@Override
			public IDigest add(final String string, final Charset charset) {
				_md.update(string.getBytes(charset));
				return this;
			}

			@Override
			public byte[] digest() {
				return _md.digest();
			}

			@Override
			public String digestHex() {
				return HexUtils.string(digest());
			}

			@Override
			public IDigest reset() {
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