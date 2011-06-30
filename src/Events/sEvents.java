/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import main.sLog;
/**
 *
 * @author alasdair
 */
public class sEvents {
    
    private static Hashtable mTable;
    private static Collection<iEvent> delayedEvents;
    private sEvents()
    {
        
    }
    public static void init()
    {
        mTable = new Hashtable();
        delayedEvents = new LinkedList<iEvent>();
    }
    
    public static void subscribeToEvent(String _eventName, iEventListener _listener)
    {
        iEventListener oldListener = (iEventListener)mTable.put(_eventName, _listener);
        if (oldListener != null)
        {
            sLog.error("Already has a listener, implement this class with a secondary container inside the Hashtable if you want multiple listeners");
        }
    }
    public static void unsubscribeToEvent(String _eventName, iEventListener _listener) throws Throwable
    {
        iEventListener removed = (iEventListener)mTable.remove(_eventName);
        if (removed != _listener)
        {
            sLog.error("Listener was not registered");
        }
    }
    public static void triggerDelayedEvent(iEvent _event)
    {
        delayedEvents.add(_event);
    }
    public static void processEvents()
    {
        Collection<iEvent> currentEvents = delayedEvents;
        Stack<iEvent> newEvents = new Stack<iEvent>();
        delayedEvents = newEvents;
        Iterator<iEvent> i = currentEvents.iterator();
        while(i.hasNext())
        {
            iEvent event = i.next();
            iEventListener listener = (iEventListener)mTable.get(event.getName());
            if (listener != null)
            {
                listener.trigger(event);
            }
            if (event.process())
            {
                i.remove();
            }
        }
        while (!newEvents.isEmpty())
        {
            currentEvents.add(newEvents.pop());
        }
        delayedEvents = currentEvents;
    }
    public static void triggerEvent(iEvent _event)
    {
        iEventListener listener = (iEventListener)mTable.get(_event.getName());
        if (listener != null)
        {
            listener.trigger(_event);
        }
        _event.process();
    }
}
