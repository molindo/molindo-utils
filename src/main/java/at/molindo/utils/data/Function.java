package at.molindo.utils.data;

import javax.annotation.Nullable;

public interface Function<F, T> {
	T apply(@Nullable F input);
}
