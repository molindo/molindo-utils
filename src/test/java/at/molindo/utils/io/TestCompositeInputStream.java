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

package at.molindo.utils.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

public class TestCompositeInputStream extends TestCase
{
    public void testRead() throws Exception
    {
        InputStream first = new ByteArrayInputStream(new byte[]{ 0, 1, 2, 3 });
        InputStream second = new ByteArrayInputStream(new byte[]{ 4, 5, 6, 7 });
        InputStream composite = new CompositeInputStream(first, second);

        byte[] buffer = new byte[32];
        int bytesRead = composite.read(buffer, 0, 32);
        assertEquals(8, bytesRead);
        for (int i = 0; i < 8; i++)
            assertEquals(i, buffer[i]);
        
        StreamUtils.close(first, second, composite);
    }
}