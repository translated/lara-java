package com.translated.lara.translator;

import java.util.Objects;

public class GlossaryExport {

    private final String jobId;

    public GlossaryExport(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GlossaryExport)) return false;
        GlossaryExport that = (GlossaryExport) object;
        return Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jobId);
    }

    @Override
    public String toString() {
        return "GlossaryExport{jobId='" + jobId + "'}";
    }

}
