package com.facebook.imagepipeline.transcoder;

import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface ImageTranscoder {
    boolean canResize(EncodedImage encodedImage, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions);

    boolean canTranscode(ImageFormat imageFormat);

    String getIdentifier();

    ImageTranscodeResult transcode(EncodedImage encodedImage, OutputStream outputStream, @Nullable RotationOptions rotationOptions, @Nullable ResizeOptions resizeOptions, @Nullable ImageFormat outputFormat, @Nullable Integer quality) throws IOException;
}
