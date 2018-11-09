package io.kimos.talentpipe.cucumber.stepdefs;

import io.kimos.talentpipe.TalentpipeApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = TalentpipeApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
