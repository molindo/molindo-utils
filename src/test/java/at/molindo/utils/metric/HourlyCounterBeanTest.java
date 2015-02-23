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
package at.molindo.utils.metric;

import static org.junit.Assert.assertArrayEquals;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

public class HourlyCounterBeanTest {

	@Test
	public void marshalling() throws Exception {
		JAXBContext context = JAXBContext.newInstance(TestDoc.class);

		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		HourlyCounterBean b = new HourlyCounterBean();
		b.setArray(new int[] { 1, 2, 3 });

		StringWriter writer = new StringWriter();
		marshaller.marshal(new TestDoc(b), writer);
		writer.close();

		String xml = writer.toString();

		StringReader reader = new StringReader(xml);
		HourlyCounterBean r = ((TestDoc) unmarshaller.unmarshal(reader)).getCounter();
		reader.close();

		assertArrayEquals(b.getArray(), r.getArray());

	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.PROPERTY)
	public static class TestDoc {
		private HourlyCounterBean _counter;

		public TestDoc() {
		}

		public TestDoc(HourlyCounterBean counter) {
			setCounter(counter);
		}

		public HourlyCounterBean getCounter() {
			return _counter;
		}

		public void setCounter(HourlyCounterBean counter) {
			_counter = counter;
		}

	}

}
