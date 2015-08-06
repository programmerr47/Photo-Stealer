package com.github.programmerr47.photostealer.api.parsers;

/**
 * Interface that describes parsing from some {@link Object}
 * to entity of request.
 *
 * @author Michael Spitsin
 * @since 2014-09-13
 */
public interface ResponseParser<ParseResult> {
    ParseResult parseObjectFrom(String objectStr);
}
