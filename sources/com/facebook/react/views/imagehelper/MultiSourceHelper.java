package com.facebook.react.views.imagehelper;

import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import java.util.List;

/* loaded from: classes.dex */
public class MultiSourceHelper {

    /* loaded from: classes.dex */
    public static class MultiSourceResult {
        private final ImageSource bestResult;
        private final ImageSource bestResultInCache;

        private MultiSourceResult(ImageSource imageSource, ImageSource imageSource2) {
            this.bestResult = imageSource;
            this.bestResultInCache = imageSource2;
        }

        public ImageSource getBestResult() {
            return this.bestResult;
        }

        public ImageSource getBestResultInCache() {
            return this.bestResultInCache;
        }
    }

    public static MultiSourceResult getBestSourceForSize(int i, int i2, List<ImageSource> list) {
        return getBestSourceForSize(i, i2, list, 1.0d);
    }

    public static MultiSourceResult getBestSourceForSize(int i, int i2, List<ImageSource> list, double d) {
        if (list.isEmpty()) {
            return new MultiSourceResult(null, null);
        }
        if (list.size() == 1) {
            return new MultiSourceResult(list.get(0), null);
        }
        if (i <= 0 || i2 <= 0) {
            return new MultiSourceResult(null, null);
        }
        ImagePipeline imagePipeline = ImagePipelineFactory.getInstance().getImagePipeline();
        double d2 = i * i2 * d;
        double d3 = Double.MAX_VALUE;
        double d4 = Double.MAX_VALUE;
        ImageSource imageSource = null;
        ImageSource imageSource2 = null;
        for (ImageSource imageSource3 : list) {
            double abs = Math.abs(1.0d - (imageSource3.getSize() / d2));
            if (abs < d3) {
                imageSource2 = imageSource3;
                d3 = abs;
            }
            if (abs < d4 && (imagePipeline.isInBitmapMemoryCache(imageSource3.getUri()) || imagePipeline.isInDiskCacheSync(imageSource3.getUri()))) {
                imageSource = imageSource3;
                d4 = abs;
            }
        }
        if (imageSource != null && imageSource2 != null && imageSource.getSource().equals(imageSource2.getSource())) {
            imageSource = null;
        }
        return new MultiSourceResult(imageSource2, imageSource);
    }
}
