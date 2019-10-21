package name.skm.graph_test.graph;

import java.io.Serializable;

public class Vertex<T> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6353171250537551089L;
    private T value;

    public Vertex(T value) {
        super();
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex<?> other = (Vertex<?>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Vertex [value=" + value + "]";
    }

}
