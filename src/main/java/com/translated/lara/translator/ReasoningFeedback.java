package com.translated.lara.translator;

import java.util.Collections;
import java.util.List;

public class ReasoningFeedback {
    private boolean applied;
    private IntelligenceError error;
    private List<ReasoningWarning> warnings;

    public boolean getApplied() {
        return applied;
    }

    public IntelligenceError getError() {
        return error;
    }

    public List<ReasoningWarning> getWarnings() {
        return warnings != null ? warnings : Collections.emptyList();
    }
}
