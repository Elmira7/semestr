package entities;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class User {

     private Long id;
     private String login;
     private String name;
     private String password;
     private String email;
     private String phoneNumber;
     private String role;
     private String uuid;

}
