package collectors;

public interface CollectorFactory<T extends Collector> {

    public T create();

}
