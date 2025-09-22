package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import jenkins.model.BuildDiscarderDescriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.BuildHistoryManager;

/**
 * Descriptor implementation needed to render UI for {@link BuildHistoryManager}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("BuildHistoryManager")
public final class BuildHistoryManagerDescriptor extends BuildDiscarderDescriptor {

    public BuildHistoryManagerDescriptor() {
        super(BuildHistoryManager.class);
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return "Build History Manager";
    }
}
