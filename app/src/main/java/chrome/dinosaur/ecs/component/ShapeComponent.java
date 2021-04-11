package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;

import lombok.*;

@Data
@AllArgsConstructor
public class ShapeComponent implements Component {
    private Polygon shape;
}