package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.actions;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.Action;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.actions.DeleteLogFileAction;

/**
 * Descriptor implementation needed to render UI for {@link DeleteLogFileAction}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("DeleteLogFile")
public class DeleteLogFileActionDescriptor extends Descriptor<Action> {

    public DeleteLogFileActionDescriptor() {
        super(DeleteLogFileAction.class);
    }

    @Override
    @NonNull
    public String getDisplayName() {
        return "Delete log file";
    }
}
