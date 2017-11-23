package com.tikalk.antsmasher.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.tikalk.antsmasher.R;

/**
 * Created by motibartov on 21/11/2017.
 */

public class SoundHelper {

    private SoundPool mSoundPool;
    private MediaPlayer mMusicPlayer;
    private MediaPlayer mSmashSound;

    private boolean mLoaded;
    private float mVolume;

    private int smashedSoundId;
    private int gameOverSoundId;
    private int missedSoundId;
    private int oopsSoundId;

    public SoundHelper(Context context) {
        prepareMusicPlayer(context);
        prepareSoundPool(context);
    }

    private void prepareMusicPlayer(Context context) {
        //Using getApplicationContext will help to improve media player operation during configuration changes
        mSmashSound = MediaPlayer.create(context, R.raw.smash);
        mMusicPlayer = MediaPlayer.create(context, R.raw.ants_moving);
        mMusicPlayer.setVolume(.5f, .5f);
        mMusicPlayer.setLooping(true);
    }

    private void prepareSoundPool(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolume / maxVolume;

//        mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).build();

        } else {
            mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> mLoaded = true);

        smashedSoundId = mSoundPool.load(context, R.raw.smash, 1);
        oopsSoundId = mSoundPool.load(context, R.raw.ooops, 1);
        gameOverSoundId = mSoundPool.load(context, R.raw.game_over, 1);
        //  missedSoundId = mSoundPool.load(mActivity, R.raw.missed_ballon, 1);
    }

    public void playMusic() {
        if (mLoaded && mMusicPlayer != null && !mMusicPlayer.isPlaying()) {
            mMusicPlayer.start();
        }
    }

    public void pauseMusic() {
        if (mLoaded && mMusicPlayer != null && mMusicPlayer.isPlaying()) {
            mMusicPlayer.pause();
        }
    }

    public void playSmashedSound() {
        if (mLoaded) {
            mSoundPool.play(smashedSoundId, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void playOops() {
        if (mLoaded) {
            mSoundPool.play(oopsSoundId, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void playGameOver() {
        if (mLoaded) {
            mSoundPool.play(gameOverSoundId, mVolume, mVolume, 1, 0, 1f);
        }
    }


    public void playMissed() {
        if (mLoaded) {
            mSoundPool.play(missedSoundId, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void dispose() {
        mMusicPlayer.pause();
        mMusicPlayer.release();
    }
}

