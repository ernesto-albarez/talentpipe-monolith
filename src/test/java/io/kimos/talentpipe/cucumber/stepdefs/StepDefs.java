package io.kimos.talentpipe.cucumber.stepdefs;

import io.kimos.talentpipe.MonolithApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = MonolithApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
