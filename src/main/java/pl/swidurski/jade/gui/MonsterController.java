package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.MonsterAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MonsterController extends Controller {

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
    private Slider damageSlider;

    @FXML
    private Label damageLabel;

    @FXML
    private Label nameLabel;

    @Getter
    @Setter
    MonsterAgent monsterAgent;

    int hpMax;

    @Override
    public void init() {
        System.out.println(getMonsterAgent().getLocalName());
        getManager().getStage().setTitle(getMonsterAgent().getLocalName());
        nameLabel.setText(getMonsterAgent().getLocalName());

        hpSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hpLabel.setText(String.valueOf(newValue.intValue()));
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
        monsterAgent.getInternalState().setPosX(x);
        monsterAgent.getInternalState().setPosY(y);
        monsterAgent.getInternalState().setHp(hpMax);
        monsterAgent.getInternalState().setMaxHp(hpMax);
        monsterAgent.getInternalState().setDamage((int) damageSlider.getValue());
        monsterAgent.getInternalState().setPoints(0);

        updateHp(hpMax);

        showTooltip(nameLabel.getParent(), "ZAPISANO!", 125, 0);
        saveButton.setVisible(false);

        damageSlider.setDisable(true);
        posXField.setDisable(true);
        posYField.setDisable(true);
        hpSlider.setDisable(true);

        getMonsterAgent().registerInYellowPages();
    }

    public void update() {
        posXField.setText(String.valueOf(monsterAgent.getInternalState().getPosX()));
        posYField.setText(String.valueOf(monsterAgent.getInternalState().getPosY()));
        updateHp(monsterAgent.getInternalState().getHp());
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
