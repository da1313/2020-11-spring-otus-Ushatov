package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private Boolean result;

    private Boolean isEmailError;

    private Boolean isPasswordError;

}
