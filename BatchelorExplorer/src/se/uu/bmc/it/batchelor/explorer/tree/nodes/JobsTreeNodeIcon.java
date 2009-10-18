/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeNodeIcon.java
 *
 * Created: Oct 16, 2009, 3:08:39 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobsTreeNodeIcon {

    private Icon icon;
    private String file = null;
    private JobsTreeNodeIconType type;

    /**
     * Construct tree node icon object. This constructor loads the icon from the
     * supplied image file path.
     * @param file The node icon image.
     * @param type The node icon type.
     */
    public JobsTreeNodeIcon(String file, JobsTreeNodeIconType type) {
        this.icon = getIcon(file);
        this.file = file;
        this.type = type;
    }

    /**
     * Construct tree node icon object.
     * @param icon The node icon.
     * @param type The node icon type.
     */
    public JobsTreeNodeIcon(Icon icon, JobsTreeNodeIconType type) {
        this.icon = icon;
        this.type = type;
    }

    /**
     * Construct the node icon object.
     * @param icon The node icon.
     * @param file Path to the node icon (informative only).
     * @param type The node cion type.
     */
    public JobsTreeNodeIcon(Icon icon, String file, JobsTreeNodeIconType type) {
        this.icon = icon;
        this.file = file;
        this.type = type;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the type
     */
    public JobsTreeNodeIconType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(JobsTreeNodeIconType type) {
        this.type = type;
    }

    /**
     * Creates the icon object by loading the image from the resource path.
     * @param file The icon image resource path.
     * @return The icon.
     */
    public static Icon getIcon(String file) {
        URL url = JobsTreeNodeIcon.class.getResource(file);
        return new ImageIcon(url);
    }

}
