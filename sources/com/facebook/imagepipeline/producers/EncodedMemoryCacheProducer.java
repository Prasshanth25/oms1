package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.hermes.intl.Constants;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class EncodedMemoryCacheProducer implements Producer<EncodedImage> {
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String PRODUCER_NAME = "EncodedMemoryCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer<EncodedImage> mInputProducer;
    private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;

    public EncodedMemoryCacheProducer(MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> inputProducer) {
        this.mMemoryCache = memoryCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = inputProducer;
    }

    @Override // com.facebook.imagepipeline.producers.Producer
    public void produceResults(final Consumer<EncodedImage> consumer, final ProducerContext producerContext) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("EncodedMemoryCacheProducer#produceResults");
            }
            ProducerListener2 producerListener = producerContext.getProducerListener();
            producerListener.onProducerStart(producerContext, PRODUCER_NAME);
            CacheKey encodedCacheKey = this.mCacheKeyFactory.getEncodedCacheKey(producerContext.getImageRequest(), producerContext.getCallerContext());
            CloseableReference<PooledByteBuffer> closeableReference = this.mMemoryCache.get(encodedCacheKey);
            if (closeableReference != null) {
                EncodedImage encodedImage = new EncodedImage(closeableReference);
                producerListener.onProducerFinishWithSuccess(producerContext, PRODUCER_NAME, producerListener.requiresExtraMap(producerContext, PRODUCER_NAME) ? ImmutableMap.of("cached_value_found", "true") : null);
                producerListener.onUltimateProducerReached(producerContext, PRODUCER_NAME, true);
                producerContext.putOriginExtra("memory_encoded");
                consumer.onProgressUpdate(1.0f);
                consumer.onNewResult(encodedImage, 1);
                EncodedImage.closeSafely(encodedImage);
                CloseableReference.closeSafely(closeableReference);
            } else if (producerContext.getLowestPermittedRequestLevel().getValue() < ImageRequest.RequestLevel.ENCODED_MEMORY_CACHE.getValue()) {
                EncodedMemoryCacheConsumer encodedMemoryCacheConsumer = new EncodedMemoryCacheConsumer(consumer, this.mMemoryCache, encodedCacheKey, producerContext.getImageRequest().isMemoryCacheEnabled(), producerContext.getImagePipelineConfig().getExperiments().isEncodedCacheEnabled());
                producerListener.onProducerFinishWithSuccess(producerContext, PRODUCER_NAME, producerListener.requiresExtraMap(producerContext, PRODUCER_NAME) ? ImmutableMap.of("cached_value_found", Constants.CASEFIRST_FALSE) : null);
                this.mInputProducer.produceResults(encodedMemoryCacheConsumer, producerContext);
                CloseableReference.closeSafely(closeableReference);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } else {
                producerListener.onProducerFinishWithSuccess(producerContext, PRODUCER_NAME, producerListener.requiresExtraMap(producerContext, PRODUCER_NAME) ? ImmutableMap.of("cached_value_found", Constants.CASEFIRST_FALSE) : null);
                producerListener.onUltimateProducerReached(producerContext, PRODUCER_NAME, false);
                producerContext.putOriginExtra("memory_encoded", "nil-result");
                consumer.onNewResult(null, 1);
                CloseableReference.closeSafely(closeableReference);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class EncodedMemoryCacheConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final boolean mEncodedCacheEnabled;
        private final boolean mIsMemoryCacheEnabled;
        private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;
        private final CacheKey mRequestedCacheKey;

        public EncodedMemoryCacheConsumer(Consumer<EncodedImage> consumer, MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKey requestedCacheKey, boolean isMemoryCacheEnabled, boolean encodedCacheEnabled) {
            super(consumer);
            this.mMemoryCache = memoryCache;
            this.mRequestedCacheKey = requestedCacheKey;
            this.mIsMemoryCacheEnabled = isMemoryCacheEnabled;
            this.mEncodedCacheEnabled = encodedCacheEnabled;
        }

        @Override // com.facebook.imagepipeline.producers.BaseConsumer
        public void onNewResultImpl(@Nullable EncodedImage newResult, int status) {
            boolean isTracing;
            try {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.beginSection("EncodedMemoryCacheProducer#onNewResultImpl");
                }
                if (!isNotLast(status) && newResult != null && !statusHasAnyFlag(status, 10) && newResult.getImageFormat() != ImageFormat.UNKNOWN) {
                    CloseableReference<PooledByteBuffer> byteBufferRef = newResult.getByteBufferRef();
                    if (byteBufferRef != null) {
                        CloseableReference<PooledByteBuffer> cache = (this.mEncodedCacheEnabled && this.mIsMemoryCacheEnabled) ? this.mMemoryCache.cache(this.mRequestedCacheKey, byteBufferRef) : null;
                        CloseableReference.closeSafely(byteBufferRef);
                        if (cache != null) {
                            EncodedImage encodedImage = new EncodedImage(cache);
                            encodedImage.copyMetaDataFrom(newResult);
                            CloseableReference.closeSafely(cache);
                            getConsumer().onProgressUpdate(1.0f);
                            getConsumer().onNewResult(encodedImage, status);
                            EncodedImage.closeSafely(encodedImage);
                            if (isTracing) {
                                return;
                            }
                            return;
                        }
                    }
                    getConsumer().onNewResult(newResult, status);
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                        return;
                    }
                    return;
                }
                getConsumer().onNewResult(newResult, status);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } finally {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        }
    }
}
