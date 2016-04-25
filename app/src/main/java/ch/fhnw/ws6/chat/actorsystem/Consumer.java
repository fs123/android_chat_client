package ch.fhnw.ws6.chat.actorsystem;

/**
 * Created by fs on 25.04.16.
 */
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);
}
