package calibrationcurves.db;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class Pair {
    final private String display;
    final private int num;

    public Pair(int num, String display)
    {
        this.display = display;
        this.num = num;
    }

    @Override
    public String toString()
    {
        return display;
    }

    public int getNum()
    {
        return num;
    }
}
