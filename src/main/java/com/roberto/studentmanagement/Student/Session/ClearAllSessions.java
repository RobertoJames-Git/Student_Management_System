package com.roberto.studentmanagement.Student.Session;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClearAllSessions implements HttpSessionListener {

    // Thread-safe set to hold all active HttpSession objects
    private static final Set<HttpSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Called when a session is created - add it to the set
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        sessions.add(event.getSession());
    }

    // Called when a session is destroyed - remove it from the set
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession());
    }

    /* Invalidate all active sessions tracked by this listener.
       Useful for forcefully logging out all users or clearing server session data. */
    public static void invalidateAllSessions() {
        for (HttpSession session : sessions) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // Session might already be invalidated - ignore
            }
        }
        sessions.clear();
    }
}
