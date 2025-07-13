package pl.damianszczepanik.jenkins.buildhistorymanager.model.conditions;

import hudson.model.Run;
import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import pl.damianszczepanik.jenkins.buildhistorymanager.model.RuleConfiguration;

/**
 * Evaluates if file exists in build workspace.
 * This implementation uses <a href="https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html">DirectoryScanner</a>.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FileScannerCondition extends Condition {

    private String includePattern = "";
    private String excludePattern = "";
    private boolean caseSensitive = true;

    @DataBoundConstructor
    public FileScannerCondition() {
        // Jenkins stapler requires to have public constructor with @DataBoundConstructor
    }

    public String getIncludePattern() {
        return includePattern;
    }

    @DataBoundSetter
    public void setIncludePattern(String includePattern) {
        this.includePattern = includePattern;
    }

    public String getExcludePattern() {
        return excludePattern;
    }

    @DataBoundSetter
    public void setExcludePattern(String excludePattern) {
        this.excludePattern = excludePattern;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @DataBoundSetter
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(Run<?, ?> run, RuleConfiguration configuration) {
        String[] includes = {includePattern};
        String[] excludes = {excludePattern};
        DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(includes);
        ds.setExcludes(excludes);
        ds.setCaseSensitive(caseSensitive);
        ds.setBasedir(run.getRootDir());
        ds.scan();

        return ds.getIncludedFilesCount() > 0;
    }
}
