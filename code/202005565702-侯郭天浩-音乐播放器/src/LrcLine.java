public class LrcLine {
    // 歌词时间
    private long time = 0;

    // 歌词内容
    private String lyric = "";

    public void setTime(String time)
    {
        // 设置时间
        String strTime[] = time.split(":|\\.");
        this.time = Integer.parseInt(strTime[0]) * 60
                + Integer.parseInt(strTime[1]);
        
    }

    public void setLyric(String lyric)
    {
        // 设置歌词
        this.lyric = lyric;
    }

    public long getTime()
    {
        return time;
    }

    public String getLrc()
    {
        return lyric;
    }
}
