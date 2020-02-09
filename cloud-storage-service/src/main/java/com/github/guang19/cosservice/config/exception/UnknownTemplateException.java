package com.github.guang19.cosservice.config.exception;

/**
 * @author yangguang
 * @date 2020/2/7
 * @description <p>未知的模板异常</p>
 */
public class UnknownTemplateException extends RuntimeException
{
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UnknownTemplateException()
    {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnknownTemplateException(String message)
    {
        super(message);
    }
}
