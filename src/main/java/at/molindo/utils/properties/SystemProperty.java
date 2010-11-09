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

package at.molindo.utils.properties;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * <p> This enum is supposed to be a complete list of available system
 * properties on different JVM and Java versions. And with complete I don't mean
 * the list documented at {@link System#getProperties()}. I mean properties that
 * are specific to different verndors, versions and operating stystems. </p> <p>
 * Use {@link #get()} or {@link #toString()} to access the property values or
 * {@link #set(String)} to update them - but only those that aren't read-only (
 * {@link #isReadOnly()}). </p> <p> If you discover some new properties in your
 * environment, please run the contained {@link #main(String[])} method and send
 * the output to me: </p> <p> You may want to check for updates once in a while:
 * <a href="http://techblog.molindo.at/files/SystemProperty.java"
 * >SystemProperty.java</a> and <a
 * href="http://techblog.molindo.at/2009/11/java-system-properties.html">Molindo
 * Techblog: The Final Take On Java System Properties</a> </p>
 * 
 * <p> <strong>List of tested JVMs</strong><br /> <small>If your JVM is missing,
 * please <a
 * href="http://techblog.molindo.at/2009/11/java-system-properties.html">send
 * me</a> your output of the included {@link #main(String[])} method</small>
 * <ul> <li>Sun JVM 1.6 <ul> <li>Ubuntu Hardy</li> <li>Cent OS</li> <li>Mac OS
 * X</li> <li>Windows XP</li> </ul> <ul> <li>Sun JVM 1.5 <ul> <li>Ubuntu
 * Hardy</li> </ul> </li> <li>Open JDK 6 <ul> <li>Ubuntu Hardy</li> </ul> </li>
 * <li>IBM JVM 1.4.2 <ul> <li>Windows XP</li> </ul> </li> </ul> <p>
 * 
 * @see System#getProperties()
 * 
 * @author Stefan Fussenegger
 */
public enum SystemProperty {

	/**
	 * <p> <strong>Known values:</strong> <ul> <li>"MacRoman": Mac OS X</li>
	 * <li>"Cp1252": Windows XP</li> <li>"UTF-8": CentOS, Ubuntu Hardy/JVM 1.5,
	 * Ubuntu Hardy/Open JDK 6</li> <li>"ISO-8859-1": Ubuntu Hardy/JVM 1.6</li>
	 * </ul> </p> <em>Note: This property <strong>is not</strong> part of
	 * {@link System#getProperties()} doc.</em>
	 */
	FILE_ENCODING("file.encoding"),

	/**
	 * This property is available on all <p> <strong>Known values:</strong> <ul>
	 * <li>"sun.io"</li> </ul> </p> <em>Note: This property <strong>is
	 * not</strong> part of {@link System#getProperties()} doc.</em>
	 */
	FILE_ENCODING_PKG("file.encoding.pkg"),

	/**
	 * <p> {@link System#getProperties()} doc: File separator ("/" on UNIX) </p>
	 * 
	 * <p> <strong>Known values:</strong> <ul> <li>"\" (Windows)</li> <li>"/"
	 * (Mac OS X, Linux)</li> <li>"\\" (Windows, IBM JVM)</li> </ul> </p>
	 */
	FILE_SEPARATOR("file.separator"),

	/**
	 * <p> <strong>Known values:</strong> <ul>
	 * <li>"sun.awt.X11GraphicsEnvironment": Ubuntu Hardy</li>
	 * <li>"apple.awt.CGraphicsEnvironment": Mac OS X</li>
	 * <li>"sun.awt.Win32GraphicsEnvironment": Windows XP</li> </ul> </p>
	 * <em>Note: This property <strong>is not</strong> part of
	 * {@link System#getProperties()} doc.</em>
	 */
	JAVA_AWT_GRAPHICSENV("java.awt.graphicsenv"), JAVA_AWT_PRINTERJOB("java.awt.printerjob"),

	JAVA_CLASS_PATH("java.class.path"), JAVA_CLASS_VERSION("java.class.version"), JAVA_COMPILER("java.compiler"), JAVA_ENDORSED_DIRS(
			"java.endorsed.dirs"), JAVA_EXT_DIRS("java.ext.dirs"),

	JAVA_HOME("java.home"),

	/**
	 * path to temporary directory including trailing file separator
	 */
	JAVA_IO_TMPDIR("java.io.tmpdir", Type.READ_WRITE) {
		@Override
		public String get() {
			String value = super.get();
			if (value == null) {
				// you'll never know
				return null;
			}

			if (!value.endsWith(File.separator)) {
				// http://rationalpi.wordpress.com/2007/01/26/javaiotmpdir-inconsitency/
				value += File.separator;
			}

			// make sure dir exists
			try {
				new File(value).mkdirs();
			} catch (final SecurityException e) {
				log.warn("not allowed to create temporary directory: " + value, e);
			}
			return value;
		}
	},

	JAVA_LIBRARY_PATH("java.library.path"), JAVA_RUNTIME_NAME("java.runtime.name"), JAVA_RUNTIME_VERSION(
			"java.runtime.version"),

	JAVA_SPECIFICATION_NAME("java.specification.name"), JAVA_SPECIFICATION_VENDOR("java.specification.vendor"), JAVA_SPECIFICATION_VERSION(
			"java.specification.version"),

	JAVA_VERSION("java.version"), JAVA_VENDOR("java.vendor"), JAVA_VENDOR_URL("java.vendor.url"), JAVA_VENDOR_URL_BUG(
			"java.vendor.url.bug"),

	JAVA_VM_INFO("java.vm.info"),

	/**
	 * This property may be used to distinguis between a JVM running in defaul
	 * (client) and server mode
	 * 
	 * known values: "Java HotSpot(TM) 64-Bit Server VM",
	 * "Java HotSpot(TM) Client VM"
	 */
	JAVA_VM_NAME("java.vm.name"), JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"), JAVA_VM_SPECIFICATION_VENDOR(
			"java.vm.specification.vendor"), JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"), JAVA_VM_VERSION(
			"java.vm.version"), JAVA_VM_VENDOR("java.vm.vendor"),

	LINE_SEPARATOR("line.separator"),

	/**
	 * see http://lopica.sourceforge.net/os.html for possible values
	 */
	OS_NAME("os.name"), OS_ARCH("os.arch"), OS_VERSION("os.version"),

	PATH_SEPARATOR("path.separator"),

	SUN_ARCH_DATA_MODEL("sun.arch.data.model"), SUN_BOOT_CLASS_PATH("sun.boot.class.path"), SUN_BOOT_LIBRARY_PATH(
			"sun.boot.library.path"), SUN_CPU_ENDIAN("sun.cpu.endian"), SUN_CPU_ISALIST("sun.cpu.isalist"),

	SUN_IO_UNICODE_ENCODING("sun.io.unicode.encoding"), SUN_JAVA_LAUNCHER("sun.java.launcher"), SUN_JNU_ENCODING(
			"sun.jnu.encoding"), SUN_MANAGEMENT_COMPILER("sun.management.compiler"), SUN_OS_PATCH_LEVEL(
			"sun.os.patch.level"),

	USER_COUNTRY("user.country"), USER_DIR("user.dir"), USER_HOME("user.home"), USER_LANGUAGE("user.language"), USER_NAME(
			"user.name"), USER_TIMEZONE("user.timezone"),

	/*
	 * Windows ONLY
	 */
	/**
	 * Windows only: known values: "" (empty string)
	 */
	USER_VARIANT("user.variant"),

	/*
	 * Linux ONLY
	 */
	/**
	 * Linux only: known values: gnome
	 */
	SUN_DESKTOP("sun.desktop"),

	/**
	 * Linux only (seen on Sun an OpenJDK JVMs): known values: /usr/share/javazi
	 */
	USER_ZONEINFO_DIR("user.zoneinfo.dir"),

	/*
	 * MAC ONLY
	 */
	/**
	 * Mac only: true or false
	 */
	AWT_NATIVE_DOUBLE_BUFFERING("awt.nativeDoubleBuffering"),
	/**
	 * Mac only: known values: apple.awt.CToolkit
	 */
	AWT_TOOLKIT("awt.toolkit"),

	/**
	 * Mac only: known values: local|*.local|169.254/16|*.169.254/16
	 */
	FTP_NON_PROXY_HOSTS("ftp.nonProxyHosts"),

	/**
	 * Mac only: true or false
	 */
	GOPHER_PROXY_SET("gopherProxySet"),
	/**
	 * Mac only: known values: local|*.local|169.254/16|*.169.254/16
	 */
	HTTP_NON_PROXY_HOSTS("http.nonProxyHosts"),

	/**
	 * Mac only: known values: 1060.1.6.0_15-219
	 */
	MRJ_VERSION("mrj.version"),

	/**
	 * Mac only: known values: local|*.local|169.254/16|*.169.254/16
	 */
	SOCKS_NON_PROXY_HOSTS("socksNonProxyHosts"),

	/*
	 * GCJ only
	 */
	/**
	 * GCJ only: known values: /usr
	 */
	GNU_CLASSPATH_HOME("gnu.classpath.home"),
	/**
	 * GCJ only: known values: file:///usr/lib64
	 */
	GNU_CLASSPATH_HOME_URL("gnu.classpath.home.url"),
	/**
	 * GCJ only: known values: 0.98
	 */
	GNU_CLASSPATH_VERSION("gnu.classpath.version"),
	/**
	 * GCJ only: known values: libgcj
	 */
	GNU_CLASSPATH_VM_SHORTNAME("gnu.classpath.vm.shortname"),
	/**
	 * GCJ only: known values: little
	 */
	GNU_CPU_ENDIAN("gnu.cpu.endian"),
	/**
	 * GCJ only: known values: /usr/lib64/gcj-4.4.1/classmap.db
	 */
	GNU_GCJ_PRECOMPILED_DB_PATH("gnu.gcj.precompiled.db.path"),
	/**
	 * GCJ only: known values: SystemProperty
	 */
	GNU_GCJ_PROGNAME("gnu.gcj.progname"),
	/**
	 * GCJ only: known values: /usr/share/java/gcj-endorsed
	 */
	GNU_GCJ_RUNTIME_ENDORSED_DIRS("gnu.gcj.runtime.endorsed.dirs"),
	/**
	 * GCJ only: known values:
	 */
	GNU_GCJ_USER_REALNAME("gnu.gcj.user.realname"),
	/**
	 * GCJ only: known values: /usr/share/zoneinfo
	 */
	GNU_JAVA_UTIL_ZONEINFO_DIR("gnu.java.util.zoneinfo.dir"),
	/**
	 * GCJ only: known values: gnu-classpath/0.98 (libgcj/4.4.1 20090725 (Red
	 * Hat 4.4.1-2))
	 */
	HTTP_AGENT("http.agent"),
	/**
	 * GCJ only: known values: GNU libgcj 4.4.1 20090725 (Red Hat 4.4.1-2)
	 */
	JAVA_FULLVERSION("java.fullversion"),
	/**
	 * GCJ only: known values: US
	 */
	USER_REGION("user.region"),

	/**
	 * AIX only
	 */
	/**
	 * AIX only: known values: big
	 */
	COM_IBM_CPU_ENDIAN("com.ibm.cpu.endian"),
	/**
	 * AIX only: known values: scar
	 */
	COM_IBM_OTI_CONFIGURATION("com.ibm.oti.configuration"),
	/**
	 * AIX only: known values: 20081111_1646
	 */
	COM_IBM_OTI_JCL_BUILD("com.ibm.oti.jcl.build"),
	/**
	 * AIX only: known values: /prj/was/java/jre/bin
	 */
	COM_IBM_OTI_VM_BOOTSTRAP_LIBRARY_PATH("com.ibm.oti.vm.bootstrap.library.path"),
	/**
	 * AIX only: known values: 23
	 */
	COM_IBM_OTI_VM_LIBRARY_VERSION("com.ibm.oti.vm.library.version"),
	/**
	 * AIX only: known values:
	 */
	COM_IBM_UTIL_EXTRALIBS_PROPERTIES("com.ibm.util.extralibs.properties"),
	/**
	 * AIX only: known values: 32
	 */
	COM_IBM_VM_BITMODE("com.ibm.vm.bitmode"),
	/**
	 * AIX only: known values: false
	 */
	IBM_SIGNALHANDLING_RS("ibm.signalhandling.rs"),
	/**
	 * AIX only: known values: true
	 */
	IBM_SIGNALHANDLING_SIGCHAIN("ibm.signalhandling.sigchain"),
	/**
	 * AIX only: known values: true
	 */
	IBM_SIGNALHANDLING_SIGINT("ibm.signalhandling.sigint"),
	/**
	 * AIX only: known values: ISO8859-1
	 */
	IBM_SYSTEM_ENCODING("ibm.system.encoding"),
	/**
	 * AIX only: known values:
	 */
	INVOKEDVIAJAVA("invokedviajava"),
	/**
	 * AIX only: known values: ON
	 */
	JAVA_ASSISTIVE("java.assistive"),
	/**
	 * AIX only: known values:
	 */
	JAVA_AWT_FONTS("java.awt.fonts"),
	/**
	 * AIX only: known values: 20090506
	 */
	JAVA_JCL_VERSION("java.jcl.version"),
	/**
	 * AIX only: known values: java.util.prefs.FileSystemPreferencesFactory
	 */
	JAVA_UTIL_PREFS__PREFERENCES_FACTORY("java.util.prefs.PreferencesFactory"),
	/**
	 * AIX only: known values: 9
	 */
	JXE_CURRENT_ROMIMAGE_VERSION("jxe.current.romimage.version"),
	/**
	 * AIX only: known values: 9
	 */
	JXE_LOWEST_ROMIMAGE_VERSION("jxe.lowest.romimage.version"),
	/**
	 * AIX only: known values:
	 */
	SUN_JAVA2D_FONTPATH("sun.java2d.fontpath"),

	/*
	 * Groovy only
	 */
	/**
	 * Groovy only: known values: groovy
	 */
	PROGRAM_NAME("program.name"),

	/**
	 * Groovy only: known values: /opt/groovy-1.6.4
	 */
	GROOVY_HOME("groovy.home"),

	/**
	 * Groovy only: known values: /opt/groovy-1.6.4/conf/groovy-starter.conf
	 */
	GROOVY_STARTER_CONF("groovy.starter.conf"),

	/*
	 * other common properties
	 */
	/**
	 * turns on headless mode
	 */
	JAVA_AWT_HEADLESS("java.awt.headless", Type.READ_WRITE) {
		@Override
		public String getDefault() {
			return "false";
		}
	},

	/**
	 * TODO doc (hint: for Sun VM on Java 6u14+)
	 */
	SUN_AWT_DISABLE_MIXING("sun.awt.disableMixing", Type.READ_WRITE),

	/**
	 * TODO doc
	 */
	SUN_AWT_NOERASEBACKGROUND("sun.awt.noerasebackground", Type.READ_WRITE),

	/**
	 * TODO doc
	 */
	SUN_AWT_XEMBEDSERVER("sun.awt.xembedserver", Type.READ_WRITE),

	/*
	 * derived properties
	 */
	/**
	 * <ul> <li><strong>windows</strong> if OS name contains the word
	 * "windows"</li> <li><strong>os/2</strong> if OS name contains the word
	 * "os/2"</li> <li><strong>netware</strong> if OS name contains the word
	 * "netware"</li> <li><strong>dos</strong> if OS family is not "netware",
	 * and its path separator is ";"</li> <li><strong>mac</strong> if OS name
	 * contains the word "mac"</li> <li><strong>tandem</strong> if OS name
	 * contains the word "nonstop_kernel" </li> <li><strong>unix</strong> if OS
	 * family is not "openvms" and not "mac" which names does not end with "X",
	 * and its path separator is ":"</li> <li><strong>win9x</strong> is OS
	 * family is "windows" and OS name contains "95", "98", "me", or "ce"</li>
	 * <li><strong>z/os</strong> if OS name contains the word "z/os" or "os/390"
	 * </li> <li><strong>os/400</strong> if OS name contains the word
	 * "os/400"</li> <li><strong>openvms</strong> if OS name contains the word
	 * "openvms"</li> <li><strong>unknown</strong> if none of the above matches
	 * </ul>
	 */
	OS_FAMILY("os.family") {
		@Override
		public String get() {
			final String osName = OS_NAME.get();
			if (osName != null) {
				final String os = osName.toLowerCase();
				if (os.contains("windows")) {
					if (os.contains("95") || os.contains("98") || os.contains("me") || os.contains("ce")) {
						return "win9x";
					} else {
						return "windows";
					}
				} else if (os.contains("mac")) {
					return "mac";
				} else if (os.contains("os/2")) {
					return "os/2";
				} else if (os.contains("os/400")) {
					return "os/400";
				} else if (os.contains("os/390") || os.contains("z/os")) {
					return "z/os";
				} else if (os.contains("netware")) {
					return "netware";
				} else if (os.contains("nonstop_kernel")) {
					return "tandem";
				} else if (os.contains("openvms")) {
					return "openvms";
				} else {
					final String pathSeparator = PATH_SEPARATOR.get();
					if (";".equals(pathSeparator)) {
						return "dos";
					}
					if (":".equals(pathSeparator) && !os.endsWith("X")) {
						return "unix";
					}
				}
			}
			return "unknown";
		}

		@Override
		public boolean isDerived() {
			return true;
		}
	},

	/**
	 * <p> returns true if VM is running in "Server Mode" (java -server). This
	 * is only true if java.vm.name starts with "Java HotSpot(TM)" and contains
	 * "Server". <code>null</code> is returned if java.vm.name is null (which
	 * should never be the case). </p> <p> <a href=
	 * "http://stackoverflow.com/questions/1833129/how-to-make-sure-im-using-the-server-jvm"
	 * > Stackoverflow: How to make sure I'm using the "server" JVM? </a> </p>
	 */
	JAVA_VM_SERVER("java.vm.server") {
		public String get() {
			final String vm = JAVA_VM_NAME.get();
			if (vm == null) {
				return null;
			}
			if (vm.startsWith("Java HotSpot(TM)") && vm.contains("Server")) {
				return Boolean.TRUE.toString();
			} else {
				return Boolean.FALSE.toString();
			}
		}

		@Override
		public boolean isDerived() {
			return true;
		}
	};

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SystemProperty.class);

	/**
	 * Type of property (only used in constructor for readability)
	 */
	private enum Type {
		READ_WRITE, READ_ONLY;
	}

	private final String _name;
	private final boolean _readOnly;

	private SystemProperty(final String name) {
		this(name, Type.READ_ONLY);
	}

	private SystemProperty(String name, final Type type) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		name = name.trim();
		if ("".equals(name)) {
			throw new IllegalArgumentException();
		}

		_name = name;
		_readOnly = type == Type.READ_ONLY;

		if (!isReadOnly() && isDerived()) {
			throw new RuntimeException("derived properties must be read-only: " + this);
		}
	}

	/**
	 * @see System#getProperty(String)
	 * @see AccessController#doPrivileged(PrivilegedAction)
	 * @return the string value of the system property, or <code>null</code> if
	 *         there is no property with that key and {@link #getDefault()} is
	 *         <code>null</code> too
	 */
	public String get() {
		final String val = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty(getName());
			}
		});
		return val != null ? val : getDefault();
	}

	/**
	 * @see System#getProperty(String)
	 * @see AccessController#doPrivileged(PrivilegedAction)
	 * @return the string value of the system property, or <code>def</code> if
	 *         there is no property with that key. ({@link #getDefault()} is
	 *         ignored)
	 */
	public String get(final String def) {
		final String val = AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty(getName());
			}
		});
		return val != null ? val : def;
	}

	/**
	 * @return returns parsed boolean or <code>null</code> if {@link #get()}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Boolean#parseBoolean(String)
	 */
	public Boolean getBoolean() {
		final String val = get();
		return val == null ? null : Boolean.parseBoolean(val);
	}

	/**
	 * @return returns parsed boolean or <code>null</code> if
	 *         {@link #get(String)} returned null
	 * 
	 * @see #get()
	 * @see Integer#parseInt(String)
	 */
	public Boolean getBoolean(final Boolean def) {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : Boolean.parseBoolean(val);
	}

	/**
	 * @return returns parsed integer or <code>null</code> if {@link #get()}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Integer#parseInt(String)
	 * @exception NumberFormatException
	 */
	public Integer getInteger() throws NumberFormatException {
		final String val = get();
		return val == null ? null : Integer.parseInt(val);
	}

	/**
	 * @return returns parsed integer or <code>null</code> if
	 *         {@link #get(String)} returned null
	 * 
	 * @see #get()
	 * @see Integer#parseInt(String)
	 * @exception NumberFormatException
	 */
	public Integer getInteger(final Integer def) throws NumberFormatException {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : Integer.parseInt(val);
	}

	/**
	 * @return returns parsed Long or <code>null</code> if {@link #get()}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Long#parseLong(String)
	 * @exception NumberFormatException
	 */
	public Long getLong() throws NumberFormatException {
		final String val = get();
		return val == null ? null : Long.parseLong(val);
	}

	/**
	 * @return returns parsed Long or <code>null</code> if {@link #get(String)}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Long#parseLong(String)
	 * @exception NumberFormatException
	 */
	public Long getLong(final Long def) throws NumberFormatException {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : Long.parseLong(val);
	}

	/**
	 * @return returns parsed Float or <code>null</code> if {@link #get()}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Float#parseFloat(String)
	 * @exception NumberFormatException
	 */
	public Float getFloat() throws NumberFormatException {
		final String val = get();
		return val == null ? null : Float.parseFloat(val);
	}

	/**
	 * @return returns parsed Float or <code>null</code> if {@link #get(String)}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Float#parseFloat(String)
	 * @exception NumberFormatException
	 */
	public Float getFloat(final Float def) throws NumberFormatException {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : Float.parseFloat(val);
	}

	/**
	 * @return returns parsed Double or <code>null</code> if {@link #get()}
	 *         returned null
	 * 
	 * @see #get()
	 * @see Double#parseDouble(String)
	 * @exception NumberFormatException
	 */
	public Double getDouble() throws NumberFormatException {
		final String val = get();
		return val == null ? null : Double.parseDouble(val);
	}

	/**
	 * @return returns parsed Double or <code>null</code> if
	 *         {@link #get(String)} returned null
	 * 
	 * @see #get()
	 * @see Double#parseDouble(String)
	 * @exception NumberFormatException
	 */
	public Double getDouble(final Double def) throws NumberFormatException {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : Double.parseDouble(val);
	}

	/**
	 * @see #get()
	 * @return a file constructed with the returned value of {@link #get()} or
	 *         null
	 */
	public File getFile() {
		final String val = get();
		return val == null ? null : new File(val);
	}

	/**
	 * 
	 * @see #get()
	 * @param def
	 *            default value
	 * @return a file constructed with the returned value of
	 *         {@link #get(String)} or null
	 */
	public File getFile(final String def) {
		final String val = get(def == null ? null : def.toString());
		return val == null ? null : new File(val);
	}

	/**
	 * @param value
	 *            the new value
	 * @see System#setProperty(String, String)
	 * @see AccessController#doPrivileged(PrivilegedAction)
	 * @see #isReadOnly()
	 * @exception UnsupportedOperationException
	 *                if this property is read-only
	 * @return the previous value of this system property or null if it didn't
	 *         have one
	 */
	public String set(final String value) {
		if (isReadOnly()) {
			throw new UnsupportedOperationException(getName() + " is a read-only property");
		}

		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return System.setProperty(getName(), value);
			}
		});
	}

	public String setBoolean(final Boolean value) throws NumberFormatException {
		return set(value == null ? null : value.toString());
	}

	public String setInteger(final Integer value) throws NumberFormatException {
		return set(value == null ? null : value.toString());
	}

	public String setLong(final Long value) throws NumberFormatException {
		return set(value == null ? null : value.toString());
	}

	public String setDouble(final Double value) throws NumberFormatException {
		return set(value == null ? null : value.toString());
	}

	public String setFloat(final Float value) throws NumberFormatException {
		return set(value == null ? null : value.toString());
	}

	public String setFile(final File value) throws IOException {
		return set(value == null ? null : value.getCanonicalPath());
	}

	public String unset() {
		return set((String) null);
	}

	/**
	 * @return name of this property
	 */
	public String getName() {
		return _name;
	}

	/**
	 * whether this property should not be modified (i.e. "read-only"). Note
	 * that it is possible to use {@link System#setProperty(String, String)}
	 * directly. It's use is discouraged though (as it might not have the
	 * desired effect)
	 * 
	 * @return <code>true</code> if this property should not be modified
	 */
	public boolean isReadOnly() {
		return _readOnly;
	}

	/**
	 * @return <code>true</code> if this is property doesn't exist but is
	 *         derived from another property (e.g. OS_FAMILY which is derived by
	 *         other properties, mainly OS_NAME)
	 */
	public boolean isDerived() {
		return false;
	}

	/**
	 * the default return value if property is not set. for most properties this
	 * is <code>null</code>. For example, the default for java.awt.headless is
	 * "false"
	 * 
	 * @return the default value for this property or <code>null</code> if not
	 *         applicable
	 */
	public String getDefault() {
		return null;
	}

	/**
	 * @return property value (same as {@link #get()}
	 * @see #get()
	 */
	@Override
	public String toString() {
		return get();
	}

	/**
	 * @return a string representation of this object (e.g.
	 *         "OS_NAME: os.name=Linux (read-only)")
	 */
	public String toDebugString() {
		final StringBuilder buf = new StringBuilder();
		buf.append(name()).append(": ");
		buf.append(getName()).append("=");
		buf.append(get());
		if (isDerived()) {
			buf.append(" (derived)");
		} else if (isReadOnly()) {
			buf.append(" (read-only)");
		}
		return buf.toString();
	}

	/**
	 * a simple main method to list available system properties. additionally,
	 * it generates Java code for yet unknown properties. If you find some of
	 * them, please let us know:
	 * http://techblog.molindo.at/2009/11/java-system-properties.html
	 * 
	 * @param args
	 *            arguments are ignored
	 */
	public static void main(final String[] args) {
		final TreeMap<Object, Object> props = new TreeMap<Object, Object>();
		final TreeSet<SystemProperty> unknown = new TreeSet<SystemProperty>();

		props.putAll(System.getProperties());

		for (final SystemProperty p : SystemProperty.values()) {
			System.out.println(p.toDebugString());
			if (!props.containsKey(p.getName())) {
				unknown.add(p);
			} else {
				props.remove(p.getName());
			}

			checkNaming(p);
		}

		if (unknown.size() > 0) {
			System.out.println("\n\n### UNKNOWN");
			for (final SystemProperty p : unknown) {
				System.out.println(p.toDebugString());
			}
		}

		if (props.size() > 0) {
			System.out.println("\n\n### MISSING");
			for (final Map.Entry<Object, Object> e : props.entrySet()) {
				System.out.println(e);
			}

			System.out.println("\n\n### PLEASE POST FULL OUTPUT . AT http://j.mp/props0 or http://j.mp/props1");
			for (final Map.Entry<Object, Object> e : props.entrySet()) {
				System.out.println(String.format("\t/**\n\t * %s only: known values: %s\n\t */\n\t%s(\"%s\"),",
						OS_NAME, e.getValue(), toEnumName((String) e.getKey()), e.getKey()));
			}
		} else {
			System.out
					.println("\n\n### NO MISSING PROPERTIES, PLEASE POST FULL OUTPUT AT http://j.mp/props0 or http://j.mp/props1");
		}
	}

	/**
	 * check naming of enums (mainly to spot possible typos)
	 * 
	 * @param p
	 *            the SytemProperty to check
	 */
	private static void checkNaming(final SystemProperty p) {
		final String expected = toEnumName(p.getName());

		if (!p.name().equals(expected)) {
			System.err.println("name missmatch: " + p.toDebugString() + " (expected " + expected + ")");
		}
	}

	/**
	 * generate a new enum name from a system property
	 * 
	 * @param property
	 *            property name (e.g. "os.name")
	 * 
	 * @return the resulting enum name (e.g. "OS_NAME")
	 */
	public static String toEnumName(final String property) {
		final StringBuilder buf = new StringBuilder();
		for (final char c : property.toCharArray()) {
			if (Character.isUpperCase(c)) {
				buf.append('_').append(c);
			} else if (c == '.') {
				buf.append('_');
			} else {
				buf.append(Character.toUpperCase(c));
			}
		}
		return buf.toString();
	}
}
