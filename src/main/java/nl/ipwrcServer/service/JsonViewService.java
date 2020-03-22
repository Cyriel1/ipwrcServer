package nl.ipwrcServer.service;

public class JsonViewService {
    public static class Internal extends Private {}
    public static class Private extends Protected {}
    public static class Protected extends Public {}
    public static class Public {}
}
