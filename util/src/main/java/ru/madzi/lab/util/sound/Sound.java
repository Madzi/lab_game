package ru.madzi.lab.util.sound;

public class Sound {

    private byte[] samples;

    public Sound(byte[] samples) {
        this.samples = samples;
    }

    public byte[] getSamples() {
        return samples;
    }

}
