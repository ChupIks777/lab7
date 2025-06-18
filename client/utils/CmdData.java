package utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CmdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String token;
    private final String name;
    private final List<Object> args;
    
    public CmdData(String token, String name, List<Object> args) {
        this.token = token;
        this.name = name;
        this.args = args;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public List<Object> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "CmdData{" +
                "token='" + token + '\'' +
                "name='" + name + '\'' +
                ", args=" + args +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CmdData cmdData = (CmdData) o;
        return Objects.equals(name, cmdData.name) && Objects.equals(args, cmdData.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, args);
    }

}
