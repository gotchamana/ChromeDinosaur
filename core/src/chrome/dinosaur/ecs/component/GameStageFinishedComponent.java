package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;

import lombok.*;

@Data
@AllArgsConstructor
public class GameStageFinishedComponent implements Component {
    private Runnable onFinished;
    private boolean finished;
}