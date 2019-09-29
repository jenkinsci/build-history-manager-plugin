package pl.damianszczepanik.jenkins.buildhistorymanager.model;

import java.io.IOException;

import hudson.model.Job;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class RuleBuilder {

    public static class SampleRule extends Rule {

        public int times;

        public SampleRule() {
            super(null, null);
        }

        public void perform(Job<?, ?> job) throws IOException, InterruptedException {
            times++;
        }
    }
}