import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ElasticLocation;
import model.MQTTLocation;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
 * Date: 2017-10-20
 */

public class ObjectMapperTest {
    public List<String> list = Arrays.asList(
            "{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":16,\"batt\":61,\"conn\":\"m\",\"lat\":59.4313733,\"lon\":18.3259013,\"tst\":1508256136}",
            "{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":22,\"batt\":50,\"conn\":\"w\",\"lat\":59.434008,\"lon\":18.3240279,\"t\":\"u\",\"tst\":1508266487}",
            "{\"_type\":\"location\",\"tid\":\"nx\",\"acc\":22,\"batt\":50,\"conn\":\"w\",\"lat\":59.4339265,\"lon\":18.3240272,\"tst\":1508266496}"
    );

    @Test
    public void test() throws IOException {
        MQTTLocation l = new ObjectMapper().readValue(list.get(0), MQTTLocation.class);
        ElasticLocation e = ElasticLocation.from(l);
    }
}
