package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VelocityComponent implements Component {
    private float x;
    private float y;
}