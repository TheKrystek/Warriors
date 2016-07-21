package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.WarriorAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WarriorController extends Controller {

    @FXML
    private Button saveButton;

    @FXML
    private TextField posXField;

    @FXML
    private TextField posYField;

    @FXML
    private Slider hpSlider;

    @FXML
    private Label hpLabel;

    @FXML
    private ProgressBar hpBar;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label speedLabel;

    @FXML
    private Slider damageSlider;

    @FXML
    private Label damageLabel;

    @FXML
    private Label treasureLabel;

    @FXML
    private Label nameLabel;

    @Getter
    @Setter
    WarriorAgent warriorAgent;

    int hpMax;

    @Override
    public void init() {
        System.out.println(getWarriorAgent().getLocalName());
        getManager().getStage().setTitle(getWarriorAgent().getLocalName());
        nameLabel.setText(getWarriorAgent().getLocalName());
        treasureLabel.setText("0");

        hpSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hpLabel.setText(String.valueOf(newValue.intValue()));
        });

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            speedLabel.setText(String.valueOf(newValue.intValue()));
        });

        damageSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            damageLabel.setText(String.valueOf(newValue.intValue()));
        });
    }


    @FXML
    public void saveAction() {
        int x = validatePosition(posXField);
        int y = validatePosition(posYField);
        if (x < 0 || y < 0)
            return;

        hpMax = (int) hpSlider.getValue();
        warriorAgent.getInternalState().setPosX(x);
        warriorAgent.getInternalState().setPosY(y);
        warriorAgent.getInternalState().setHp(hpMax);
        warriorAgent.getInternalState().setMaxHp(hpMax);
        warriorAgent.getInternalState().setDamage((int) damageSlider.getValue());
        warriorAgent.getInternalState().setSpeed((int) speedSlider.getValue());
        warriorAgent.getInternalState().setPoints(0);

        updateHp(hpMax);

        showTooltip(nameLabel.getParent(), "ZAPISANO!", 125, 0);
        saveButton.setVisible(false);

        damageSlider.setDisable(true);
        posXField.setDisable(true);
        posYField.setDisable(true);
        speedSlider.setDisable(true);
        hpSlider.setDisable(true);

        // Po zapisaniu zarejestruj się w yellow pages
        getWarriorAgent().registerInYellowPages();
    }

    public void update() {
        posXField.setText(String.valueOf(warriorAgent.getInternalState().getPosX()));
        posYField.setText(String.valueOf(warriorAgent.getInternalState().getPosY()));
        treasureLabel.setText(String.valueOf(warriorAgent.getInternalState().getPoints()));
        updateHp(warriorAgent.getInternalState().getHp());
    }


    public void updateHp(int hp) {
        hpBar.setProgress(hp / (double) hpMax);
    }


    private int validatePosition(TextField textField) {
        return validate(textField, "Pole pozycja musi być wypełnione wartością z przediału 0 - ", 10);
    }

    private int validate(TextField textField, String text, int max) {
        text += max;
        String value = textField.getText();
        if (value == null || value.trim().isEmpty()) {
            showTooltip(textField, text);
            return -1;
        }

        try {
            int i = Integer.parseInt(value);
            if (i < 0 || i > max)
                throw new NumberFormatException();
            return i;
        } catch (NumberFormatException e) {
            showTooltip(textField, text);
            return -1;
        }
    }

    private void showTooltip(Node node, String text, double x, double y) {
        Tooltip t = new Tooltip(text);
        t.setAutoHide(true);
        Point2D point2D = node.localToScreen(x, y);
        t.show(node, point2D.getX(), point2D.getY());
    }

    private void showTooltip(Node node, String text) {
        showTooltip(node, text, 0, 0);
    }

}
