import javafx.event.Event;
import javafx.event.EventType;

public class CollisionEvent extends Event {
    public static final EventType<CollisionEvent> COLLISION_EVENT_TYPE = new EventType("COLLISION_EVENT_TYPE");
    public static final int COLLISION_TOP = 1;
    public static final int COLLISION_BOTTOM = 2;
    public static final int COLLISION_LEFT = 3;
    public static final int COLLISION_RIGHT = 4;

    private int collisionType;
    public CollisionEvent(int collistionType) {
        super(COLLISION_EVENT_TYPE);
        this.collisionType = collistionType;
    }
    public int getCollisionType(){
        return this.collisionType;
    }
}
