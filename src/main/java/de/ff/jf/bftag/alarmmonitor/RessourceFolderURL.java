package de.ff.jf.bftag.alarmmonitor;

public class RessourceFolderURL {
    public static final String ressourceFolderBaseURL = RessourceFolderURL.class.getClassLoader().getResource("files").getPath();
    public static final String configurtationFolderBaseURL = RessourceFolderURL.class.getClassLoader().getResource("configuration").getPath();
}
