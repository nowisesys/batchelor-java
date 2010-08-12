/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PluginType.java
 *
 * Created: Aug 11, 2010, 1:54:34 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.plugin.spi;

/**
 * TODO: add description for the enum type.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public enum PluginType {

    /**
     * This plug-in shows the welcome and introduction page.
     */
    START,
    /**
     * This plug-in handles viewing of file content.
     */
    FILE,
    /**
     * This plug-in provides information when a directory is selected.
     */
    DIRECTORY,
    /**
     * This plug-in can view the result from an finished job.
     */
    RESULT,
    /**
     * This plug-in displays information about the web service connection.
     */
    SERVICE,
}
