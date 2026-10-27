package app.gkai.ai.core.common.network;

import retrofit2.Call;
import retrofit2.Response;

@FunctionalInterface
public interface BiFunction<T> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    void apply(Call<T> call, Response<T> response) throws Exception;


}
