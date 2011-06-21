/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.lang.String;
import java.util.Hashtable;
import java.util.Stack;
import main.sLog;
/**
 *
 * @author alasdair
 */
public class sEvents {
    
    private static Hashtable mTable;
    private static Stack<iEvent> delayedEvents;
    private sEvents()
    {
        
    }
    public static void init()
    {
        mTable = new Hashtable();
        delayedEvents = new Stack<iEvent>();
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
        while (!delayedEvents.isEmpty())
        {
            triggerEvent(delayedEvents.pop());
        }
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
