package io.sphere.sdk.states.commands;

import io.sphere.sdk.states.State;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Before;
import org.junit.Test;

import static io.sphere.sdk.states.StateFixtures.cleanUpByKey;
import static io.sphere.sdk.states.StateFixtures.createStateByKey;

public class StateDeleteCommandTest extends IntegrationTest {

    public static final String KEY = StateDeleteCommandTest.class.getSimpleName();

    @Before
    public void setUp() throws Exception {
        cleanUpByKey(client(), KEY);

    }

    @Test
    public void execution() throws Exception {
        final State state = getState();
        final State deletedState = execute(StateDeleteCommand.of(state));
    }

    private State getState() {
        return createStateByKey(client(), KEY);
    }
}