package ru.madzi.lab.util.sound;

import ru.madzi.lab.util.graphics.Sprite;

public class Filter3d extends SoundFilter {

    private static final  int NUM_SHIFTING_SAMPLES = 500;

    private Sprite source;
    private Sprite listener;
    private int maxDistance;
    private float lastVolume;

    public Filter3d(Sprite source, Sprite listener, int maxDistance) {
        this.source = source;
        this.listener = listener;
        this.maxDistance = maxDistance;
        this.lastVolume = 0.0f;
    }

    public void filter(byte[] samples, int offset, int length) {
        if (source == null || listener == null) {
            return;
        }
        float dx = source.getX() - listener.getX();
        float dy = source.getY() - listener.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float newVolume = (maxDistance - distance) / maxDistance;
        if (newVolume <= 0) {
            newVolume = 0;
        }
        int shift = 0;
        for (int i = offset; i < offset + length; i += 2) {
            float volume = newVolume;
            if (shift < NUM_SHIFTING_SAMPLES) {
                volume = lastVolume + (newVolume - lastVolume) * shift / NUM_SHIFTING_SAMPLES;
                ++shift;
            }
            short oldSample = getSample(samples, i);
            short newSample = (short) (oldSample * volume);
            setSample(samples, i, newSample);
        }
        lastVolume = newVolume;
    }
}
