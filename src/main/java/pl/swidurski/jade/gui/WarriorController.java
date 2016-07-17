package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    private TextField nameField;

    @FXML
    private TextField posXField;

    @FXML
    private TextField posYField;

    @FXML
    private TextField hpField;

    @FXML
    private TextField maxHpField;

    @FXML
    private ProgressBar hpBar;

    @FXML
    private TextField damageField;

    @FXML
    private TextField treasureField;

    @FXML
    private TextField stateField;

    @Getter
    @Setter
    WarriorAgent warriorAgent;


    @Override
    public void init() {
        System.out.println(getWarriorAgent().getLocalName());
        getManager().getStage().setTitle(getWarriorAgent().getLocalName());
        nameField.setText(getWarriorAgent().getLocalName());
        treasureField.setText("0");
    }

    @FXML
    public void saveAction() {
        int x = validatePosition(posXField);
        int y = validatePosition(posYField);
        int hp = validateHP(maxHpField);
        int dmg = validateDMG(damageField);
        if (x < 0 || y < 0 || hp < 0 || dmg < 0)
            return;

        warriorAgent.getInternalState().setPosX(x);
        warriorAgent.getInternalState().setPosY(y);
        warriorAgent.getInternalState().setHp(hp);
        warriorAgent.getInternalState().setMaxHp(hp);
        warriorAgent.getInternalState().setDamage(dmg);

        hpMax = hp;
        updateHp(hp);

        showTooltip(damageField.getParent(), "ZAPISANO!", 50, 50);
        saveButton.setVisible(false);

        nameField.setDisable(true);
        posXField.setDisable(true);
        posYField.setDisable(true);
        hpField.setDisable(true);
        maxHpField.setDisable(true);
        damageField.setDisable(true);
        treasureField.setDisable(true);
        stateField.setDisable(true);

        // Po zapisaniu zarejestruj się w yellow pages
        getWarriorAgent().registerInYellowPages();
    }

    int hpMax;

    public void updateHp(int hp) {
        hpField.setText(String.valueOf(hp));
        double value = hp / (double) hpMax;
        hpBar.setProgress(value);
    }


    private int validateHP(TextField textField) {
        return validate(textField, "Pole życie musi być wypełnione wartością z przediału 0 - ", 1000);
    }

    private int validateDMG(TextField textField) {
        return validate(textField, "Pole obrażenia musi być wypełnione wartością z przediału 0 - ", 200);
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
