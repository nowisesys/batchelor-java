/*
 * Web service library for the Batchelor batch job queue.
 * Copyright (C) 2009 by Anders Lövgren and the Computing Department at BMC,
 * Uppsala University.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Send questions, suggestions, bugs or comments to: 
 * Anders Lövgren (lespaul@algonet.se or anders.lovgren@bmc.uu.se)
 * 
 * For more info: http://it.bmc.uu.se/andlov/proj/batchelor/
 */

/*
 * HttpServerResponse.java
 *
 * Created: Apr 19, 2009, 11:17:38 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.util.List;

import se.uu.bmc.it.batchelor.rest.schema.Result;
import se.uu.bmc.it.batchelor.rest.schema.Error;
import se.uu.bmc.it.batchelor.rest.schema.File;
import se.uu.bmc.it.batchelor.rest.schema.Job;
import se.uu.bmc.it.batchelor.rest.schema.Link;

/**
 * Convenient class for the XML Schema class Result.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class HttpServerResponse {

    public enum State {

        Failed, Success
    };

    public enum Type {

        Unknown, ErrorObject, FileObject, JobCollection, LinkCollection, StatusObject, VersionObject
    }

    public enum ErrorOrigin {
        Local, Remote
    }

    /**
     * @param result The result object.
     */
    public HttpServerResponse(Result result) {
        this.result = result;
    }

    /**
     * @param result Set the result object.
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return The result object.
     */
    public Result getResult() {
        return result;
    }

    /**
     * @return True if the result object represent an error object.
     */
    boolean isErrorObject() {
        return result.getError() != null;
    }

    /**
     * @return True if the result object represent a file object.
     */
    boolean isFileObject() {
        return result.getFile() != null;
    }

    /**
     * @return True if the result object represent a job collection.
     */
    boolean isJobCollection() {
        return !result.getJob().isEmpty();
    }

    /**
     * @return True if the result object represent a link collection.
     */
    boolean isLinkCollection() {
        return !result.getLink().isEmpty();
    }

    /**
     * @return True if the result object represent a status object.
     */
    boolean isStatusObject() {
        return result.getStatus() != null;
    }

    /**
     * @return True if the result object represent a remote version object.
     */
    boolean isVersionObject() {
        return result.getVersion() != null;
    }

    /**
     * @return The state of the result object.
     */
    public State getState() {
        if (result.getState().compareTo("success") == 0) {
            return State.Success;
        } else {
            return State.Failed;
        }
    }

    /**
     * @param error The error object.
     * @return The origin of the error object.
     */
    public ErrorOrigin getErrorOrigin(Error error) {
        if(error.getOrigin().compareTo("remote") == 0) {
            return ErrorOrigin.Remote;
        } else {
            return ErrorOrigin.Local;
        }
    }

    /**
     * @return The type of the result object.
     */
    public Type getType() {
        if (result.getError() != null) {
            return Type.ErrorObject;
        } else if (result.getFile() != null) {
            return Type.FileObject;
        } else if (!result.getJob().isEmpty()) {
            return Type.JobCollection;
        } else if (!result.getLink().isEmpty()) {
            return Type.LinkCollection;
        } else if (result.getStatus() != null) {
            return Type.StatusObject;
        } else if (result.getVersion() != null) {
            return Type.VersionObject;
        } else {
            return Type.Unknown;
        }
    }

    @Override
    public String toString() {
        String str = "";
        StringBuilder builder;

        switch (getType()) {
            case ErrorObject:
                Error error = result.getError();
                str += "Error: message=" + error.getMessage() + " (" + error.getCode() + "), origin=" + error.getOrigin();
                break;
            case FileObject:
                File file = result.getFile();
                str += "File: name=" + file.getName() + ", size=" + file.getSize() + ", encoding=" + file.getEncoding();
                break;
            case JobCollection:
                builder = new StringBuilder();
                List<Job> jobs = result.getJob();
                for (Job job : jobs) {
                    builder.append("Job: jobid=" + job.getJobid() + ", result=" + job.getResult() + "\n");
                }
                str += builder.toString();
                break;
            case LinkCollection:
                builder = new StringBuilder();
                List<Link> links = result.getLink();
                for (Link link : links) {
                    builder.append("Link: " + link.getHref());
                    if (link.getGet() != null) {
                        builder.append(", get=" + link.getGet());
                    }
                    if (link.getPost() != null) {
                        builder.append(", post=" + link.getPost());
                    }
                    if (link.getPut() != null) {
                        builder.append(", put=" + link.getPut());
                    }
                    if (link.getDelete() != null) {
                        builder.append(", delete=" + link.getDelete());
                    }
                    builder.append("\n");
                }
                str += builder.toString();
                break;
            case StatusObject:
                str += "Status: " + result.getStatus();
                break;
            case VersionObject:
                str += "Version: " + result.getVersion();
                break;
            default:
                str += "Result: Unknown result type!";
                break;
        }
        return str;
    }
    private Result result;
}
