package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStateFinishedComponent implements Component {
    private Runnable onFinished;
    private boolean finished;
}