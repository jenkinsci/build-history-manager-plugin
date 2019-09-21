package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import jenkins.model.BuildDiscarderDescriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryDiscarder;


@Extension
public final class BuildHistoryDiscarderDescriptor extends BuildDiscarderDescriptor {

    public BuildHistoryDiscarderDescriptor() {
        super(BuildHistoryDiscarder.class);
    }

    public String getDisplayName() {
        return "Build History Manager";
    }
}
