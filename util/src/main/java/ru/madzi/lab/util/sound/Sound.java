package com.e2.games.tools.sound;

/**
 * @author Dmitry Eliseev (deliseev@madzi.ru)
 */
public class Sound {

    private byte[] samples;

    public Sound(byte[] samples) {
        this.samples = samples;
    }

    public byte[] getSamples() {
        return samples;
    }

}
