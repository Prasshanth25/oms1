package net.time4j.history;

import com.facebook.hermes.intl.Constants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.time4j.PlainDate;
import net.time4j.engine.CalendarDate;
import net.time4j.engine.EpochDays;

/* loaded from: classes2.dex */
public final class EraPreference {
    private static final int NON_DEFAULT_MARKER = 127;
    private final PlainDate end;
    private final HistoricEra era;
    private final PlainDate start;
    static final EraPreference DEFAULT = new EraPreference();
    private static final HistoricDate AD1 = HistoricDate.of(HistoricEra.AD, 1, 1, 1);
    private static final HistoricDate BC38 = HistoricDate.of(HistoricEra.BC, 38, 1, 1);
    private static final PlainDate PROTOTYPE = PlainDate.of(2000, 1);

    private EraPreference() {
        this.era = null;
        this.start = PlainDate.axis().getMinimum();
        this.end = PlainDate.axis().getMaximum();
    }

    private EraPreference(HistoricEra historicEra, PlainDate plainDate, PlainDate plainDate2) {
        if (historicEra.compareTo(HistoricEra.AD) <= 0) {
            throw new UnsupportedOperationException(historicEra.name());
        }
        if (plainDate2.isBefore((CalendarDate) plainDate)) {
            throw new IllegalArgumentException("End before start: " + plainDate + "/" + plainDate2);
        }
        this.era = historicEra;
        this.start = plainDate;
        this.end = plainDate2;
    }

    public static EraPreference hispanicUntil(PlainDate plainDate) {
        return hispanicBetween(PlainDate.axis().getMinimum(), plainDate);
    }

    public static EraPreference hispanicBetween(PlainDate plainDate, PlainDate plainDate2) {
        return new EraPreference(HistoricEra.HISPANIC, plainDate, plainDate2);
    }

    public static EraPreference byzantineUntil(PlainDate plainDate) {
        return byzantineBetween(PlainDate.axis().getMinimum(), plainDate);
    }

    public static EraPreference byzantineBetween(PlainDate plainDate, PlainDate plainDate2) {
        return new EraPreference(HistoricEra.BYZANTINE, plainDate, plainDate2);
    }

    public static EraPreference abUrbeCondita() {
        return abUrbeConditaUntil(PlainDate.axis().getMaximum());
    }

    public static EraPreference abUrbeConditaUntil(PlainDate plainDate) {
        return abUrbeConditaBetween(PlainDate.axis().getMinimum(), plainDate);
    }

    public static EraPreference abUrbeConditaBetween(PlainDate plainDate, PlainDate plainDate2) {
        return new EraPreference(HistoricEra.AB_URBE_CONDITA, plainDate, plainDate2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof EraPreference) {
            EraPreference eraPreference = (EraPreference) obj;
            EraPreference eraPreference2 = DEFAULT;
            return this == eraPreference2 ? eraPreference == eraPreference2 : this.era == eraPreference.era && this.start.equals(eraPreference.start) && this.end.equals(eraPreference.end);
        }
        return false;
    }

    public int hashCode() {
        return (this.era.hashCode() * 17) + (this.start.hashCode() * 31) + (this.end.hashCode() * 37);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (this == DEFAULT) {
            sb.append(Constants.COLLATION_DEFAULT);
        } else {
            sb.append("era->");
            sb.append(this.era);
            sb.append(",start->");
            sb.append(this.start);
            sb.append(",end->");
            sb.append(this.end);
        }
        sb.append(']');
        return sb.toString();
    }

    public HistoricEra getPreferredEra(HistoricDate historicDate, PlainDate plainDate) {
        if (this.era == null || plainDate.isBefore((CalendarDate) this.start) || plainDate.isAfter((CalendarDate) this.end)) {
            return historicDate.compareTo(AD1) < 0 ? HistoricEra.BC : HistoricEra.AD;
        } else if (this.era == HistoricEra.HISPANIC && historicDate.compareTo(BC38) < 0) {
            return HistoricEra.BC;
        } else {
            return this.era;
        }
    }

    public void writeToStream(DataOutput dataOutput) throws IOException {
        if (this == DEFAULT) {
            dataOutput.writeByte(0);
            return;
        }
        dataOutput.writeByte(127);
        dataOutput.writeUTF(this.era.name());
        dataOutput.writeLong(((Long) this.start.get(EpochDays.MODIFIED_JULIAN_DATE)).longValue());
        dataOutput.writeLong(((Long) this.end.get(EpochDays.MODIFIED_JULIAN_DATE)).longValue());
    }

    public static EraPreference readFromStream(DataInput dataInput) throws IOException {
        if (dataInput.readByte() == Byte.MAX_VALUE) {
            HistoricEra valueOf = HistoricEra.valueOf(dataInput.readUTF());
            long readLong = dataInput.readLong();
            long readLong2 = dataInput.readLong();
            PlainDate plainDate = PROTOTYPE;
            return new EraPreference(valueOf, (PlainDate) plainDate.with(EpochDays.MODIFIED_JULIAN_DATE, readLong), (PlainDate) plainDate.with(EpochDays.MODIFIED_JULIAN_DATE, readLong2));
        }
        return DEFAULT;
    }
}
