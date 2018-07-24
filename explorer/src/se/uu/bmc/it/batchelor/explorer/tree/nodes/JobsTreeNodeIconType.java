/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeNodeIconType.java
 *
 * Created: Oct 16, 2009, 2:43:59 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree.nodes;

/**
 * Enum of all tree node icon types.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public enum JobsTreeNodeIconType {

    /** Web service icon. */
    WEB_SERVICE,
    /** Directory icon. */
    DIRECTORY,
    /** ASCII file icon. */

    FILE_ASCII,
    /** Timestamp file icon. */
    FILE_STAMP,
    /** ZIP/TGZ file icon. */
    FILE_TGZ,
    /** Log file icon. */
    FILE_LOG,
    /** Uploaded data file icon. */
    FILE_INDATA,
    /** Result data file icon. */
    FILE_RESULT,

    /** Job is pending icon. */
    JOB_PENDING,
    /** Job is running icon. */
    JOB_RUNNING,
    /** Job finished with errors icon. */
    JOB_ERROR,
    /** Job has crashed icon. */
    JOB_CRASHED,
    /** Job has finished with warnings icon. */
    JOB_WARNING,
    /** Job has finished successful. */
    JOB_SUCCESS
        
}
