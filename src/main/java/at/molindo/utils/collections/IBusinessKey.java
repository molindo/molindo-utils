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

package at.molindo.utils.collections;

/**
 * The contract for classes implementing this interface is:
 * 
 * <ul> <li>o1.getBusinessKey().equals(o2.getBusinessKey()) does not imply
 * o1.equals(o2)</li> <li>o1.equals(o2) implies
 * o1.getBusinessKey().equals(o2.getBusinessKey())</li> </ul>
 * 
 * It's therefore recommended to use (amongst others) .equals(..) and
 * .hashCode() of business key for object equality.
 * 
 * @param <K>
 *            the business key type
 * 
 * @author stf@molindo.at
 */
public interface IBusinessKey<K> {

	/**
	 * @return business key for this object
	 */
	K getBusinessKey();
}
