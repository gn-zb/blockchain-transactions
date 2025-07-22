package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    String transactionId; // hash
    String address;
    Double amount; // * -1
    String timestamp; // unix timestamp
}
