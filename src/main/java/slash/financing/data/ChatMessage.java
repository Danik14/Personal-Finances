package slash.financing.data;

import lombok.*;
import slash.financing.enums.ChatUserStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private ChatUserStatus status;
}