/*
 * Copyright (c) 2016-2023 Michael Zhang <yidongnan@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.devh.boot.grpc.server.config;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;

import javax.net.ServerSocketFactory;

/**
 * Simple utility for finding available TCP ports on {@code localhost} for use in integration testing scenarios.
 *
 * <p>
 * This is a limited form of {@link org.springframework.util.SocketUtils} which has been deprecated since Spring
 * Framework 5.3.16 and removed in Spring Framework 6.0.
 *
 * <p>
 * {@code TestSocketUtils} can be used in integration tests which start an external server on an available random port.
 * However, these utilities make no guarantee about the subsequent availability of a given port and are therefore
 * unreliable. Instead of using {@code TestSocketUtils} to find an available local port for a server, it is recommended
 * that you rely on a server's ability to start on a random <em>ephemeral</em> port that it selects or is assigned by
 * the operating system. To interact with that server, you should query the server for the port it is currently using.
 *
 * @author Sam Brannen
 * @author Ben Hale
 * @author Arjen Poutsma
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Chris Bono
 * @since 5.3.24
 */
public class TestSocketUtils {

    /**
     * The minimum value for port ranges used when finding an available TCP port.
     */
    static final int PORT_RANGE_MIN = 1024;

    /**
     * The maximum value for port ranges used when finding an available TCP port.
     */
    static final int PORT_RANGE_MAX = 65535;

    private static final int PORT_RANGE_PLUS_ONE = PORT_RANGE_MAX - PORT_RANGE_MIN + 1;

    private static final int MAX_ATTEMPTS = 1_000;

    private static final Random random = new Random(System.nanoTime());

    private static final TestSocketUtils INSTANCE = new TestSocketUtils();


    /**
     * Although {@code TestSocketUtils} consists solely of static utility methods, this constructor is intentionally
     * {@code public}.
     * <h5>Rationale</h5>
     * <p>
     * Static methods from this class may be invoked from within XML configuration files using the Spring Expression
     * Language (SpEL) and the following syntax.
     * 
     * <pre>
     * <code>
     * &lt;bean id="myBean" ... p:port="#{T(org.springframework.test.util.TestSocketUtils).findAvailableTcpPort()}" /&gt;</code>
     * </pre>
     * <p>
     * If this constructor were {@code private}, you would be required to supply the fully qualified class name to
     * SpEL's {@code T()} function for each usage. Thus, the fact that this constructor is {@code public} allows you to
     * reduce boilerplate configuration with SpEL as can be seen in the following example.
     * 
     * <pre>
     * <code>
     * &lt;bean id="socketUtils" class="org.springframework.test.util.TestSocketUtils" /&gt;
     * &lt;bean id="myBean" ... p:port="#{socketUtils.findAvailableTcpPort()}" /&gt;</code>
     * </pre>
     */
    public TestSocketUtils() {}

    /**
     * Find an available TCP port randomly selected from the range [1024, 65535].
     * 
     * @return an available TCP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableTcpPort() {
        return INSTANCE.findAvailableTcpPortInternal();
    }


    /**
     * Internal implementation of {@link #findAvailableTcpPort()}.
     * <p>
     * Package-private solely for testing purposes.
     */
    int findAvailableTcpPortInternal() {
        int candidatePort;
        int searchCounter = 0;
        do {
            /*
             * Assert.state(++searchCounter <= MAX_ATTEMPTS, () -> String.format(
             * "Could not find an available TCP port in the range [%d, %d] after %d attempts", PORT_RANGE_MIN,
             * PORT_RANGE_MAX, MAX_ATTEMPTS));
             */
            candidatePort = PORT_RANGE_MIN + random.nextInt(PORT_RANGE_PLUS_ONE);
        } while (!isPortAvailable(candidatePort));

        return candidatePort;
    }

    /**
     * Determine if the specified TCP port is currently available on {@code localhost}.
     * <p>
     * Package-private solely for testing purposes.
     */
    boolean isPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault()
                    .createServerSocket(port, 1, InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
