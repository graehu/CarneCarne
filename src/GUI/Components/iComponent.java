
package GUI.Components;

import java.util.ArrayList;
import org.newdawn.slick.geom.Transform;

/**
 *
 * @author Aaron
 */
public abstract class iComponent {

    public iComponent() {
    }
    
    iComponent mParent;
    ArrayList<iComponent> mChildren;
    Transform mLocalTransform;
    
    
}
