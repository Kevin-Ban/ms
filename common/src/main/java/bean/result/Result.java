package bean.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private boolean success;

    private String msg;

    private T data;

    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMsg(msg);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setSuccess(false);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }
}
