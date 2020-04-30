package bean.message;

import bean.User;
import lombok.Data;

@Data
public class OrderMessage {

    private long orderId;

    private User user;
}
