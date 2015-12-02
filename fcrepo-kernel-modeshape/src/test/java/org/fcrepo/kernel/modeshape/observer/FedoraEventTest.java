/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fcrepo.kernel.modeshape.observer;

import static com.google.common.collect.Iterators.contains;
import static java.util.Collections.singleton;
import static javax.jcr.observation.Event.PROPERTY_CHANGED;
import static org.fcrepo.kernel.api.utils.EventType.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Map;

import javax.jcr.observation.Event;

import org.junit.Test;
import org.fcrepo.kernel.api.observer.FedoraEvent;
import org.fcrepo.kernel.api.utils.EventType;

import com.google.common.collect.ImmutableMap;

/**
 * <p>FedoraEventTest class.</p>
 *
 * @author ksclarke
 */
public class FedoraEventTest {

    FedoraEvent e = new FedoraEventImpl(new TestEvent(1, "Path/Child", "UserId", "Identifier",
            ImmutableMap.of("1", "2"), "data", 0L));


    @SuppressWarnings("unused")
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrapNullEvent() {
        new FedoraEventImpl((Event)null);
    }

    @SuppressWarnings("unused")
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrapNullFedoraEvent() {
        new FedoraEventImpl((FedoraEvent)null);
    }

    @Test
    public void testGetType() {
        assertEquals(singleton(valueOf(1)), e.getTypes());
    }

    @Test
    public void testGetPath() {
        assertEquals("Path/Child", e.getPath());

    }

    @Test
    public void testGetPathWithProperties() {
        final FedoraEvent e = new FedoraEventImpl(new TestEvent(PROPERTY_CHANGED,
                                                            "Path/Child",
                                                            "UserId",
                                                            "Identifier",
                                                            ImmutableMap.of("1", "2"),
                                                            "data",
                                                            0L));
        assertEquals("Path", e.getPath());
    }

    @Test
    public void testGetUserID() {

        assertEquals("UserId", e.getUserID());

    }

    @Test
    public void testGetEventID() {

        assertNotNull(e.getEventID());

    }

    @Test
    public void testGetUserData() {

        assertEquals("data", e.getUserData());

    }

    @Test
    public void testGetDate() {
        assertEquals(0L, e.getDate());

    }

    @Test
    public void testAddType() {
        final EventType type = valueOf(PROPERTY_CHANGED);
        e.addType(type);
        assertEquals(2, e.getTypes().size());

        assertTrue("Should contain: " + type, contains(e.getTypes().iterator(), type));
        assertTrue("Should contain: NODE_ADDED", contains(e.getTypes().iterator(), valueOf(1)));
    }

    @Test
    public void testAddProperty() {
        e.addProperty("prop");
        assertEquals(1, e.getProperties().size());
        assertEquals("prop", e.getProperties().iterator().next());
    }

    @Test
    public void testToString() {
        final String text = e.toString();
        assertTrue("Should contain path: " + text, text.contains(e.getPath()));

        assertTrue("Should contain types: " + text, text.contains(e.getTypes().iterator().next().getName()));
        assertTrue("Should contain date: " + text, text.contains(Long.toString(e.getDate())));

        assertFalse("Should not contain user-data: " + text, text.contains(e.getUserData()));
        assertFalse("Should not contain user-id: " + text, text.contains(e.getUserID()));
    }

    class TestEvent implements Event {

        private final int type;

        private final String path;

        private final String user_id;

        private final String identifier;

        private final Map<String, String> info;

        private final String userData;

        private final long date;

        public TestEvent(final int type, final String path,
                final String user_id, final String identifier,
                final Map<String, String> info, final String userData,
                final long date) {
            this.type = type;
            this.path = path;
            this.user_id = user_id;
            this.identifier = identifier;
            this.info = info;
            this.userData = userData;
            this.date = date;
        }


        @Override
        public int getType() {
            return type;
        }


        @Override
        public String getPath() {
            return path;
        }


        @Override
        public String getUserID() {
            return user_id;
        }


        @Override
        public String getIdentifier() {
            return identifier;
        }


        @Override
        public Map<String, String> getInfo() {
            return info;
        }


        @Override
        public String getUserData() {
            return userData;
        }


        @Override
        public long getDate() {
            return date;
        }
    }
}