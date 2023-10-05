package com.facebook.react.common.mapbuffer;

import com.facebook.jni.HybridData;
import com.facebook.react.common.mapbuffer.MapBuffer;
import com.facebook.react.uimanager.ViewProps;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.UShort;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.Charsets;

/* compiled from: ReadableMapBuffer.kt */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010(\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 ?2\u00020\u0001:\u0002?@B\u000f\b\u0013\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u000f\b\u0012\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\tH\u0016J\u0013\u0010\u0016\u001a\u00020\u00112\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0096\u0002J\u0010\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010\u001a\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\tH\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010\u001e\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\tH\u0002J\u0010\u0010\"\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0016\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00000$2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010%\u001a\u00020&2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0010\u0010'\u001a\u00020(2\u0006\u0010\u0012\u001a\u00020\tH\u0016J\u0018\u0010)\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010*\u001a\u00020(H\u0002J\b\u0010+\u001a\u00020\tH\u0016J\t\u0010,\u001a\u00020\u0006H\u0082 J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00140.H\u0096\u0002J\u0010\u0010/\u001a\u00020\u00112\u0006\u00100\u001a\u00020\tH\u0002J\u0010\u00101\u001a\u00020(2\u0006\u0010!\u001a\u00020\tH\u0002J\u0010\u00102\u001a\u00020\u001d2\u0006\u00100\u001a\u00020\tH\u0002J\b\u00103\u001a\u000204H\u0002J\u0010\u00105\u001a\u00020\t2\u0006\u00100\u001a\u00020\tH\u0002J\u0016\u00106\u001a\b\u0012\u0004\u0012\u00020\u00000$2\u0006\u00107\u001a\u00020\tH\u0002J\u0010\u00108\u001a\u00020\u00002\u0006\u00107\u001a\u00020\tH\u0002J\u0010\u00109\u001a\u00020&2\u0006\u00100\u001a\u00020\tH\u0002J \u0010:\u001a\u00020;2\u0006\u00100\u001a\u00020\tH\u0002ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b<\u0010=J\b\u0010>\u001a\u00020&H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0012\u0010\r\u001a\u0004\u0018\u00010\u00038\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\f\u0082\u0002\u000f\n\u0002\b!\n\u0005\b¡\u001e0\u0001\n\u0002\b\u0019¨\u0006A"}, d2 = {"Lcom/facebook/react/common/mapbuffer/ReadableMapBuffer;", "Lcom/facebook/react/common/mapbuffer/MapBuffer;", "hybridData", "Lcom/facebook/jni/HybridData;", "(Lcom/facebook/jni/HybridData;)V", "buffer", "Ljava/nio/ByteBuffer;", "(Ljava/nio/ByteBuffer;)V", "<set-?>", "", "count", "getCount", "()I", "mHybridData", "offsetForDynamicData", "getOffsetForDynamicData", "contains", "", "key", "entryAt", "Lcom/facebook/react/common/mapbuffer/MapBuffer$Entry;", "offset", "equals", "other", "", "getBoolean", "getBucketIndexForKey", "intKey", "getDouble", "", "getInt", "getKeyOffset", "getKeyOffsetForBucketIndex", "bucketIndex", "getMapBuffer", "getMapBufferList", "", "getString", "", "getType", "Lcom/facebook/react/common/mapbuffer/MapBuffer$DataType;", "getTypedValueOffsetForKey", "expected", "hashCode", "importByteBuffer", "iterator", "", "readBooleanValue", "bufferPosition", "readDataType", "readDoubleValue", "readHeader", "", "readIntValue", "readMapBufferListValue", ViewProps.POSITION, "readMapBufferValue", "readStringValue", "readUnsignedShort", "Lkotlin/UShort;", "readUnsignedShort-BwKQO78", "(I)S", "toString", "Companion", "MapBufferEntry", "ReactAndroid_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes.dex */
public final class ReadableMapBuffer implements MapBuffer {
    private static final int ALIGNMENT = 254;
    private static final int BUCKET_SIZE = 12;
    public static final Companion Companion = new Companion(null);
    private static final int HEADER_SIZE = 8;
    private static final int TYPE_OFFSET = 2;
    private static final int VALUE_OFFSET = 4;
    private final ByteBuffer buffer;
    private int count;
    private final HybridData mHybridData;

    /* compiled from: ReadableMapBuffer.kt */
    @Metadata(k = 3, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[MapBuffer.DataType.values().length];
            try {
                iArr[MapBuffer.DataType.BOOL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[MapBuffer.DataType.INT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[MapBuffer.DataType.DOUBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[MapBuffer.DataType.STRING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[MapBuffer.DataType.MAP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public final int getKeyOffsetForBucketIndex(int i) {
        return (i * 12) + 8;
    }

    private final native ByteBuffer importByteBuffer();

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public int getCount() {
        return this.count;
    }

    private ReadableMapBuffer(HybridData hybridData) {
        this.mHybridData = hybridData;
        this.buffer = importByteBuffer();
        readHeader();
    }

    private ReadableMapBuffer(ByteBuffer byteBuffer) {
        this.mHybridData = null;
        this.buffer = byteBuffer;
        readHeader();
    }

    private final void readHeader() {
        if (this.buffer.getShort() != ALIGNMENT) {
            this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        this.count = m82readUnsignedShortBwKQO78(this.buffer.position()) & UShort.MAX_VALUE;
    }

    private final int getOffsetForDynamicData() {
        return getKeyOffsetForBucketIndex(getCount());
    }

    private final int getBucketIndexForKey(int i) {
        IntRange kEY_RANGE$ReactAndroid_release = MapBuffer.Companion.getKEY_RANGE$ReactAndroid_release();
        int i2 = 0;
        if (i <= kEY_RANGE$ReactAndroid_release.getLast() && kEY_RANGE$ReactAndroid_release.getFirst() <= i) {
            short m721constructorimpl = UShort.m721constructorimpl((short) i);
            int count = getCount() - 1;
            while (i2 <= count) {
                int i3 = (i2 + count) >>> 1;
                int m82readUnsignedShortBwKQO78 = m82readUnsignedShortBwKQO78(getKeyOffsetForBucketIndex(i3)) & UShort.MAX_VALUE;
                int i4 = 65535 & m721constructorimpl;
                if (Intrinsics.compare(m82readUnsignedShortBwKQO78, i4) < 0) {
                    i2 = i3 + 1;
                } else if (Intrinsics.compare(m82readUnsignedShortBwKQO78, i4) <= 0) {
                    return i3;
                } else {
                    count = i3 - 1;
                }
            }
            return -1;
        }
        return -1;
    }

    private final MapBuffer.DataType readDataType(int i) {
        return MapBuffer.DataType.values()[m82readUnsignedShortBwKQO78(getKeyOffsetForBucketIndex(i) + 2) & UShort.MAX_VALUE];
    }

    private final int getTypedValueOffsetForKey(int i, MapBuffer.DataType dataType) {
        int bucketIndexForKey = getBucketIndexForKey(i);
        if (!(bucketIndexForKey != -1)) {
            throw new IllegalArgumentException(("Key not found: " + i).toString());
        }
        MapBuffer.DataType readDataType = readDataType(bucketIndexForKey);
        if (!(readDataType == dataType)) {
            throw new IllegalStateException(("Expected " + dataType + " for key: " + i + ", found " + readDataType + " instead.").toString());
        }
        return getKeyOffsetForBucketIndex(bucketIndexForKey) + 4;
    }

    /* renamed from: readUnsignedShort-BwKQO78 */
    public final short m82readUnsignedShortBwKQO78(int i) {
        return UShort.m721constructorimpl(this.buffer.getShort(i));
    }

    public final double readDoubleValue(int i) {
        return this.buffer.getDouble(i);
    }

    public final int readIntValue(int i) {
        return this.buffer.getInt(i);
    }

    public final boolean readBooleanValue(int i) {
        return readIntValue(i) == 1;
    }

    public final String readStringValue(int i) {
        int offsetForDynamicData = getOffsetForDynamicData() + this.buffer.getInt(i);
        int i2 = this.buffer.getInt(offsetForDynamicData);
        byte[] bArr = new byte[i2];
        this.buffer.position(offsetForDynamicData + 4);
        this.buffer.get(bArr, 0, i2);
        return new String(bArr, Charsets.UTF_8);
    }

    public final ReadableMapBuffer readMapBufferValue(int i) {
        int offsetForDynamicData = getOffsetForDynamicData() + this.buffer.getInt(i);
        int i2 = this.buffer.getInt(offsetForDynamicData);
        byte[] bArr = new byte[i2];
        this.buffer.position(offsetForDynamicData + 4);
        this.buffer.get(bArr, 0, i2);
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        Intrinsics.checkNotNullExpressionValue(wrap, "wrap(newBuffer)");
        return new ReadableMapBuffer(wrap);
    }

    private final List<ReadableMapBuffer> readMapBufferListValue(int i) {
        ArrayList arrayList = new ArrayList();
        int offsetForDynamicData = getOffsetForDynamicData() + this.buffer.getInt(i);
        int i2 = this.buffer.getInt(offsetForDynamicData);
        int i3 = offsetForDynamicData + 4;
        int i4 = 0;
        while (i4 < i2) {
            int i5 = this.buffer.getInt(i3 + i4);
            byte[] bArr = new byte[i5];
            int i6 = i4 + 4;
            this.buffer.position(i3 + i6);
            this.buffer.get(bArr, 0, i5);
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            Intrinsics.checkNotNullExpressionValue(wrap, "wrap(newMapBuffer)");
            arrayList.add(new ReadableMapBuffer(wrap));
            i4 = i6 + i5;
        }
        return arrayList;
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public boolean contains(int i) {
        return getBucketIndexForKey(i) != -1;
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public int getKeyOffset(int i) {
        return getBucketIndexForKey(i);
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public MapBuffer.Entry entryAt(int i) {
        return new MapBufferEntry(getKeyOffsetForBucketIndex(i));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public MapBuffer.DataType getType(int i) {
        int bucketIndexForKey = getBucketIndexForKey(i);
        if (!(bucketIndexForKey != -1)) {
            throw new IllegalArgumentException(("Key not found: " + i).toString());
        }
        return readDataType(bucketIndexForKey);
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public int getInt(int i) {
        return readIntValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.INT));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public double getDouble(int i) {
        return readDoubleValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.DOUBLE));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public String getString(int i) {
        return readStringValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.STRING));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public boolean getBoolean(int i) {
        return readBooleanValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.BOOL));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public ReadableMapBuffer getMapBuffer(int i) {
        return readMapBufferValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.MAP));
    }

    @Override // com.facebook.react.common.mapbuffer.MapBuffer
    public List<ReadableMapBuffer> getMapBufferList(int i) {
        return readMapBufferListValue(getTypedValueOffsetForKey(i, MapBuffer.DataType.MAP));
    }

    public int hashCode() {
        this.buffer.rewind();
        return this.buffer.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReadableMapBuffer) {
            ByteBuffer byteBuffer = this.buffer;
            ByteBuffer byteBuffer2 = ((ReadableMapBuffer) obj).buffer;
            if (byteBuffer == byteBuffer2) {
                return true;
            }
            byteBuffer.rewind();
            byteBuffer2.rewind();
            return Intrinsics.areEqual(byteBuffer, byteBuffer2);
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<MapBuffer.Entry> it = iterator();
        while (it.hasNext()) {
            MapBuffer.Entry next = it.next();
            sb.append(next.getKey());
            sb.append('=');
            int i = WhenMappings.$EnumSwitchMapping$0[next.getType().ordinal()];
            if (i == 1) {
                sb.append(next.getBooleanValue());
            } else if (i == 2) {
                sb.append(next.getIntValue());
            } else if (i == 3) {
                sb.append(next.getDoubleValue());
            } else if (i == 4) {
                sb.append(next.getStringValue());
            } else if (i == 5) {
                sb.append(next.getMapBufferValue().toString());
            }
            sb.append(',');
        }
        sb.append('}');
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "builder.toString()");
        return sb2;
    }

    @Override // java.lang.Iterable
    public Iterator<MapBuffer.Entry> iterator() {
        return new ReadableMapBuffer$iterator$1(this);
    }

    /* compiled from: ReadableMapBuffer.kt */
    @Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001bH\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0012\u001a\u00020\u00138VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00178VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001d¨\u0006!"}, d2 = {"Lcom/facebook/react/common/mapbuffer/ReadableMapBuffer$MapBufferEntry;", "Lcom/facebook/react/common/mapbuffer/MapBuffer$Entry;", "bucketOffset", "", "(Lcom/facebook/react/common/mapbuffer/ReadableMapBuffer;I)V", "booleanValue", "", "getBooleanValue", "()Z", "doubleValue", "", "getDoubleValue", "()D", "intValue", "getIntValue", "()I", "key", "getKey", "mapBufferValue", "Lcom/facebook/react/common/mapbuffer/MapBuffer;", "getMapBufferValue", "()Lcom/facebook/react/common/mapbuffer/MapBuffer;", "stringValue", "", "getStringValue", "()Ljava/lang/String;", "type", "Lcom/facebook/react/common/mapbuffer/MapBuffer$DataType;", "getType", "()Lcom/facebook/react/common/mapbuffer/MapBuffer$DataType;", "assertType", "", "expected", "ReactAndroid_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public final class MapBufferEntry implements MapBuffer.Entry {
        private final int bucketOffset;

        public MapBufferEntry(int i) {
            ReadableMapBuffer.this = r1;
            this.bucketOffset = i;
        }

        private final void assertType(MapBuffer.DataType dataType) {
            MapBuffer.DataType type = getType();
            if (dataType == type) {
                return;
            }
            throw new IllegalStateException(("Expected " + dataType + " for key: " + getKey() + " found " + type + " instead.").toString());
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public int getKey() {
            return ReadableMapBuffer.this.m82readUnsignedShortBwKQO78(this.bucketOffset) & UShort.MAX_VALUE;
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public MapBuffer.DataType getType() {
            return MapBuffer.DataType.values()[ReadableMapBuffer.this.m82readUnsignedShortBwKQO78(this.bucketOffset + 2) & UShort.MAX_VALUE];
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public double getDoubleValue() {
            assertType(MapBuffer.DataType.DOUBLE);
            return ReadableMapBuffer.this.readDoubleValue(this.bucketOffset + 4);
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public int getIntValue() {
            assertType(MapBuffer.DataType.INT);
            return ReadableMapBuffer.this.readIntValue(this.bucketOffset + 4);
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public boolean getBooleanValue() {
            assertType(MapBuffer.DataType.BOOL);
            return ReadableMapBuffer.this.readBooleanValue(this.bucketOffset + 4);
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public String getStringValue() {
            assertType(MapBuffer.DataType.STRING);
            return ReadableMapBuffer.this.readStringValue(this.bucketOffset + 4);
        }

        @Override // com.facebook.react.common.mapbuffer.MapBuffer.Entry
        public MapBuffer getMapBufferValue() {
            assertType(MapBuffer.DataType.MAP);
            return ReadableMapBuffer.this.readMapBufferValue(this.bucketOffset + 4);
        }
    }

    /* compiled from: ReadableMapBuffer.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/facebook/react/common/mapbuffer/ReadableMapBuffer$Companion;", "", "()V", "ALIGNMENT", "", "BUCKET_SIZE", "HEADER_SIZE", "TYPE_OFFSET", "VALUE_OFFSET", "ReactAndroid_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        MapBufferSoLoader.staticInit();
    }
}
