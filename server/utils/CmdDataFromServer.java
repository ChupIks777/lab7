package utils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CmdDataFromServer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private final List<Object> args;
    
    public CmdDataFromServer(String name, List<Object> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<Object> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "CmdDataFromServer{" +
                "name='" + name + '\'' +
                ", args=" + args +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CmdDataFromServer cmdDataFromServer = (CmdDataFromServer) o;
        return Objects.equals(name, cmdDataFromServer.name) && Objects.equals(args, cmdDataFromServer.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, args);
    }

}