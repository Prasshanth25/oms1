package com.facebook.imagepipeline.producers;

import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class RemoveImageTransformMetaDataProducer implements Producer<CloseableReference<PooledByteBuffer>> {
    private final Producer<EncodedImage> mInputProducer;

    public RemoveImageTransformMetaDataProducer(Producer<EncodedImage> inputProducer) {
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(Consumer<CloseableReference<PooledByteBuffer>> consumer, ProducerContext context) {
        this.mInputProducer.produceResults(new RemoveImageTransformMetaDataConsumer(consumer), context);
    }

    /* loaded from: classes.dex */
    private class RemoveImageTransformMetaDataConsumer extends DelegatingConsumer<EncodedImage, CloseableReference<PooledByteBuffer>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private RemoveImageTransformMetaDataConsumer(Consumer<CloseableReference<PooledByteBuffer>> consumer) {
            super(consumer);
            RemoveImageTransformMetaDataProducer.this = this$0;
        }

        @Override // com.facebook.imagepipeline.producers.BaseConsumer
        public void onNewResultImpl(@Nullable EncodedImage newResult, int status) {
            try {
                r0 = EncodedImage.isValid(newResult) ? newResult.getByteBufferRef() : null;
                getConsumer().onNewResult(r0, status);
            } finally {
                CloseableReference.closeSafely(r0);
            }
        }
    }
}
