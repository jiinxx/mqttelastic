import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

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
public class TestDLS {

    @Test
    public void test(){
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")).build();
        try {
            HttpEntity entity1 = new NStringEntity(
                    "{\n" +
                            "    \"query\" : {\n" +
                            "    \"match\": { \"album\":\"Traditional\"} \n" +
                            "} \n" +
                            "}", ContentType.APPLICATION_JSON);

            Response response = restClient.performRequest("GET", "/music/lyrics/_search", Collections.singletonMap("pretty", "true"),
                    entity1);
            System.out.println(EntityUtils.toString(response.getEntity()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
