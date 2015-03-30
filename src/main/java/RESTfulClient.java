/**
 * Created by webonise on 30-03-2015.
 */
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
public class RESTfulClient {

    public static void main(String[] args) {

        try {
            Client client = Client.create();
            WebResource resource = client.resource("http://localhost:2015/JAX-RS-Client-Example/rest/customers/100");
            ClientResponse response = resource.accept("application/json").get(ClientResponse.class);

            if(response.getStatus() == 200){

                String output = response.getEntity(String.class);
                System.out.println(output);

            }else System.out.println("Something went wrong..!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}