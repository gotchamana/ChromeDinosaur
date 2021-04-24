package chrome.dinosaur.ecs.component;

import com.badlogic.ashley.core.Component;

import lombok.*;

@Data
public class ScoreComponent implements Component {
    private int score;
}