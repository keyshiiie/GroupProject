package utils;

public class StringList extends LinkedList<String> {
    public StringList() {
        super();
    }

    public StringList(java.util.Collection<String> strings) {
        super();
        this.addAll(strings);
    }
}