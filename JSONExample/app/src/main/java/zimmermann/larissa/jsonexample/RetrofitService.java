package zimmermann.larissa.jsonexample;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by laris on 25/07/2017.
 */

public interface RetrofitService {
    @Headers("X-Mashape-Key: AuuyclCPjcmshv2iOPq190OpzLrMp1FJWwejsnJrdfwOUr4h44")

    @FormUrlEncoded
    @POST("convert")
    Call<ServerResponse> converterUnidade(@Field("from-type") String from_type,     //Unidade original
                                          @Field("from-value") String from_value,   //Valor
                                          @Field("to-type") String to_type);        //Unidade final
}
