package handler;

import utils.Attachment;

import java.nio.channels.CompletionHandler;

/**
 * A general handler for socket to use. You can use this class to implement you custom handler.
 */
public abstract class GeneralHandler implements CompletionHandler<Integer, Attachment> {
}
