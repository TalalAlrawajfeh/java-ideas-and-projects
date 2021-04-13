package tree;

import java.util.List;

public interface TreeConverter<R, T> {
	public R convert(List<T> ts);
}
