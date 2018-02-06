/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity.Utils;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

public class TestableConsoleLogger {

    private String lastMessage;
    private final List<String> allMessages;
    private PrintStream printStream;

    public TestableConsoleLogger() {
        allMessages = new ArrayList<>();
        printStream = Mockito.mock(PrintStream.class);
        setUpOutputStream();
    }

    public PrintStream getPrintStream() {
        if (printStream == null) {
            new TestableConsoleLogger();
        }

        return printStream;
    }

    private void setUpOutputStream() {
        setUpOutputStream_println();
        setUpOutputStream_Write();
    }

    private void setUpOutputStream_println() {
        Answer<Void> setLastMessage = new Answer<Void>() {
            public Void answer(InvocationOnMock mock) throws Throwable {
                String arg = (String)mock.getArguments()[0];
                lastMessage = arg;
                allMessages.add(arg);
                return null;
            }
        };
        Mockito.doAnswer(setLastMessage).when(printStream).println(anyString());
    }

    private void setUpOutputStream_Write() {
        try{
            Answer<Void> setLastMessage = new Answer<Void>() {
                public Void answer(InvocationOnMock mock) throws Throwable {
                    byte[] obj = (byte[])mock.getArguments()[0];
                    String arg = new String(obj, StandardCharsets.UTF_8);
                    lastMessage = arg;
                    allMessages.add(arg);
                    return null;
                }
            };
            Mockito.doAnswer(setLastMessage).when(printStream).write(Matchers.any(byte[].class));
        } catch (IOException e) {
            // no-op
        }
    }

    public void verifyLastMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastMessage);
    }

    public void verifyMessages(String... expectedMessages) {
        assertArrayEquals(expectedMessages, allMessages.toArray());
    }
}
