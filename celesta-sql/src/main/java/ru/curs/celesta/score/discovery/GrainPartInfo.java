package ru.curs.celesta.score.discovery;

import java.io.File;

//TODO: This class is redundant. GrainPart must be used instead.
public class GrainPartInfo {
    private String grainName;
    private boolean isDefinition;
    private File file;

    public GrainPartInfo(String grainName, boolean isDefinition) {
        this.grainName = grainName;
        this.isDefinition = isDefinition;
    }

    public String getGrainName() {
        return grainName;
    }

    public boolean isDefinition() {
        return isDefinition;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
