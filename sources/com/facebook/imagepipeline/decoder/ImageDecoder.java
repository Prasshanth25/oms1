package com.facebook.imagepipeline.decoder;

import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.QualityInfo;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public interface ImageDecoder {
    CloseableImage decode(@Nonnull EncodedImage encodedImage, int length, @Nonnull QualityInfo qualityInfo, @Nonnull ImageDecodeOptions options);
}
