package gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import oscillatorFinder.Permutator;
import state.State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Controller implements Initializable {

    @FXML
    private Canvas simulationCanvas;
    @FXML
    private TextField inputAliveRatio;
    @FXML
    private TextField inputBornIf;
    @FXML
    private TextField inputStableIf;
    @FXML
    private TextField snapshotPath;
    @FXML
    private TextField inputDeadIf;
    @FXML
    private Label errorMessage;
    @FXML
    private Label outGeneration;
    @FXML
    private Label outLivingCells;
    @FXML
    private Label outDeadCells;
    @FXML
    private Pane oscillatorFinderPane;
    @FXML
    private TextField oscMaxWidth;
    @FXML
    private TextField oscMaxHeight;
    @FXML
    private TextField pathToPermut;
    @FXML
    private Label oscErrorMessage;
    @FXML
    private ProgressBar oscProgressBar;
    @FXML
    private Label oscProgressLab;
    @FXML
    private TextField oscPatternsPath;
    @FXML
    private TextField oscMaxGeneration;
    @FXML
    private TextField oscSave;
    @FXML
    private TextField oscBornIf;
    @FXML
    private TextField oscStableIf;
    @FXML
    private TextField oscDeadIf;


    private State simulation;
    private boolean canvasSave = false;
    private String path = "";

    private void showInputErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }

    private boolean isCorrectAR() {
        String tmp = inputAliveRatio.getText();

        if (tmp.isEmpty() || tmp.isBlank())
            return false;

        int ar;
        try {
            ar = Integer.parseInt(tmp);
        } catch (Exception e) {
            showInputErrorMessage("Not a number.");
            return false;
        }

        if (ar > 0 && ar < 102)
            return true;
        else {
            showInputErrorMessage("The ratio should be between 1 and 100");
            return false;
        }
    }

    private boolean isCorrectRule(String rule) {
        if (rule.isEmpty() || rule.isBlank())
            return false;

        String[] ruleArray = rule.split("[\\s\\p{Punct}]+");
        for (String s : ruleArray) {
            if (s.length() != 1)
                return false;
            if (Integer.parseInt(s) > 8 || Integer.parseInt(s) < 0)
                return false;
        }

        return true;
    }

    private boolean isDirectoryExists(String directory) {
        File f = new File(directory);
        return f.exists() && f.isDirectory();
    }

    private int saveAliveRatio() {
        return Integer.parseInt(inputAliveRatio.getText());
    }

    private int[] formatRule(String rule) {
        String[] splited = rule.split("[\\s\\p{Punct}]+");

        int[] ruleArray = new int[splited.length];
        for (int i = 0; i < splited.length; i++)
            ruleArray[i] = Integer.parseInt(splited[i]);

        return ruleArray;
    }

    private int[] saveBornIf() {
        return formatRule(inputBornIf.getText());
    }

    private int[] saveStableIf() {
        return formatRule(inputStableIf.getText());
    }

    private int[] saveDeadIf() {
        return formatRule(inputDeadIf.getText());
    }

    private double[] getCenterCoordinates(int positionX, int positionY) {
        double[] coordinates = new double[2];

        if (positionX % 2 == 0) {
            coordinates[0] = 16 + (positionY - 1) * 9 + 10;
            coordinates[1] = 6 + positionX * 15 + 10;
        } else {
            coordinates[0] = 6 + positionY * 9 + 10;
            coordinates[1] = 16 + (positionX - 1) * 15 + 15;
        }

        return coordinates;
    }

    private double[] getCoordinatesX(double centerCoordinateX) {
        double[] Xs = new double[6];

        for (int i = 0; i < 6; i++)
            Xs[i] = centerCoordinateX + (10 - 0.3 / 2) * sin(i * Math.PI / 3);

        return Xs;
    }

    private double[] getCoordinatesY(double centerCoordinateY) {
        double[] Ys = new double[6];

        for (int i = 0; i < 6; i++)
            Ys[i] = centerCoordinateY + (10 - 0.3 / 2) * cos(i * Math.PI / 3);

        return Ys;
    }

    private void drawHexagon(int positionX, int positionY) {
        GraphicsContext canvasGC = simulationCanvas.getGraphicsContext2D();

        canvasGC.setStroke(Color.WHITE);
        canvasGC.setLineWidth(0.3);

        double[] centerCoordinates = getCenterCoordinates(positionX, positionY);

        double[] Xs = getCoordinatesX(centerCoordinates[0]);
        double[] Ys = getCoordinatesY(centerCoordinates[1]);

        if (simulation.getMap()[positionX][positionY].isAlive())
            canvasGC.setFill(Color.CHOCOLATE);
        else
            canvasGC.setFill(Color.DARKBLUE);

        canvasGC.fillPolygon(Xs, Ys, 6);
        canvasGC.strokePolygon(Xs, Ys, 6);
    }

    private void drawGrid(int rows, int hexagons) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < hexagons; j++)
                if (simulation.getMap()[i][j].isCell())
                    drawHexagon(i, j);
    }

    private boolean isCorrectInput() {
        return isCorrectAR() && isCorrectRule(inputBornIf.getText()) && isCorrectRule(inputStableIf.getText()) && isCorrectRule(inputDeadIf.getText());
    }

    private void initializeSimulation(int aliveRatio, int rows, int columns, int[] bornIf, int[] stableIf, int[] deadIf) {
        simulation = new State(aliveRatio, rows, columns);
        simulation.setBornIf(bornIf);
        simulation.setStableIf(stableIf);
        simulation.setDeadIf(deadIf);
    }

    private Point getMouseLocation() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    private double[] getMouseCoordinates(Point mouseLocation) {
        double[] mouseCoordinates = new double[2];

        mouseCoordinates[0] = mouseLocation.getX() - 21;
        mouseCoordinates[1] = mouseLocation.getY() - 16;

        return mouseCoordinates;
    }

    private int[] getCellCoordinates(double[] mouseCoordinates) {
        int[] cellCoordinates = new int[2];

        for (int i = 0; i < simulation.getMap().length; i++)
            for (int j = 0; j < simulation.getMap()[0].length; j++) {
                double[] center = getCenterCoordinates(i, j);
                if ((center[0] - 10 < mouseCoordinates[0] && center[0] + 10 > mouseCoordinates[0]) &&
                        (center[1] - 10 < mouseCoordinates[1] && center[1] + 10 > mouseCoordinates[1])) {
                    cellCoordinates[0] = i;
                    cellCoordinates[1] = j;
                    return cellCoordinates;
                }
            }

        return cellCoordinates;
    }

    private void saveCanvas() {
        if (canvasSave) {
            SnapshotParameters params = new SnapshotParameters();
            WritableImage tmp = new WritableImage(1430, 1035);
            WritableImage snapshot = simulationCanvas.snapshot(params, tmp);

            File out = new File(path + "/generation" + simulation.getGeneration() + ".png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void plusGeneration(int steps) {
        errorMessage.setVisible(false);

        if (simulation == null)
            showInputErrorMessage("Missing parameters");
        else {
            simulation.refresh(steps);
            refreshCanvas();
            saveCanvas();
            refreshLabels();
        }
    }

    @FXML
    private void changeCanvasSave() {
        canvasSave = !canvasSave;
    }

    @FXML
    private void changeCellStatus() {
        Point mouseLocation = getMouseLocation();

        double[] mouseCoordinates = getMouseCoordinates(mouseLocation);
        int[] cellCoordinates = getCellCoordinates(mouseCoordinates);

        simulation.reverseCellStatus(cellCoordinates[0], cellCoordinates[1]);
        refreshCanvas();
    }

    @FXML
    private void refreshCanvas() {
        drawGrid(simulation.getMap().length, simulation.getMap()[0].length);
    }

    @FXML
    private void refreshLabels() {
        int livingCells = simulation.getLivingCells();
        int deadCells = simulation.getDeadCells();

        outGeneration.setText("" + simulation.getGeneration());
        outLivingCells.setText(livingCells + " (" + 100 * livingCells / simulation.getTOTAL() + "%)");
        outDeadCells.setText("" + deadCells + " (" + 100 * deadCells / simulation.getTOTAL() + "%)");
    }

    @FXML
    private void saveInput() {
        errorMessage.setVisible(false);

        if (isDirectoryExists(snapshotPath.getText()))
            path = snapshotPath.getText();

        if (isCorrectInput()) {
            initializeSimulation(saveAliveRatio(), 68, 78, saveBornIf(), saveStableIf(), saveDeadIf());

            refreshCanvas();
            saveCanvas();
            refreshLabels();
        }
    }

    @FXML
    private void nextStep() {
        plusGeneration(1);
    }

    @FXML
    public void plus50Steps() {
        plusGeneration(50);
    }

    @FXML
    public void plus100Steps() {
        plusGeneration(100);
    }

    @FXML
    public void plus500Steps() {
        plusGeneration(500);
    }

    @FXML
    public void plus1000Steps() {
        plusGeneration(1000);
    }

    @FXML
    public void permutate() {
        showOscErrorMessage(false);

        if (isPermutInputCorrect()) {
            Permutator permutator = new Permutator(Integer.parseInt(oscMaxWidth.getText()), Integer.parseInt(oscMaxHeight.getText()), pathToPermut.getText());
            permutator.permutate();
        } else
            showOscErrorMessage(true);
    }

    @FXML
    private void showOscErrorMessage(boolean visible) {
        oscErrorMessage.setVisible(visible);
    }

    private boolean isPermutInputCorrect() {
        return Integer.parseInt(oscMaxWidth.getText()) < 8 && Integer.parseInt(oscMaxWidth.getText()) > 0 &&
                Integer.parseInt(oscMaxHeight.getText()) < 8 && Integer.parseInt(oscMaxHeight.getText()) > 0 &&
                new File(pathToPermut.getText()).isDirectory();
    }

    @FXML
    public void openOscillatorFinderPane() {
        oscillatorFinderPane.setVisible(true);
    }

    @FXML
    public void closeOscillatorFinderPane() {
        oscillatorFinderPane.setVisible(false);
    }

    @FXML
    public void startFinder() {
        
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        oscillatorFinderPane.setVisible(false);
    }
}
