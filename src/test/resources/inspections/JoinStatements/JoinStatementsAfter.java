import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class JoinStatements {

    private void joinStatements() {
        List<String> list = new ArrayList<>();
        // the future is always born in pain
        /* tricky */
        assertThat(list).as("foo").hasSize(2)
                /* do another */
                /* do one */.as("bar").contains("barbar")
                // comment to keep
                .doesNotContain("barbara") // another comment to keep
                .doesNotContain("wrzlbrmpft")
                /* and a multi line comment
                    after the statement */
                // across two lines
                .as("etc")/* what a nasty comment */.contains("etcetc")
                // moar!
                .doesNotContain("foobar");

        assertThat("narf").isNotEqualTo("puit").as("bar").contains("barbar").as("foo").hasSize(2);
        assertThat(list).as("evil").extracting(String::length).contains(2);

        assertThat(list).as("bar").contains("barbar");
        assertThat("narf").isNotEqualTo("puit").as("foo").hasSize(2);
        if (true) {
            assertThat(list).doesNotContain("narf").as("bar").contains("barbar");
        }
        assertThat(list.get(0)).isNotEmpty().hasSize(3).isEqualTo("bar");

        assertThat(list.get(0) + "foo").isEqualTo("bar").doesNotStartWith("foo");

        Iterator<String> iterator = list.iterator();
        assertThat(iterator.next()).isEqualTo("foo");
        assertThat(iterator.next()).isEqualTo("bar");
    }
}