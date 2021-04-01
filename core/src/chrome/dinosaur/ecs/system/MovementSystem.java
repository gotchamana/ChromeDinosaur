package chrome.dinosaur.ecs.system;

import javax.inject.*;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;

import chrome.dinosaur.ecs.component.*;

public class MovementSystem extends IteratingSystem {

    @Inject
    ComponentMapper<PositionComponent> positioMapper;

    @Inject
    ComponentMapper<VelocityComponent> velocityMapper;

    @Inject
    ComponentMapper<JumpComponent> jumpMapper;

    private float gravity;
    
    @Inject
    public MovementSystem(@Named("movement-system.gravity") float gravity,
        @Named("movement-system.priority") int priority) {

        super(Family.all(PositionComponent.class, VelocityComponent.class).get(), priority);
        this.gravity = gravity;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = positioMapper.get(entity);
        var velocity = velocityMapper.get(entity);

        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());

        var jump = jumpMapper.get(entity);
        if (jump == null) return;

        if (position.getY() <= jump.getOrigPositionY()) {
            entity.remove(JumpComponent.class);
            position.setY(jump.getOrigPositionY());
            velocity.setY(0);
            jump.getOnJumpFinished().run();
        } else {
            velocity.setY(velocity.getY() + gravity);
        }
    }
}