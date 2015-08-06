package com.github.programmerr47.photostealer.api;

/**
 * Common interface for all supported methods for this application.
 * If new methods will be available or needed to be written,
 * they must implement this interface or one of its inheritors.
 *
 * @author Michael Spitsin
 * @since 2014-10-20
 */
public interface ApiMethod<Response> {

    /**
     * Starts executing method described by certain inheritor.
     *
     * @return result of certain method. It can be some response object (for GET methods),
     * or it can be just null object (for some POST methods or e.t.c)
     */
    @SuppressWarnings("unused")
    Response execute() throws Exception;
}
