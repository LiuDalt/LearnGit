package com.example.administrator.transfer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.media.MediaCodec.BUFFER_FLAG_KEY_FRAME;

public class TransferHepler {
    public static final String TAG = "TransferHepler";
    private static final String MIME_TYPE = "video/avc";
    public static final String VIDEO_TYPE_PREFIX = "video";
    private static final long TIMEOUT_US = 1000;
    private int mSrcHeight;
    private int mSrcWidth;
    private MediaFormat mEncoderVideoFormat;
    private ByteBuffer[] mDecodeOutputBuffer;
    private ByteBuffer[] mDecodeInputBuffer;
    private String mSrcMime;
    private int mDstTrackIndex;
    private MediaMuxer mMuxer;
    private MediaFormat mSrcFormat;
    private int mSrcTrackIndex;
    private int VIDEO_READ_SAMPLE_SIZE = 1024 * 1024;
    private static final int BIT_RATE = 8 * 1024 * 1024;
    private MediaExtractor mExtractor;
    private String mSrcPath;
    private String mDstPath;
    private long mMaxTimeStamp;
    private MediaCodec mDecoder;
    private MediaCodec mEncoder;
    private boolean mIsAllKeyFrame = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void test(){
        String src = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "00t" + File.separator + "a.mp4";
        String dst = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "00t" + File.separator + "aa.mp4";
        TransferHepler transferHepler = new TransferHepler(src, dst);
        transferHepler.start();
    }

    public TransferHepler(String src, String dst){
        mSrcPath = src;
        mDstPath = dst;
        mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(mSrcPath);
            mSrcTrackIndex = getTrackIndex(mExtractor);
            mSrcFormat = mExtractor.getTrackFormat(mSrcTrackIndex);
            Log.i(TAG + 1, "input:" + mSrcFormat.toString());
            mSrcMime = mSrcFormat.getString(MediaFormat.KEY_MIME);
            mSrcWidth = mSrcFormat.getInteger(MediaFormat.KEY_WIDTH);
            mSrcHeight = mSrcFormat.getInteger(MediaFormat.KEY_HEIGHT);
            mExtractor.selectTrack(mSrcTrackIndex);

            mMuxer = new MediaMuxer(mDstPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//            mDstTrackIndex = mMuxer.addTrack(mSrcFormat);
            mMaxTimeStamp = mSrcFormat.getLong(MediaFormat.KEY_DURATION);
//            mMuxer.start();

            mDecoder = MediaCodec.createDecoderByType(MIME_TYPE);
            mDecoder.configure(mSrcFormat, null, null, 0);
            mDecoder.start();

            mEncoder = MediaCodec.createEncoderByType(mSrcMime);
            mEncoderVideoFormat = MediaFormat.createVideoFormat(mSrcMime, mSrcWidth, mSrcHeight);
            mEncoderVideoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, mSrcFormat.getInteger(MediaFormat.KEY_FRAME_RATE));
            mEncoderVideoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 0);
            mEncoderVideoFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            Integer roation = mSrcFormat.containsKey(MediaFormat.KEY_ROTATION) ? mSrcFormat.getInteger(MediaFormat.KEY_ROTATION) : 0;
            mEncoderVideoFormat.setInteger(MediaFormat.KEY_ROTATION,  roation);
            mEncoderVideoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            mEncoder.configure(mEncoderVideoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

            mEncoder.start();

            Log.i(TAG, "duration=" + mMaxTimeStamp + " frameRate=" + mSrcFormat.getInteger(MediaFormat.KEY_FRAME_RATE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    protected void requestKeyFrame() {
        if (mIsAllKeyFrame){
            try {
                Bundle reqKeyCmd = new Bundle();
                reqKeyCmd.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0);
                mEncoder.setParameters(reqKeyCmd);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        try {
            if(mMuxer != null) {
                mMuxer.stop();
                mMuxer.release();
            }
            if(mExtractor != null){
                mExtractor.release();
            }
            if(mDecoder != null){
                mDecoder.release();
            }
            if(mEncoder != null){
                mEncoder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTrackIndex(MediaExtractor extractor) {
        int count = extractor.getTrackCount();
        for(int i = 0; i < count; i++){
            MediaFormat format = extractor.getTrackFormat(i);
            if(format.getString(MediaFormat.KEY_MIME).startsWith(VIDEO_TYPE_PREFIX)){
                return i;
            }
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void start(){
//        handleTrack(mMuxer, mDstTrackIndex, mExtractor);
        decoderInputBuffer();
        release();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printOutputFile();
            }
        }).start();

    }

    private void printOutputFile() {
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(mDstPath);
            int index = getTrackIndex(extractor);
            MediaFormat format = extractor.getTrackFormat(index);
            Log.i(TAG + 1, "output:" + format.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void decoderInputBuffer() {
        while (true){
            int inputBufIndex = mDecoder.dequeueInputBuffer(TIMEOUT_US);
            if(inputBufIndex >= 0){
                ByteBuffer inputBuffer = mDecoder.getInputBuffer(inputBufIndex);
                inputBuffer.clear();
                int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
                long presentTimeStamp = mExtractor.getSampleTime();
                if(sampleSize < 0){
                    Log.i(TAG, "decode input sampleSize less than 0 ");
                    break;
                }
                mDecoder.queueInputBuffer(inputBufIndex, 0, sampleSize, presentTimeStamp, BUFFER_FLAG_KEY_FRAME);
                Log.i(TAG, "decoderInputBuffer inputBufferIndex=" + inputBufIndex + " sampleSize=" + sampleSize + " presetnTimeStamp=" + presentTimeStamp);
                decoderOutputBuffer();
                mExtractor.advance();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void decoderOutputBuffer() {
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int outputIndex = mDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
        if(outputIndex >= 0){
            byte[] chunk = new byte[info.size];
            ByteBuffer buffer = mDecoder.getOutputBuffer(outputIndex);
            buffer.position(info.offset);
            buffer.get(chunk);
            buffer.clear();
            mDecoder.releaseOutputBuffer(outputIndex, false);
            Log.i(TAG, "decoderOutputBuffer outputIndex= " + outputIndex + " chunk length=" + chunk.length + " presentTimeStamp=" + info.presentationTimeUs);
            if(chunk.length > 0) {
                encoderInputBuffer(chunk, info.presentationTimeUs);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void encoderInputBuffer(byte[] chunk, long presentationTimeUs) {
        int inputBufferIndex = mEncoder.dequeueInputBuffer(TIMEOUT_US);
        requestKeyFrame();
        if(inputBufferIndex >= 0){
            ByteBuffer inputBuffer = mEncoder.getInputBuffer(inputBufferIndex);
            inputBuffer.clear();
            inputBuffer.put(chunk);
            mEncoder.queueInputBuffer(inputBufferIndex, 0, chunk.length, presentationTimeUs, BUFFER_FLAG_KEY_FRAME);
            Log.i(TAG, "encoderInputBuffer inputBufferIndex=" + inputBufferIndex + " chunk length=" + chunk.length + " presentaiontTimeUs=" + presentationTimeUs);
            encoderOutputBuffer();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void encoderOutputBuffer() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
        int count = 0;
        while (true){
            if(outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER){
                count++;
                if(count == 5){
                    break;
                }
            }else if(outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED){
                Log.i(TAG, "encoderOutputBuffer output buffers changed");
            }else if(outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
                addDstTrack();
                Log.i(TAG, "encoderOutputBuffer output format changed and add dst track");
            }else if(outputBufferIndex < 0){
                Log.i(TAG, "encoderOutputBuffer output wrong");
            } else {
                int size = bufferInfo.size;
                ByteBuffer outputBuffer = mEncoder.getOutputBuffer(outputBufferIndex);
                outputBuffer.position(bufferInfo.offset);
                outputBuffer.limit(bufferInfo.offset + size);
                mMuxer.writeSampleData(mDstTrackIndex, outputBuffer, bufferInfo);
                mEncoder.releaseOutputBuffer(outputBufferIndex, false);
                bufferInfo.flags = BUFFER_FLAG_KEY_FRAME;
                Log.i(TAG, "encoderOutputBuffer outputBufferIndex=" + outputBufferIndex + " bufferSize=" + size + " presentTimeStamp=" + bufferInfo.presentationTimeUs);
            }
            outputBufferIndex = mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
        }
    }

    private void addDstTrack() {
        MediaFormat format = mEncoder.getOutputFormat();
        mDstTrackIndex = mMuxer.addTrack(format);
        mMuxer.start();
    }

    private boolean handleTrack(MediaMuxer mediaMuxer, int trackIndex, MediaExtractor extractor) {
        if(mediaMuxer == null || trackIndex < 0 || extractor == null){
            return false;
        }
        ByteBuffer inputBuffer = ByteBuffer.allocate(VIDEO_READ_SAMPLE_SIZE);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int sampleSize;
        while ((sampleSize = extractor.readSampleData(inputBuffer, 0)) > 0){
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs = extractor.getSampleTime();
            if(mMaxTimeStamp < info.presentationTimeUs){
                break;
            }
            extractor.advance();
            mediaMuxer.writeSampleData(trackIndex, inputBuffer, info);
        }
        return true;
    }
}
