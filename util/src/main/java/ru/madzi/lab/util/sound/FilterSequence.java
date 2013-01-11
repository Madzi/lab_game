package com.e2.games.tools.sound;

/**
 * @author Dmitry Eliseev (deliseev@madzi.ru)
 */
public class FilterSequence extends SoundFilter {

    private SoundFilter[] filters;

    public  int getRemainingSize() {
        int max = 0;
        for (SoundFilter filter : filters) {
            max = Math.max(max, filter.getRemainingSize());
        }
        return max;
    }

    public void reset() {
        for (SoundFilter filter : filters) {
            filter.reset();
        }
    }

    public void filter(byte[] samples, int offset, int length) {
        for (SoundFilter filter : filters) {
            filter.filter(samples, offset, length);
        }
    }

}
