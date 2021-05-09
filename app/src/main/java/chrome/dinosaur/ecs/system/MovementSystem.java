package chrome.dinosaur.ecs.system;

import static chrome.dinosaur.config.Config.GRAVITY;

import java.util.Optional;

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

    @Inject
    ComponentMapper<ShapeComponent> shapeMapper;

    @Inject
    public MovementSystem(@Named("movement-system.priority") int priority) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var position = positioMapper.get(entity);
        var velocity = velocityMapper.get(entity);

        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());

        Optional.ofNullable(shapeMapper.get(entity))
            .ifPresent(shapeComponent -> shapeComponent.getShape().setPosition(position.getX(), position.getY()));

        var jump = jumpMapper.get(entity);
        if (jump == null) return;

        if (position.getY() <= jump.getOrigPositionY()) {
            entity.remove(JumpComponent.class);
            position.setY(jump.getOrigPositionY());
            velocity.setY(0);
        } else {
            velocity.setY(velocity.getY() + GRAVITY);
        }
    }
}