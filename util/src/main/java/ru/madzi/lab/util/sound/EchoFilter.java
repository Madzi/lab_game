package ru.madzi.lab.util.sound;

/**
 * @author Dmitry Eliseev (deliseev@madzi.ru)
 */
public class EchoFilter extends SoundFilter {

    private short[] delayBuffer;
    private int delayBufferPos;
    private float decay;

    public EchoFilter(int numDelaySamples, float decay) {
        delayBuffer = new short[numDelaySamples];
        this.decay = decay;
    }

    public int getRemainingSize() {
        float finalDecay = 0.01f;
        int numRemainingBuffers = (int) Math.ceil(Math.log(finalDecay)/Math.log(decay));
        int bufferSize = delayBuffer.length * 2;
        return bufferSize * numRemainingBuffers;
    }

    public void reset() {
        for (short db : delayBuffer) {
            db = 0;
        }
        delayBufferPos = 0;
    }

    public void filter(byte[] samples, int offset, int length) {
        for (int i = offset; i < offset + length; i += 2) {
            short oldSample = getSample(samples, i);
            short newSample = (short) (oldSample + decay * delayBuffer[delayBufferPos]);
            setSample(samples, i, newSample);
            delayBuffer[delayBufferPos] = newSample;
            delayBufferPos = delayBufferPos == delayBuffer.length ? 0 : delayBufferPos + 1;
        }
    }

}
