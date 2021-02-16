import core.CommentsRemover;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author xiaoxi666
 * @date 2021-02-15 20:10
 */
public class CommentsRemoverTest {

    @Test
    public void givenInput_whenRemoveComments_thenReturnOutput() throws IOException {
        String content_actual = CommentsRemover.doAction(readFileContent("./src/test/java/Input.java"));
        String content_expected = readFileContent("./src/test/resources/Output");
        Assert.assertEquals(content_expected, content_actual);
    }

    private String readFileContent(String fileName) throws IOException {
        BufferedReader br_input = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        String content;
        while((content = br_input.readLine()) != null) {
            sb.append(content);
            sb.append('\n');
        }
        return sb.toString();
    }
}
