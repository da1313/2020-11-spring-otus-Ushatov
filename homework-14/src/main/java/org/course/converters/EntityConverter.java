package org.course.converters;

public interface EntityConverter<T1, T2> {

    T2 convert(T1 input);

}
