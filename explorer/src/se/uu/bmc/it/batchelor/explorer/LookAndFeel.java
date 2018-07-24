/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LookAndFeel.java
 *
 * Created: Oct 13, 2009, 2:45:08 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class LookAndFeel {

    /**
     * Get an list of supported Look'n'feel for this platform.
     * @return List of supported styles.
     */
    public static UIManager.LookAndFeelInfo[] getStyleList() {
        return UIManager.getInstalledLookAndFeels();
    }

    /**
     * Set the default Look and Feel for the application. The order in searching
     * for an Look and Feel to use is:
     * 1. User supplied (-Dswing.defaultlaf command line option or from property file)
     * 2. The system Look and Feel.
     * 3. The cross platform Look and Feel.
     */
    public static void setDefaultStyle() {
        String[] styles = new String[3];

        try {
            styles[0] = System.getProperty("swing.defaultlaf");
        } catch (java.security.AccessControlException e) {
            System.err.println(e.getMessage());
        }
        styles[1] = UIManager.getSystemLookAndFeelClassName();
        styles[2] = UIManager.getCrossPlatformLookAndFeelClassName();

        for (int i = 0; i < styles.length; ++i) {
            if (styles[i] != null) {
                try {
                    UIManager.setLookAndFeel(styles[i]);
                    return;
                } catch (ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (InstantiationException e) {
                    System.err.println(e.getMessage());
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                } catch (UnsupportedLookAndFeelException e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < info.length; ++i) {
            System.out.println("Name: " + info[i].getName() + ", Class: " + info[i].getClassName());
        }

        System.out.println("Default: " + System.getProperty("swing.defaultlaf"));
    }
}
