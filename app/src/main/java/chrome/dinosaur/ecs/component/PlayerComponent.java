package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;

import lombok.*;

@AllArgsConstructor
@Data
public class PlayerComponent implements Component {
    private Polygon normalShape;
    private Polygon crouchShape;
}