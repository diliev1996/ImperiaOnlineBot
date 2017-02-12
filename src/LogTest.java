
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author viva
 */
public class LogTest {
    public static void main(String[] args) throws IOException {  

    Logger logger = Logger.getLogger("MyLog");  
    FileHandler fh;  

    try {  

        // This block configure the logger with handler and formatter  
        fh = new FileHandler("D:\\Rarely use\\Снимки\\MyLog" ,true);
     
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        // the following statement is used to log any messages  
        logger.info("My first log");  

    } catch (SecurityException | IOException e) {  
        e.printStackTrace();  
    }  

    logger.info("Hi How r u?");  

}
}
