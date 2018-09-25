package com.example.learnmedia.convert;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.example.learnmedia.ExtractMpegFramesTest14;

import java.io.IOException;

public class ConvertVideo {
    private static final String TAG = "convertVideo";
    private static final String OUTPUT_VIDEO_MIME_TYPE = "video/avc"; // H.264 Advanced Video Coding
    private String mSrcPath;
    private String mDstPath;
    private MediaCodec mDecoder;
    private MediaExtractor mExtractor;
    private int mSrcWidth;
    private int mSrcHeight;
    private DecodeOutputSurface mDecodeOutputSurface;
    private MediaFormat mSrcFormat;

    public static void run() throws IOException {
        ConvertVideo convertVideo = new ConvertVideo();
        convertVideo.start();
    }

    public void start() throws IOException {
        init();
    }

    private void init() throws IOException {
        initDecoder();
        initEncoder();
        initMuxer();
    }

    private void initMuxer() {

    }

    private void initEncoder() {
        MediaCodecInfo videoCodecInfo = selectCodec(OUTPUT_VIDEO_MIME_TYPE);
    }

    /**
     * Returns the first codec capable of encoding the specified MIME type, or null if no match was
     * found.
     */
    private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    private void initDecoder() throws IOException {
        mExtractor = new MediaExtractor();
        mExtractor.setDataSource(mSrcPath);
        int videoTrack = getVideoTrack();
        mExtractor.selectTrack(videoTrack);
        mSrcFormat = mExtractor.getTrackFormat(videoTrack);
        mSrcWidth = mSrcFormat.getInteger(MediaFormat.KEY_WIDTH);
        mSrcHeight = mSrcFormat.getInteger(MediaFormat.KEY_HEIGHT);
        Log.d(TAG, " src Video size is " + mSrcWidth + " x " + mSrcHeight);
        mDecodeOutputSurface = new DecodeOutputSurface(mSrcWidth, mSrcHeight);

        // Create a MediaCodec decoder, and configure it with the MediaFormat from the
        // extractor.  It's very important to use the format from the extractor because
        // it contains a copy of the CSD-0/CSD-1 codec-specific data chunks.
        String mime = mSrcFormat.getString(MediaFormat.KEY_MIME);
        mDecoder = MediaCodec.createDecoderByType(mime);
        mDecoder.configure(mSrcFormat, mDecodeOutputSurface.getSurface(), null, 0);
        mDecoder.start();
    }

    private int getVideoTrack() {
        int numTracks = mExtractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = mExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                    Log.d(TAG, "Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }
}
