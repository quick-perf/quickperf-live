import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.quickperf.spring.springboottest.ASpringBootApplication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ASpringBootApplication.class})
@AutoConfigureMockMvc
public class HtmlWithoutSqlTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void get_html_without_sql() throws Exception {
        MvcResult result = mockMvc.perform(get("/html-without-sql"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String actualResponseContent = response.getContentAsString();

        String expectedResponseContent = findExpectedResponseContent();

        assertThat(actualResponseContent).isEqualToNormalizingWhitespace(expectedResponseContent);

    }

    private String findExpectedResponseContent() {
        Path path;
        try {
            path = Paths.get(getClass().getClassLoader()
                        .getResource("html-without-sql-expected.html").toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        try(Stream<String> lines = Files.lines(path)) {
            return lines.collect(Collectors.joining("\n"));
         } catch (IOException e) {
            throw new IllegalStateException(e);
         }
    }

}
