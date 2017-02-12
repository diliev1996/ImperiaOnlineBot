
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author viva
 */
public class Bot extends RobotCommands {

    private ArrayList<Method> priorities = new ArrayList<>();
    private static final int MINUTE = 60000;
    public volatile boolean running = true;
    private double totalDuration;
    private int minutes;
    private Thread thread;
    int spy_from_x = 1136;
    int spy_y = 360;
    int spy_to_x = spy_from_x + 60;
    int counter;

    public Bot() throws AWTException {
    }

    public Bot(double totalDuration, int minutes) throws AWTException {
        this.totalDuration = totalDuration;
        this.minutes = minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setTotalDuration(double totalDuration) {
        this.totalDuration = totalDuration;
    }

    private void checkBankWithShortcut() {
        pressEscape();
        delay(550);
        // moveAndPressLeft(728, 515); //moves to bank
        pressAndRealseKeyWithDelay(KeyEvent.VK_D, 10); // opens bank
        moveAndPressLeft(770, 340); // opens deposit menu
        if (markAndCopy(1071, 657, 1135).contains("00:00:00")) {
            withdrawDeposit();
            makeNewDeposit();
        } else {
            System.out.print("It is not ready");
        }
        pressEscape();
    }

    private void withdrawDeposit() {
        moveAndPressLeft(1215, 662);
    }

    private void makeNewDeposit() {
        delay(3300);
        moveAndPressLeft(887, 644); // moves to the sum
        moveAndPressLeft(1020, 644); // presses the button
    }

    private void spyAll(int y) {
        moveAndPressLeft(1210, y);
        delay(660);
    }

    private void pressAttackButton(int x, int y, int numberOfAttacks, String goldAmount) {
        if (numberOfAttacks <= 0) {
            return;
        }
        moveAndPressLeft(x, y); // presses attackButton
        delay(500);
        selectLightSwordmen(numberOfAttacks);
        selectHeavyArchers(numberOfAttacks);
        selectHeavyCalvary(numberOfAttacks);
        moveAndPressLeft(970, 700);
        moveDown(4);
        moveAndPressLeft(960, 871);
        delay(1250);
        pressEscape();
        delay(1000);
        checkGold(spy_from_x, spyNext(), spy_to_x, goldAmount, numberOfAttacks - 1, counter);
    }

    private int spyNext() {
        return spy_y = spy_y + 33;
    }

    private void selectLightSwordmen(int numberOfAttacks) {
        selectUnits(numberOfAttacks, 790, 502, 861);
    }

    private void selectUnits(int numberOfAttacks, int x, int y, int to_x) {
        int units = Integer.parseInt(markAndCopy(x, y, to_x).replaceAll(" ", "").trim());
        moveAndPressLeft(x, y + 36);
        type(units / numberOfAttacks + "");
    }

    private void selectHeavyArchers(int numberOfAttacks) {
        selectUnits(numberOfAttacks, 970, 440, 1035);
    }

    private void selectHeavyCalvary(int numberOfAttacks) {
        selectUnits(numberOfAttacks, 970, 570, 1035);
    }

    private boolean tryBuyingResourse(double desiredPrice) {
        double actualPrice = Double.parseDouble(markAndCopy(1007, 896, 1047).replaceAll(" ", ""));
        if (actualPrice <= desiredPrice) {
            markAndCopy(843, 897, 897); //copies amount to be bought
            moveAndPressLeft(958, 674);
            paste();
            pressEnter();
            return true;
        }
        return false;
    }

    public void buyResources(double price) {
        pressAndRealseKeyWithDelay(KeyEvent.VK_M, 15);
        // moveAndPressLeft(391, 409); // opens market
        int y = 503;
        moveAndPressLeft(857, y); // mooves to wood
        if (tryBuyingResourse(price)) { //tries to buy wood
            moveAndPressLeft(958, y + 80); //moves to iron
        } else {
            moveAndPressLeft(958, y);
        }
        if (tryBuyingResourse(price)) { // tries to buy iron
            moveAndPressLeft(1058, y + 80); //moves to stone
        } else {
            moveAndPressLeft(1058, y);
        }
        tryBuyingResourse(price); // tries to buy stone       
    }

    private void markOneLine(int x, int level, int y, String goldAmount, int numberOfAttacks, int attackCounter) {
        if (y >= 1000 || y <= 200) {
            return;
        }
        moveTo(x, y);
        delay(22);
        String copied = markAndCopy(x, y, x + 210);
        spy(copied, level, y, goldAmount, numberOfAttacks, x, y, attackCounter);
    }

    /**
     * Does not work yet
     *
     * @throws InterruptedException
     */
    private void checkIfAttacked() throws InterruptedException {
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        if (getPixelColor(1820, 178).equals(new Color(254,103,51))) {
            while(true){
                Thread.sleep(1000);
                toolKit.beep();
            }
        }else{
            System.out.println("Not under attack");
        }
        System.out.println(getPixelColor(1820, 178));
    }



    private void spy(String copied, int level, int current_y, String goldAmount, int numberOfAttacks, int x, int y, int attackCounter) {
        if (copied.contains("Independent") && copied.contains("Level " + level)) {
            spyAll(current_y);
            delay(400);
            checkGold(spy_from_x, spy_y, spy_to_x, goldAmount, numberOfAttacks, attackCounter);
        } else {
            markOneLine(x, level, y + 33, goldAmount, numberOfAttacks, attackCounter);
        }
    }

    private void checkGold(int from_x, int y, int to_x, String goldAmount, int numberOfAttacks, int attackCounter) {
        if (numberOfAttacks <= 0 && y > 1000) {
            return;
        }
        //  moveUp(4);
        if (markAndCopy(from_x, y, to_x).equals(goldAmount)) {
            if (attackCounter == counter) {
                pressAttackButton(to_x + 122, y, numberOfAttacks, goldAmount);
            } else {
                counter++;
                checkGold(from_x, spyNext(), to_x, goldAmount, numberOfAttacks, attackCounter);
            }
        } else {
            checkGold(from_x, spyNext(), to_x, goldAmount, numberOfAttacks, attackCounter);
        }
    }

    public void use10Minutes() {
        pressEscape();
        //moveAndPressLeft(1132, 570);
        pressAndRealseKeyWithDelay(KeyEvent.VK_U, 10);
        delay(1750);
        for (int i = 0; i < 30; i++) {
            mouseMove(1011, 250 - i);
            pressLeftMouseButton(10);
        }
        pressEscape();
    }

    public void openImperia() {
        delay(300);
        keyPress(KeyEvent.VK_I);
        keyRelease(KeyEvent.VK_I);
        pressEnter();
    }

    public void login() {
        moveAndPressLeft(1200, 500);
        moveAndPressLeft(700, 650);
    }

    public void openHireMenu() {
        delay(3300);
        moveAndPressLeft(1880, 828);
    }

    public void optimalIncome() {
        moveAndPressLeft(951, 348);
    }

    public void hireWorkersOnWood() {
        moveAndPressLeft(706, 450);
    }

    public void hireWorkersOnIron() {
        moveAndPressLeft(906, 450);
    }

    public void hireWorkersOnStone() {
        moveAndPressLeft(1100, 450);
    }

    public ArrayList<Method> getPriorities() {
        return priorities;
    }

    public void refreshPage() {
        moveAndPressLeft(72, 38);
        delay(1500);
        if (getPixelColor(1000, 500).equals(Color.WHITE) || getPixelColor(1000, 500).equals(Color.BLACK)) {
            delay(1500);
        }
    }

    public void executePriorities() {
        for (Method m : priorities) {
            try {
                m.invoke(this);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                break;
            }
        }
        pressEscape();
    }

    private void URL() {
        moveAndPressLeft(400, 42);
        markText(); // marks the URl
    }

    private void openChrome() {
        moveAndPressLeft(600, 1100);
    }

    private void goToCapital() {
        delay(1000);
        moveAndPressLeft(28, 484);
    }

    private void openFortress() {
        delay(300);
        pressAndRealseKeyWithDelay(KeyEvent.VK_C, 10);
        // moveAndPressLeft(1281, 919);
        delay(500);
    }

    private void openEspionage() {
        moveAndPressLeft(802, 208);
        delay(700);
    }

    private void goToInternet(boolean logged) {
        openChrome();
        if (!logged) {
            URL();
            openImperia();
            login();
        } else {
            refreshPage();
            // goToCapital();
        }
    }

    public void stopTimer(Timer timer, TimerTask timerTask) throws InterruptedException {
        thread.wait(minutes * MINUTE);
        timer.cancel();
        delay(500);
        timerTask.cancel();
        thread.wait(5000);
    }

    public boolean stop() {
        thread.interrupt();
        System.exit(0);
        return running = false;
    }

    public void start(boolean logged, double price, boolean buying,boolean checkIfAttacked) {
        thread = new Thread(() -> {
            goToInternet(logged);
            Timer timer = new Timer();
           TimerTask timerTask  = new TimerTask() {
                @Override
                public void run() {
                    buyResources(price);
                }
            };
            while (running && totalDuration > 0) {
                try {
                    delay(1250);
                    if(checkIfAttacked){
                     checkIfAttacked(); 
                    }
                    openHireMenu();
                    executePriorities();
                    use10Minutes();
                    checkBankWithShortcut();
                    // attackIndependentCities(); there is another menu now
                    if (buying) {                              
                        timer.scheduleAtFixedRate(timerTask, 1000, 10000);
                        stopTimer(timer, timerTask);
                    }
                } catch (InterruptedException ex) {
                }
                totalDuration = totalDuration - minutes;
                //   refreshPage();
            }
        });
        thread.start();
    }

    public void startAttacks(boolean logged, int level, String goldAmount, int numberOfAttacks, String from, boolean addHeavySwordsmen, int attackCounter) {
        thread = new Thread(() -> {
            goToInternet(logged);
            delay(1500);
            openFortress();
            openEspionage();
            markOneLine(581, level, 420, goldAmount, numberOfAttacks, attackCounter);
        });
        thread.start();
    }

}
