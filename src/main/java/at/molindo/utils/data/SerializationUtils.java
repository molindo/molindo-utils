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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import at.molindo.utils.io.StreamUtils;

public class SerializationUtils {
	public static int DEFAULT_BUFFER = 4096;

	private SerializationUtils() {
	}

	public static byte[] serialize(Object obj) throws NotSerializableException {
		return serialize(obj, DEFAULT_BUFFER);
	}

	public static byte[] serialize(Object obj, int buffer) throws NotSerializableException {
		ByteArrayOutputStream out = null;
		ObjectOutputStream oOut = null;
		try {
			out = new ByteArrayOutputStream(buffer);
			oOut = new ObjectOutputStream(out);

			oOut.writeObject(obj);

			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("failed to write to ByteArrayOutputStream", e);
		} finally {
			StreamUtils.close(out, oOut);
		}
	}

	public static Object deserialize(byte[] bytes) throws ClassNotFoundException {
		ByteArrayInputStream in = null;
		ObjectInputStream oIn = null;
		try {
			in = new ByteArrayInputStream(bytes);
			oIn = new ObjectInputStream(in);

			return oIn.readObject();
		} catch (IOException e) {
			throw new RuntimeException("failed to write to ByteArrayOutputStream", e);
		} finally {
			StreamUtils.close(in, oIn);
		}
	}

	public static <T> T deserialize(byte[] bytes, Class<T> cls) throws ClassNotFoundException {
		return cls.cast(deserialize(bytes));
	}

	public static Object copy(Object obj) throws NotSerializableException {
		return copy(obj, Object.class, DEFAULT_BUFFER);
	}

	public static <T> T copy(T obj, Class<T> cls) throws NotSerializableException {
		return copy(obj, cls, DEFAULT_BUFFER);
	}

	public static <T> T copy(T obj, Class<T> cls, int buffer) throws NotSerializableException {
		try {
			return deserialize(serialize(obj, buffer), cls);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("why can't we deserialize a class we just serialized?", e);
		}
	}
}
