
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author viva
 */
public class RobotCommands extends Robot {

    private TextTransfer clipboardObject = new TextTransfer();

    public RobotCommands() throws AWTException {

    }

    public void moveTo(int x, int y) {
        Point point = MouseInfo.getPointerInfo().getLocation();
        double start_x = point.getX();
        double start_y = point.getY();
        for (int i = 0; i < 100; i++) {
            double mov_x = ((x * i) / 100) + (start_x * (100 - i) / 100);
            double mov_y = ((y * i) / 100) + (start_y * (100 - i) / 100);
            mouseMove((int) mov_x, (int) mov_y);
            delay(5);
        }
        delay(530);
    }
    
    public void moveFastTo(int x,int y){
        mouseMove(x, y);
        delay(300);
    }

    private void useCtrl(int keyCode) {
        pressKeyAndAddDelay(KeyEvent.VK_CONTROL, 100);
        pressAndRealseKeyWithDelay(keyCode, 100);
        realseKeyAndAddDelay(KeyEvent.VK_CONTROL, 100);
    }

    public void paste() {
        useCtrl(KeyEvent.VK_V);
    }

    public void copy() {
        useCtrl(KeyEvent.VK_C);
    }

    public void pressEscape() {
        pressAndRealseKeyWithDelay(KeyEvent.VK_ESCAPE, 100);
    }

    public void markText() {
        useCtrl(KeyEvent.VK_A);
    }

    public void pressEnter() {
        pressAndRealseKeyWithDelay(KeyEvent.VK_ENTER, 100);
    }

    public String markAndCopy(int from_x, int y, int to_x) {
        //moveTo(from_x, y);
        mouseMove(from_x, y);
        delay(350);
        mousePress(InputEvent.BUTTON1_MASK);
        mouseMove(to_x, y);
        mouseRelease(InputEvent.BUTTON1_MASK);
        copy();
        return clipboardObject.getClipboardContents();
    }

    private void pressKeyAndAddDelay(int keyKode, int delayTime) {
        keyPress(keyKode);
        delay(delayTime);
    }

    private void realseKeyAndAddDelay(int keyKode, int delayTime) {
        keyRelease(keyKode);
        delay(delayTime);
    }

    protected void pressAndRealseKeyWithDelay(int keyKode, int delayTime) {
        pressKeyAndAddDelay(keyKode, delayTime);
        realseKeyAndAddDelay(keyKode, delayTime);
    }

    protected void pressDown() {
        pressAndRealseKeyWithDelay(40, 10);
    }

    public void moveAndPressLeft(int x, int y) {
        moveTo(x, y);
        pressLeftMouseButton();
    }

    public void pressLeftMouseButton(int delay) {
        mousePress(InputEvent.BUTTON1_MASK);
        delay(delay);
        mouseRelease(InputEvent.BUTTON1_MASK);
        delay(delay);
    }

    private void pressLeftMouseButton() {
        pressLeftMouseButton(300);
    }

    public void type(int i) {
        pressKeyAndAddDelay(i, 15);
    }

    public void type(String s) {
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            int code = b;
            if (code > 96 && code < 123) {
                code = code - 32;
            }
            type(code);
        }
    }

    private void pressUp() {
        pressAndRealseKeyWithDelay(KeyEvent.VK_UP, 10);
    }

    public void moveUp(int i) {
        for (int x = 0; x <= i; x++) {
            pressUp();
        }
    }

    public void moveDown(int i) {
        for (int x = 0; x <= i; x++) {
            pressDown();
        }
    }

}
