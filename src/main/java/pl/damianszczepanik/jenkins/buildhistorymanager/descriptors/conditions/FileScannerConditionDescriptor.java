package pl.damianszczepanik.jenkins.buildhistorymanager.descriptors.conditions;

import hudson.Extension;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.Condition;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions.FileScannerCondition;

/**
 * Descriptor implementation needed to render UI for {@link FileScannerCondition}.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@Extension
@Symbol("FileScanner")
public class FileScannerConditionDescriptor extends Descriptor<Condition> {

    public FileScannerConditionDescriptor() {
        super(FileScannerCondition.class);
    }

    @Override
    public String getDisplayName() {
        return "File scanner";
    }
}
