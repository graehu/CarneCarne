/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.AreaEvents.AreaEvent;
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
    
    private static Hashtable<String,LinkedList<iEventListener>> mTable = new Hashtable<String,LinkedList<iEventListener>>();
    private static Collection<iEvent> delayedEvents = new LinkedList<iEvent>();

    public static void addNewAreaEvent(AreaEvent _areaEvent)
    {
    }
    private sEvents()
    {
        
    }
    public static void init()
    {
    }
    
    public static void subscribeToEvent(String _eventName, iEventListener _listener)
    {
        if (mTable.get(_eventName) == null)
        {
            LinkedList<iEventListener> list = new LinkedList<iEventListener>();
            list.add(_listener);
            mTable.put(_eventName, list);
        }
        else
        {
            mTable.get(_eventName).add(_listener);
        }
    }
    public static void unsubscribeToEvent(String _eventName, iEventListener _listener)
    {
        assert(false);
        /*iEventListener removed = (iEventListener)mTable.remove(_eventName);
        if (removed != _listener)
        {
            sLog.error("Listener was not registered");
        }*/
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
            LinkedList<iEventListener> list = mTable.get(event.getName());
            if (list != null)
            {
                for (iEventListener listener: list)
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
        LinkedList<iEventListener> list = mTable.get(_event.getName());
        if (list != null)
        {
            for (iEventListener listener: list)
                listener.trigger(_event);
        }
        _event.process();
    }
}
