package net.time4j.format.expert;

import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AmbivalentValueException extends ChronoException {
    private static final long serialVersionUID = -4315329288187364457L;

    public AmbivalentValueException(ChronoElement<?> chronoElement) {
        super("Duplicate element parsed with different values: " + chronoElement.name());
    }
}
