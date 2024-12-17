package TheGame;

import Models.GameState;
import Models.HighScoreDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Config.Constants.HIGH_SCORE_FILE_PATH;
import static Config.Constants.MAX_NUM_SCORE_BOARD_ENTRY;

public class ScoreBoard {

    public void updateHighScore(GameState gameState) {
        try {
            List<HighScoreDTO> highScores = loadHighScores();
            highScores.add(new HighScoreDTO(gameState.firstPlayerName, gameState.firstPlayerScore));
            highScores.sort((o1, o2) -> Integer.compare(o2.score, o1.score));
            saveHighScores(highScores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<HighScoreDTO> loadHighScores() {
        List<HighScoreDTO> highScores = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(HIGH_SCORE_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    break;
                }
                HighScoreDTO scoreEntry = parseScoreLine(line);
                if (scoreEntry != null) {
                    highScores.add(scoreEntry);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return highScores;
    }

    private HighScoreDTO parseScoreLine(String line) {
        try {
            String[] parts = line.split("\\s+");
            if (parts.length < 2) {
                return null;
            }
            int score = Integer.parseInt(parts[parts.length - 1]);
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                name.append(parts[i]).append(" ");
            }
            return new HighScoreDTO(name.toString().trim(), score);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void saveHighScores(List<HighScoreDTO> highScores) throws IOException {
        List<HighScoreDTO> topScores = highScores.subList(0, Math.min(highScores.size(), MAX_NUM_SCORE_BOARD_ENTRY));
        try (FileWriter writer = new FileWriter(HIGH_SCORE_FILE_PATH)) {
            for (HighScoreDTO score : topScores) {
                writer.write(String.format("%s %d%n", score.name, score.score));
            }
        }
    }
}
