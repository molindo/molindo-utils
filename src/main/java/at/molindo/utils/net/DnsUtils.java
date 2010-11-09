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

package at.molindo.utils.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class DnsUtils {
	private DnsUtils() {

	}

	/**
	 * best effort method to determine host name of localhost. falls back to
	 * 'localhost' if not possible
	 * 
	 * @return a domain name
	 */
	public static String getLocalHostName() {
		try {
			final String name = InetAddress.getLocalHost().getCanonicalHostName();
			if (name == null || name.length() == 0) {
				return "localhost";
			} else if (name.equals(InetAddress.getLocalHost().getHostAddress())) {
				return "localhost";
			} else {
				return name;
			}
		} catch (final UnknownHostException e) {
			return "localhost";
		}
	}

	/**
	 * returns a String array of mail exchange servers (mail hosts) sorted from
	 * most preferred to least preferred source:
	 * http://ww2.cs.fsu.edu/~steele/MAILHOSTS/mailHostsLookup.html
	 */
	public static String[] lookupMailHosts(final String domainName) throws NamingException {
		// see: RFC 974 - Mail routing and the domain system
		// see: RFC 1034 - Domain names - concepts and facilities
		// see: http://java.sun.com/j2se/1.5.0/docs/guide/jndi/jndi-dns.html
		// - DNS Service Provider for the Java Naming Directory Interface
		// (JNDI)

		// get the default initial Directory Context
		final InitialDirContext iDirC = new InitialDirContext();
		// get the MX records from the default DNS directory service
		// provider
		// NamingException thrown if no DNS record found for domainName
		final Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[] { "MX" });
		// attributeMX is an attribute ('list') of the Mail Exchange(MX)
		// Resource Records(RR)
		final Attribute attributeMX = attributes.get("MX");

		// if there are no MX RRs then default to domainName (see: RFC 974)
		if (attributeMX == null) {
			return new String[] { domainName };
		}

		// split MX RRs into Preference Values(pvhn[0]) and Host
		// Names(pvhn[1])
		final String[][] pvhn = new String[attributeMX.size()][2];
		for (int i = 0; i < attributeMX.size(); i++) {
			pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
		}

		// sort the MX RRs by RR value (lower is preferred)
		Arrays.sort(pvhn, new Comparator<String[]>() {
			public int compare(final String[] o1, final String[] o2) {
				return Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]);
			}
		});

		// put sorted host names in an array, get rid of any trailing '.'
		final String[] sortedHostNames = new String[pvhn.length];
		for (int i = 0; i < pvhn.length; i++) {
			sortedHostNames[i] = pvhn[i][1].endsWith(".") ? pvhn[i][1].substring(0, pvhn[i][1].length() - 1)
					: pvhn[i][1];
		}
		return sortedHostNames;
	}

}
