package app.controller.tool;

public class PathName {
    private String path = "";
    private String name = "";

    public PathName() {
        path = "";
        name = "";
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public PathName extended(String extension) {
        PathName extended = new PathName();
        extended.path = path.isEmpty() ? name : path.endsWith(".") ? path + name : path + "." + name;
        extended.name = extension;
        return extended;
    }

    public String getPathname() {
        return path.isEmpty() ? name : path.endsWith(".") ? path + name : path + "." + name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return path.hashCode() & name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PathName && path.equals(((PathName) obj).path) && name.equals(((PathName) obj).name);
    }
}