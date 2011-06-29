/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import main.sLog;
/**
 *
 * @author alasdair
 */
public class sEvents {
    
    private static Hashtable mTable;
    private static List<iEvent> delayedEvents;
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
        ListIterator<iEvent> i = delayedEvents.listIterator();
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
