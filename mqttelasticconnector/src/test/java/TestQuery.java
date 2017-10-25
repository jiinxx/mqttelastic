import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Ericsson AB, 2016.
 * <p/>
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 * <p/>
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * <p/>
 * User: eurbmod
 * Date: 2017-10-17
 */
@Ignore
public class TestQuery {

    @Test
    public void test(){
        try {
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")).build();


            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("q", "year:>1900");
            paramMap.put("pretty", "true");

            Response response = restClient.performRequest("GET", "/music/lyrics/_search",
                    paramMap);
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("Host -" + response.getHost());
            System.out.println("RequestLine -" + response.getRequestLine());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        try {
            RestClient sinkClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")).build();
            HttpEntity entity = new NStringEntity("{\"type\":\"location\",\"tid\":\"nx\",\"acc\":16,\"batt\":61,\"conn\":\"m\",\"lat\":59.4313733,\"lon\":18.3259013,\"tst\":1508256136}", ContentType.APPLICATION_JSON);
            Response response = sinkClient.performRequest("PUT", "owntracks", Collections.emptyMap(), entity);
            System.out.println(EntityUtils.toString(response.getEntity()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
