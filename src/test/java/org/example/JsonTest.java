package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javatojsonpojo.MyFirstPojo;
import javatojsonpojo.NewPojo;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsonTest {

    private static final Supplier<ObjectMapper> getObjectMapper = ()->
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final Consumer<String> printValue            = System.out::println;
    private static final ObjectMapper objectMapper              = getObjectMapper.get();
    private static final Supplier<String> locationToFile        = ()-> System.getProperty("user.dir") + "/src/resources/jsontextfiles/testonejson.txt";
    private static final ImplementMePls<File, JsonNode> parse   = objectMapper::readTree;
    private static final Function<String, String> customFile    = (loc)-> System.getProperty("user.dir") + "/src/resources/jsontextfiles/"+loc+".txt";
    private static final ImplementMePls<JsonNode, String> prettyPrint =
            objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT)::writeValueAsString;

    private static <T>T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    private static <O> JsonNode valToTree(O o){
        return objectMapper.valueToTree(o);
    }

    @Test
    void jsonTest() throws IOException {
      var jsonNode = parse.implementMe(new File(locationToFile.get()));
      var s = jsonNode.get("title").asText();
      printValue.accept(s);

    }

    @Test
    void pojotest() throws  IOException{
        var jsonNode = parse.implementMe(new File(locationToFile.get()));
        var s = fromJson(jsonNode, MyFirstPojo.class);
        printValue.accept("POJO TITLE :: " + s.getTitle());
    }

    @Test
    void deserialisation() throws IOException{
        var node  = parse.implementMe(new File(customFile.apply("testtwojson")));
        printValue.accept(node.get("author").asText());
        var s = fromJson(node, MyFirstPojo.class);
        printValue.accept(s.getTitle());
    }

    @Test
    void testThree() throws IOException{

        var newpojo = new NewPojo(); newpojo.author = "J K Lawlings" ; newpojo.title = "Harry Potter";
        var s = valToTree(newpojo);
     //   printValue.accept(s.toString());
        printValue.accept( prettyPrint.implementMe(s));
    }

}

@FunctionalInterface
interface ImplementMePls <I, O>{
    O implementMe(I i) throws IOException;
}




