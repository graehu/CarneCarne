/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.AreaEvents.AreaEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
/**
 *
 * @author alasdair
 */
public class sEvents {
    
    private static Hashtable<String,LinkedList<iEventListener>> mTable = new Hashtable<String,LinkedList<iEventListener>>();
    private static Collection<iEvent> delayedEvents = new LinkedList<iEvent>();
    private static Set<String> blockedEvents = new HashSet<String>();

    public static void addNewAreaEvent(AreaEvent _areaEvent)
    {
    }

    public static void unblockAllEvents()
    {
        blockedEvents = new HashSet<String>();
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
        LinkedList<iEventListener> list = mTable.get(_eventName);
        list.remove(_listener);
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
    public static void blockEvent(String _name)
    {
        blockedEvents.add(_name);
    }
    public static void unblockEvent(String _name)
    {
        blockedEvents.remove(_name);
    }
    public static void triggerEvent(iEvent _event)
    {
        if (!blockedEvents.contains(_event.getName()))
        {
            LinkedList<iEventListener> list = mTable.get(_event.getName());
            if (list != null)
            {
                /*for (iEventListener listener: list)
                    listener.trigger(_event);*/
                Iterator<iEventListener> i = list.iterator();
                while(i.hasNext())
                {
                    iEventListener listener = i.next();
                    if (!listener.trigger(_event))
                    {
                        i.remove();
                    }
                }
            }
            _event.process();
        }
    }
}
