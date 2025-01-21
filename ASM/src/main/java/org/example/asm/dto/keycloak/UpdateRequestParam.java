package org.example.asm.dto.keycloak;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestParam {
    private String email;
    private String firstName;
    private String lastName;
}
