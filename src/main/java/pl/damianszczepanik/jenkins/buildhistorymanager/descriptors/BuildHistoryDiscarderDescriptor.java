package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import hudson.Extension;
import jenkins.model.BuildDiscarderDescriptor;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryDiscarder;

/**
 * Descriptor implementation needed to render UI for {@link BuildHistoryDiscarder}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
public final class BuildHistoryDiscarderDescriptor extends BuildDiscarderDescriptor {

    public BuildHistoryDiscarderDescriptor() {
        super(BuildHistoryDiscarder.class);
    }

    @Override
    public String getDisplayName() {
        return "Build History Manager";
    }
}
