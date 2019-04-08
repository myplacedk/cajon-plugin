import static org.assertj.core.api.Assertions.assertThat;

public class ObjectIsNull {

    private void objectIsNull() {
        assertThat("").isNull();
        assertThat("").as("nah").isNull();
        assertThat(new Object()).isNull();

        assertThat("").isNotNull();
        assertThat("").as("nah").isNotNull();
        assertThat(new Object()).isNotNull();
    }
}